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
import com.st1.itx.db.domain.JcicB201;
import com.st1.itx.db.domain.JcicB201Id;
import com.st1.itx.db.repository.online.JcicB201Repository;
import com.st1.itx.db.repository.day.JcicB201RepositoryDay;
import com.st1.itx.db.repository.mon.JcicB201RepositoryMon;
import com.st1.itx.db.repository.hist.JcicB201RepositoryHist;
import com.st1.itx.db.service.JcicB201Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicB201Service")
@Repository
public class JcicB201ServiceImpl extends ASpringJpaParm implements JcicB201Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicB201Repository jcicB201Repos;

  @Autowired
  private JcicB201RepositoryDay jcicB201ReposDay;

  @Autowired
  private JcicB201RepositoryMon jcicB201ReposMon;

  @Autowired
  private JcicB201RepositoryHist jcicB201ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicB201Repos);
    org.junit.Assert.assertNotNull(jcicB201ReposDay);
    org.junit.Assert.assertNotNull(jcicB201ReposMon);
    org.junit.Assert.assertNotNull(jcicB201ReposHist);
  }

  @Override
  public JcicB201 findById(JcicB201Id jcicB201Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicB201Id);
    Optional<JcicB201> jcicB201 = null;
    if (dbName.equals(ContentName.onDay))
      jcicB201 = jcicB201ReposDay.findById(jcicB201Id);
    else if (dbName.equals(ContentName.onMon))
      jcicB201 = jcicB201ReposMon.findById(jcicB201Id);
    else if (dbName.equals(ContentName.onHist))
      jcicB201 = jcicB201ReposHist.findById(jcicB201Id);
    else 
      jcicB201 = jcicB201Repos.findById(jcicB201Id);
    JcicB201 obj = jcicB201.isPresent() ? jcicB201.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicB201> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicB201> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "BankItem", "BranchItem", "TranCode", "SubTranCode", "AcctNo", "SeqNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "BankItem", "BranchItem", "TranCode", "SubTranCode", "AcctNo", "SeqNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicB201ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicB201ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicB201ReposHist.findAll(pageable);
    else 
      slice = jcicB201Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicB201 holdById(JcicB201Id jcicB201Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicB201Id);
    Optional<JcicB201> jcicB201 = null;
    if (dbName.equals(ContentName.onDay))
      jcicB201 = jcicB201ReposDay.findByJcicB201Id(jcicB201Id);
    else if (dbName.equals(ContentName.onMon))
      jcicB201 = jcicB201ReposMon.findByJcicB201Id(jcicB201Id);
    else if (dbName.equals(ContentName.onHist))
      jcicB201 = jcicB201ReposHist.findByJcicB201Id(jcicB201Id);
    else 
      jcicB201 = jcicB201Repos.findByJcicB201Id(jcicB201Id);
    return jcicB201.isPresent() ? jcicB201.get() : null;
  }

  @Override
  public JcicB201 holdById(JcicB201 jcicB201, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicB201.getJcicB201Id());
    Optional<JcicB201> jcicB201T = null;
    if (dbName.equals(ContentName.onDay))
      jcicB201T = jcicB201ReposDay.findByJcicB201Id(jcicB201.getJcicB201Id());
    else if (dbName.equals(ContentName.onMon))
      jcicB201T = jcicB201ReposMon.findByJcicB201Id(jcicB201.getJcicB201Id());
    else if (dbName.equals(ContentName.onHist))
      jcicB201T = jcicB201ReposHist.findByJcicB201Id(jcicB201.getJcicB201Id());
    else 
      jcicB201T = jcicB201Repos.findByJcicB201Id(jcicB201.getJcicB201Id());
    return jcicB201T.isPresent() ? jcicB201T.get() : null;
  }

  @Override
  public JcicB201 insert(JcicB201 jcicB201, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicB201.getJcicB201Id());
    if (this.findById(jcicB201.getJcicB201Id(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicB201.setCreateEmpNo(empNot);

    if(jcicB201.getLastUpdateEmpNo() == null || jcicB201.getLastUpdateEmpNo().isEmpty())
      jcicB201.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicB201ReposDay.saveAndFlush(jcicB201);	
    else if (dbName.equals(ContentName.onMon))
      return jcicB201ReposMon.saveAndFlush(jcicB201);
    else if (dbName.equals(ContentName.onHist))
      return jcicB201ReposHist.saveAndFlush(jcicB201);
    else 
    return jcicB201Repos.saveAndFlush(jcicB201);
  }

  @Override
  public JcicB201 update(JcicB201 jcicB201, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicB201.getJcicB201Id());
    if (!empNot.isEmpty())
      jcicB201.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicB201ReposDay.saveAndFlush(jcicB201);	
    else if (dbName.equals(ContentName.onMon))
      return jcicB201ReposMon.saveAndFlush(jcicB201);
    else if (dbName.equals(ContentName.onHist))
      return jcicB201ReposHist.saveAndFlush(jcicB201);
    else 
    return jcicB201Repos.saveAndFlush(jcicB201);
  }

  @Override
  public JcicB201 update2(JcicB201 jcicB201, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicB201.getJcicB201Id());
    if (!empNot.isEmpty())
      jcicB201.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicB201ReposDay.saveAndFlush(jcicB201);	
    else if (dbName.equals(ContentName.onMon))
      jcicB201ReposMon.saveAndFlush(jcicB201);
    else if (dbName.equals(ContentName.onHist))
        jcicB201ReposHist.saveAndFlush(jcicB201);
    else 
      jcicB201Repos.saveAndFlush(jcicB201);	
    return this.findById(jcicB201.getJcicB201Id());
  }

  @Override
  public void delete(JcicB201 jcicB201, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicB201.getJcicB201Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicB201ReposDay.delete(jcicB201);	
      jcicB201ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB201ReposMon.delete(jcicB201);	
      jcicB201ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB201ReposHist.delete(jcicB201);
      jcicB201ReposHist.flush();
    }
    else {
      jcicB201Repos.delete(jcicB201);
      jcicB201Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicB201> jcicB201, TitaVo... titaVo) throws DBException {
    if (jcicB201 == null || jcicB201.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicB201 t : jcicB201){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicB201 = jcicB201ReposDay.saveAll(jcicB201);	
      jcicB201ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB201 = jcicB201ReposMon.saveAll(jcicB201);	
      jcicB201ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB201 = jcicB201ReposHist.saveAll(jcicB201);
      jcicB201ReposHist.flush();
    }
    else {
      jcicB201 = jcicB201Repos.saveAll(jcicB201);
      jcicB201Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicB201> jcicB201, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicB201 == null || jcicB201.size() == 0)
      throw new DBException(6);

    for (JcicB201 t : jcicB201) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicB201 = jcicB201ReposDay.saveAll(jcicB201);	
      jcicB201ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB201 = jcicB201ReposMon.saveAll(jcicB201);	
      jcicB201ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB201 = jcicB201ReposHist.saveAll(jcicB201);
      jcicB201ReposHist.flush();
    }
    else {
      jcicB201 = jcicB201Repos.saveAll(jcicB201);
      jcicB201Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicB201> jcicB201, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicB201 == null || jcicB201.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicB201ReposDay.deleteAll(jcicB201);	
      jcicB201ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB201ReposMon.deleteAll(jcicB201);	
      jcicB201ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB201ReposHist.deleteAll(jcicB201);
      jcicB201ReposHist.flush();
    }
    else {
      jcicB201Repos.deleteAll(jcicB201);
      jcicB201Repos.flush();
    }
  }

  @Override
  public void Usp_L8_JcicB201_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jcicB201ReposDay.uspL8Jcicb201Upd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jcicB201ReposMon.uspL8Jcicb201Upd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jcicB201ReposHist.uspL8Jcicb201Upd(TBSDYF, EmpNo);
   else
      jcicB201Repos.uspL8Jcicb201Upd(TBSDYF, EmpNo);
  }

}
