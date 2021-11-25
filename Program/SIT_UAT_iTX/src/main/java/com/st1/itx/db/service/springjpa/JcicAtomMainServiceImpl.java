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
import com.st1.itx.db.domain.JcicAtomMain;
import com.st1.itx.db.repository.online.JcicAtomMainRepository;
import com.st1.itx.db.repository.day.JcicAtomMainRepositoryDay;
import com.st1.itx.db.repository.mon.JcicAtomMainRepositoryMon;
import com.st1.itx.db.repository.hist.JcicAtomMainRepositoryHist;
import com.st1.itx.db.service.JcicAtomMainService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicAtomMainService")
@Repository
public class JcicAtomMainServiceImpl extends ASpringJpaParm implements JcicAtomMainService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicAtomMainRepository jcicAtomMainRepos;

  @Autowired
  private JcicAtomMainRepositoryDay jcicAtomMainReposDay;

  @Autowired
  private JcicAtomMainRepositoryMon jcicAtomMainReposMon;

  @Autowired
  private JcicAtomMainRepositoryHist jcicAtomMainReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicAtomMainRepos);
    org.junit.Assert.assertNotNull(jcicAtomMainReposDay);
    org.junit.Assert.assertNotNull(jcicAtomMainReposMon);
    org.junit.Assert.assertNotNull(jcicAtomMainReposHist);
  }

  @Override
  public JcicAtomMain findById(String functionCode, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + functionCode);
    Optional<JcicAtomMain> jcicAtomMain = null;
    if (dbName.equals(ContentName.onDay))
      jcicAtomMain = jcicAtomMainReposDay.findById(functionCode);
    else if (dbName.equals(ContentName.onMon))
      jcicAtomMain = jcicAtomMainReposMon.findById(functionCode);
    else if (dbName.equals(ContentName.onHist))
      jcicAtomMain = jcicAtomMainReposHist.findById(functionCode);
    else 
      jcicAtomMain = jcicAtomMainRepos.findById(functionCode);
    JcicAtomMain obj = jcicAtomMain.isPresent() ? jcicAtomMain.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicAtomMain> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicAtomMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "FunctionCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "FunctionCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicAtomMainReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicAtomMainReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicAtomMainReposHist.findAll(pageable);
    else 
      slice = jcicAtomMainRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicAtomMain holdById(String functionCode, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + functionCode);
    Optional<JcicAtomMain> jcicAtomMain = null;
    if (dbName.equals(ContentName.onDay))
      jcicAtomMain = jcicAtomMainReposDay.findByFunctionCode(functionCode);
    else if (dbName.equals(ContentName.onMon))
      jcicAtomMain = jcicAtomMainReposMon.findByFunctionCode(functionCode);
    else if (dbName.equals(ContentName.onHist))
      jcicAtomMain = jcicAtomMainReposHist.findByFunctionCode(functionCode);
    else 
      jcicAtomMain = jcicAtomMainRepos.findByFunctionCode(functionCode);
    return jcicAtomMain.isPresent() ? jcicAtomMain.get() : null;
  }

  @Override
  public JcicAtomMain holdById(JcicAtomMain jcicAtomMain, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicAtomMain.getFunctionCode());
    Optional<JcicAtomMain> jcicAtomMainT = null;
    if (dbName.equals(ContentName.onDay))
      jcicAtomMainT = jcicAtomMainReposDay.findByFunctionCode(jcicAtomMain.getFunctionCode());
    else if (dbName.equals(ContentName.onMon))
      jcicAtomMainT = jcicAtomMainReposMon.findByFunctionCode(jcicAtomMain.getFunctionCode());
    else if (dbName.equals(ContentName.onHist))
      jcicAtomMainT = jcicAtomMainReposHist.findByFunctionCode(jcicAtomMain.getFunctionCode());
    else 
      jcicAtomMainT = jcicAtomMainRepos.findByFunctionCode(jcicAtomMain.getFunctionCode());
    return jcicAtomMainT.isPresent() ? jcicAtomMainT.get() : null;
  }

  @Override
  public JcicAtomMain insert(JcicAtomMain jcicAtomMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicAtomMain.getFunctionCode());
    if (this.findById(jcicAtomMain.getFunctionCode()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicAtomMain.setCreateEmpNo(empNot);

    if(jcicAtomMain.getLastUpdateEmpNo() == null || jcicAtomMain.getLastUpdateEmpNo().isEmpty())
      jcicAtomMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicAtomMainReposDay.saveAndFlush(jcicAtomMain);	
    else if (dbName.equals(ContentName.onMon))
      return jcicAtomMainReposMon.saveAndFlush(jcicAtomMain);
    else if (dbName.equals(ContentName.onHist))
      return jcicAtomMainReposHist.saveAndFlush(jcicAtomMain);
    else 
    return jcicAtomMainRepos.saveAndFlush(jcicAtomMain);
  }

  @Override
  public JcicAtomMain update(JcicAtomMain jcicAtomMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicAtomMain.getFunctionCode());
    if (!empNot.isEmpty())
      jcicAtomMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicAtomMainReposDay.saveAndFlush(jcicAtomMain);	
    else if (dbName.equals(ContentName.onMon))
      return jcicAtomMainReposMon.saveAndFlush(jcicAtomMain);
    else if (dbName.equals(ContentName.onHist))
      return jcicAtomMainReposHist.saveAndFlush(jcicAtomMain);
    else 
    return jcicAtomMainRepos.saveAndFlush(jcicAtomMain);
  }

  @Override
  public JcicAtomMain update2(JcicAtomMain jcicAtomMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicAtomMain.getFunctionCode());
    if (!empNot.isEmpty())
      jcicAtomMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicAtomMainReposDay.saveAndFlush(jcicAtomMain);	
    else if (dbName.equals(ContentName.onMon))
      jcicAtomMainReposMon.saveAndFlush(jcicAtomMain);
    else if (dbName.equals(ContentName.onHist))
        jcicAtomMainReposHist.saveAndFlush(jcicAtomMain);
    else 
      jcicAtomMainRepos.saveAndFlush(jcicAtomMain);	
    return this.findById(jcicAtomMain.getFunctionCode());
  }

  @Override
  public void delete(JcicAtomMain jcicAtomMain, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicAtomMain.getFunctionCode());
    if (dbName.equals(ContentName.onDay)) {
      jcicAtomMainReposDay.delete(jcicAtomMain);	
      jcicAtomMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicAtomMainReposMon.delete(jcicAtomMain);	
      jcicAtomMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicAtomMainReposHist.delete(jcicAtomMain);
      jcicAtomMainReposHist.flush();
    }
    else {
      jcicAtomMainRepos.delete(jcicAtomMain);
      jcicAtomMainRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicAtomMain> jcicAtomMain, TitaVo... titaVo) throws DBException {
    if (jcicAtomMain == null || jcicAtomMain.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicAtomMain t : jcicAtomMain){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicAtomMain = jcicAtomMainReposDay.saveAll(jcicAtomMain);	
      jcicAtomMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicAtomMain = jcicAtomMainReposMon.saveAll(jcicAtomMain);	
      jcicAtomMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicAtomMain = jcicAtomMainReposHist.saveAll(jcicAtomMain);
      jcicAtomMainReposHist.flush();
    }
    else {
      jcicAtomMain = jcicAtomMainRepos.saveAll(jcicAtomMain);
      jcicAtomMainRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicAtomMain> jcicAtomMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicAtomMain == null || jcicAtomMain.size() == 0)
      throw new DBException(6);

    for (JcicAtomMain t : jcicAtomMain) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicAtomMain = jcicAtomMainReposDay.saveAll(jcicAtomMain);	
      jcicAtomMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicAtomMain = jcicAtomMainReposMon.saveAll(jcicAtomMain);	
      jcicAtomMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicAtomMain = jcicAtomMainReposHist.saveAll(jcicAtomMain);
      jcicAtomMainReposHist.flush();
    }
    else {
      jcicAtomMain = jcicAtomMainRepos.saveAll(jcicAtomMain);
      jcicAtomMainRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicAtomMain> jcicAtomMain, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicAtomMain == null || jcicAtomMain.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicAtomMainReposDay.deleteAll(jcicAtomMain);	
      jcicAtomMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicAtomMainReposMon.deleteAll(jcicAtomMain);	
      jcicAtomMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicAtomMainReposHist.deleteAll(jcicAtomMain);
      jcicAtomMainReposHist.flush();
    }
    else {
      jcicAtomMainRepos.deleteAll(jcicAtomMain);
      jcicAtomMainRepos.flush();
    }
  }

}
