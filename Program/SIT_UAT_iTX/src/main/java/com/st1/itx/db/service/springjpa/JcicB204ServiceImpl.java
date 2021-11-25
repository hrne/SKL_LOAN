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
import com.st1.itx.db.domain.JcicB204;
import com.st1.itx.db.domain.JcicB204Id;
import com.st1.itx.db.repository.online.JcicB204Repository;
import com.st1.itx.db.repository.day.JcicB204RepositoryDay;
import com.st1.itx.db.repository.mon.JcicB204RepositoryMon;
import com.st1.itx.db.repository.hist.JcicB204RepositoryHist;
import com.st1.itx.db.service.JcicB204Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicB204Service")
@Repository
public class JcicB204ServiceImpl extends ASpringJpaParm implements JcicB204Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicB204Repository jcicB204Repos;

  @Autowired
  private JcicB204RepositoryDay jcicB204ReposDay;

  @Autowired
  private JcicB204RepositoryMon jcicB204ReposMon;

  @Autowired
  private JcicB204RepositoryHist jcicB204ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicB204Repos);
    org.junit.Assert.assertNotNull(jcicB204ReposDay);
    org.junit.Assert.assertNotNull(jcicB204ReposMon);
    org.junit.Assert.assertNotNull(jcicB204ReposHist);
  }

  @Override
  public JcicB204 findById(JcicB204Id jcicB204Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicB204Id);
    Optional<JcicB204> jcicB204 = null;
    if (dbName.equals(ContentName.onDay))
      jcicB204 = jcicB204ReposDay.findById(jcicB204Id);
    else if (dbName.equals(ContentName.onMon))
      jcicB204 = jcicB204ReposMon.findById(jcicB204Id);
    else if (dbName.equals(ContentName.onHist))
      jcicB204 = jcicB204ReposHist.findById(jcicB204Id);
    else 
      jcicB204 = jcicB204Repos.findById(jcicB204Id);
    JcicB204 obj = jcicB204.isPresent() ? jcicB204.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicB204> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicB204> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYMD", "BankItem", "BranchItem", "DataDate", "AcctNo", "CustId", "AcctCode", "SubAcctCode", "SubTranCode", "SeqNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYMD", "BankItem", "BranchItem", "DataDate", "AcctNo", "CustId", "AcctCode", "SubAcctCode", "SubTranCode", "SeqNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicB204ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicB204ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicB204ReposHist.findAll(pageable);
    else 
      slice = jcicB204Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicB204 holdById(JcicB204Id jcicB204Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicB204Id);
    Optional<JcicB204> jcicB204 = null;
    if (dbName.equals(ContentName.onDay))
      jcicB204 = jcicB204ReposDay.findByJcicB204Id(jcicB204Id);
    else if (dbName.equals(ContentName.onMon))
      jcicB204 = jcicB204ReposMon.findByJcicB204Id(jcicB204Id);
    else if (dbName.equals(ContentName.onHist))
      jcicB204 = jcicB204ReposHist.findByJcicB204Id(jcicB204Id);
    else 
      jcicB204 = jcicB204Repos.findByJcicB204Id(jcicB204Id);
    return jcicB204.isPresent() ? jcicB204.get() : null;
  }

  @Override
  public JcicB204 holdById(JcicB204 jcicB204, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicB204.getJcicB204Id());
    Optional<JcicB204> jcicB204T = null;
    if (dbName.equals(ContentName.onDay))
      jcicB204T = jcicB204ReposDay.findByJcicB204Id(jcicB204.getJcicB204Id());
    else if (dbName.equals(ContentName.onMon))
      jcicB204T = jcicB204ReposMon.findByJcicB204Id(jcicB204.getJcicB204Id());
    else if (dbName.equals(ContentName.onHist))
      jcicB204T = jcicB204ReposHist.findByJcicB204Id(jcicB204.getJcicB204Id());
    else 
      jcicB204T = jcicB204Repos.findByJcicB204Id(jcicB204.getJcicB204Id());
    return jcicB204T.isPresent() ? jcicB204T.get() : null;
  }

  @Override
  public JcicB204 insert(JcicB204 jcicB204, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicB204.getJcicB204Id());
    if (this.findById(jcicB204.getJcicB204Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicB204.setCreateEmpNo(empNot);

    if(jcicB204.getLastUpdateEmpNo() == null || jcicB204.getLastUpdateEmpNo().isEmpty())
      jcicB204.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicB204ReposDay.saveAndFlush(jcicB204);	
    else if (dbName.equals(ContentName.onMon))
      return jcicB204ReposMon.saveAndFlush(jcicB204);
    else if (dbName.equals(ContentName.onHist))
      return jcicB204ReposHist.saveAndFlush(jcicB204);
    else 
    return jcicB204Repos.saveAndFlush(jcicB204);
  }

  @Override
  public JcicB204 update(JcicB204 jcicB204, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicB204.getJcicB204Id());
    if (!empNot.isEmpty())
      jcicB204.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicB204ReposDay.saveAndFlush(jcicB204);	
    else if (dbName.equals(ContentName.onMon))
      return jcicB204ReposMon.saveAndFlush(jcicB204);
    else if (dbName.equals(ContentName.onHist))
      return jcicB204ReposHist.saveAndFlush(jcicB204);
    else 
    return jcicB204Repos.saveAndFlush(jcicB204);
  }

  @Override
  public JcicB204 update2(JcicB204 jcicB204, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicB204.getJcicB204Id());
    if (!empNot.isEmpty())
      jcicB204.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicB204ReposDay.saveAndFlush(jcicB204);	
    else if (dbName.equals(ContentName.onMon))
      jcicB204ReposMon.saveAndFlush(jcicB204);
    else if (dbName.equals(ContentName.onHist))
        jcicB204ReposHist.saveAndFlush(jcicB204);
    else 
      jcicB204Repos.saveAndFlush(jcicB204);	
    return this.findById(jcicB204.getJcicB204Id());
  }

  @Override
  public void delete(JcicB204 jcicB204, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicB204.getJcicB204Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicB204ReposDay.delete(jcicB204);	
      jcicB204ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB204ReposMon.delete(jcicB204);	
      jcicB204ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB204ReposHist.delete(jcicB204);
      jcicB204ReposHist.flush();
    }
    else {
      jcicB204Repos.delete(jcicB204);
      jcicB204Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicB204> jcicB204, TitaVo... titaVo) throws DBException {
    if (jcicB204 == null || jcicB204.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicB204 t : jcicB204){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicB204 = jcicB204ReposDay.saveAll(jcicB204);	
      jcicB204ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB204 = jcicB204ReposMon.saveAll(jcicB204);	
      jcicB204ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB204 = jcicB204ReposHist.saveAll(jcicB204);
      jcicB204ReposHist.flush();
    }
    else {
      jcicB204 = jcicB204Repos.saveAll(jcicB204);
      jcicB204Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicB204> jcicB204, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicB204 == null || jcicB204.size() == 0)
      throw new DBException(6);

    for (JcicB204 t : jcicB204) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicB204 = jcicB204ReposDay.saveAll(jcicB204);	
      jcicB204ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB204 = jcicB204ReposMon.saveAll(jcicB204);	
      jcicB204ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB204 = jcicB204ReposHist.saveAll(jcicB204);
      jcicB204ReposHist.flush();
    }
    else {
      jcicB204 = jcicB204Repos.saveAll(jcicB204);
      jcicB204Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicB204> jcicB204, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicB204 == null || jcicB204.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicB204ReposDay.deleteAll(jcicB204);	
      jcicB204ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB204ReposMon.deleteAll(jcicB204);	
      jcicB204ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB204ReposHist.deleteAll(jcicB204);
      jcicB204ReposHist.flush();
    }
    else {
      jcicB204Repos.deleteAll(jcicB204);
      jcicB204Repos.flush();
    }
  }

  @Override
  public void Usp_L8_JcicB204_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jcicB204ReposDay.uspL8Jcicb204Upd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jcicB204ReposMon.uspL8Jcicb204Upd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jcicB204ReposHist.uspL8Jcicb204Upd(TBSDYF, EmpNo);
   else
      jcicB204Repos.uspL8Jcicb204Upd(TBSDYF, EmpNo);
  }

}
