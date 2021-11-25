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
import com.st1.itx.db.domain.JcicRel;
import com.st1.itx.db.domain.JcicRelId;
import com.st1.itx.db.repository.online.JcicRelRepository;
import com.st1.itx.db.repository.day.JcicRelRepositoryDay;
import com.st1.itx.db.repository.mon.JcicRelRepositoryMon;
import com.st1.itx.db.repository.hist.JcicRelRepositoryHist;
import com.st1.itx.db.service.JcicRelService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicRelService")
@Repository
public class JcicRelServiceImpl extends ASpringJpaParm implements JcicRelService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicRelRepository jcicRelRepos;

  @Autowired
  private JcicRelRepositoryDay jcicRelReposDay;

  @Autowired
  private JcicRelRepositoryMon jcicRelReposMon;

  @Autowired
  private JcicRelRepositoryHist jcicRelReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicRelRepos);
    org.junit.Assert.assertNotNull(jcicRelReposDay);
    org.junit.Assert.assertNotNull(jcicRelReposMon);
    org.junit.Assert.assertNotNull(jcicRelReposHist);
  }

  @Override
  public JcicRel findById(JcicRelId jcicRelId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicRelId);
    Optional<JcicRel> jcicRel = null;
    if (dbName.equals(ContentName.onDay))
      jcicRel = jcicRelReposDay.findById(jcicRelId);
    else if (dbName.equals(ContentName.onMon))
      jcicRel = jcicRelReposMon.findById(jcicRelId);
    else if (dbName.equals(ContentName.onHist))
      jcicRel = jcicRelReposHist.findById(jcicRelId);
    else 
      jcicRel = jcicRelRepos.findById(jcicRelId);
    JcicRel obj = jcicRel.isPresent() ? jcicRel.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicRel> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicRel> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYMD", "BankItem", "BranchItem", "CustId", "RelId", "TranCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYMD", "BankItem", "BranchItem", "CustId", "RelId", "TranCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicRelReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicRelReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicRelReposHist.findAll(pageable);
    else 
      slice = jcicRelRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicRel holdById(JcicRelId jcicRelId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicRelId);
    Optional<JcicRel> jcicRel = null;
    if (dbName.equals(ContentName.onDay))
      jcicRel = jcicRelReposDay.findByJcicRelId(jcicRelId);
    else if (dbName.equals(ContentName.onMon))
      jcicRel = jcicRelReposMon.findByJcicRelId(jcicRelId);
    else if (dbName.equals(ContentName.onHist))
      jcicRel = jcicRelReposHist.findByJcicRelId(jcicRelId);
    else 
      jcicRel = jcicRelRepos.findByJcicRelId(jcicRelId);
    return jcicRel.isPresent() ? jcicRel.get() : null;
  }

  @Override
  public JcicRel holdById(JcicRel jcicRel, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicRel.getJcicRelId());
    Optional<JcicRel> jcicRelT = null;
    if (dbName.equals(ContentName.onDay))
      jcicRelT = jcicRelReposDay.findByJcicRelId(jcicRel.getJcicRelId());
    else if (dbName.equals(ContentName.onMon))
      jcicRelT = jcicRelReposMon.findByJcicRelId(jcicRel.getJcicRelId());
    else if (dbName.equals(ContentName.onHist))
      jcicRelT = jcicRelReposHist.findByJcicRelId(jcicRel.getJcicRelId());
    else 
      jcicRelT = jcicRelRepos.findByJcicRelId(jcicRel.getJcicRelId());
    return jcicRelT.isPresent() ? jcicRelT.get() : null;
  }

  @Override
  public JcicRel insert(JcicRel jcicRel, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicRel.getJcicRelId());
    if (this.findById(jcicRel.getJcicRelId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicRel.setCreateEmpNo(empNot);

    if(jcicRel.getLastUpdateEmpNo() == null || jcicRel.getLastUpdateEmpNo().isEmpty())
      jcicRel.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicRelReposDay.saveAndFlush(jcicRel);	
    else if (dbName.equals(ContentName.onMon))
      return jcicRelReposMon.saveAndFlush(jcicRel);
    else if (dbName.equals(ContentName.onHist))
      return jcicRelReposHist.saveAndFlush(jcicRel);
    else 
    return jcicRelRepos.saveAndFlush(jcicRel);
  }

  @Override
  public JcicRel update(JcicRel jcicRel, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicRel.getJcicRelId());
    if (!empNot.isEmpty())
      jcicRel.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicRelReposDay.saveAndFlush(jcicRel);	
    else if (dbName.equals(ContentName.onMon))
      return jcicRelReposMon.saveAndFlush(jcicRel);
    else if (dbName.equals(ContentName.onHist))
      return jcicRelReposHist.saveAndFlush(jcicRel);
    else 
    return jcicRelRepos.saveAndFlush(jcicRel);
  }

  @Override
  public JcicRel update2(JcicRel jcicRel, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicRel.getJcicRelId());
    if (!empNot.isEmpty())
      jcicRel.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicRelReposDay.saveAndFlush(jcicRel);	
    else if (dbName.equals(ContentName.onMon))
      jcicRelReposMon.saveAndFlush(jcicRel);
    else if (dbName.equals(ContentName.onHist))
        jcicRelReposHist.saveAndFlush(jcicRel);
    else 
      jcicRelRepos.saveAndFlush(jcicRel);	
    return this.findById(jcicRel.getJcicRelId());
  }

  @Override
  public void delete(JcicRel jcicRel, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicRel.getJcicRelId());
    if (dbName.equals(ContentName.onDay)) {
      jcicRelReposDay.delete(jcicRel);	
      jcicRelReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicRelReposMon.delete(jcicRel);	
      jcicRelReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicRelReposHist.delete(jcicRel);
      jcicRelReposHist.flush();
    }
    else {
      jcicRelRepos.delete(jcicRel);
      jcicRelRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicRel> jcicRel, TitaVo... titaVo) throws DBException {
    if (jcicRel == null || jcicRel.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicRel t : jcicRel){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicRel = jcicRelReposDay.saveAll(jcicRel);	
      jcicRelReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicRel = jcicRelReposMon.saveAll(jcicRel);	
      jcicRelReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicRel = jcicRelReposHist.saveAll(jcicRel);
      jcicRelReposHist.flush();
    }
    else {
      jcicRel = jcicRelRepos.saveAll(jcicRel);
      jcicRelRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicRel> jcicRel, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicRel == null || jcicRel.size() == 0)
      throw new DBException(6);

    for (JcicRel t : jcicRel) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicRel = jcicRelReposDay.saveAll(jcicRel);	
      jcicRelReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicRel = jcicRelReposMon.saveAll(jcicRel);	
      jcicRelReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicRel = jcicRelReposHist.saveAll(jcicRel);
      jcicRelReposHist.flush();
    }
    else {
      jcicRel = jcicRelRepos.saveAll(jcicRel);
      jcicRelRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicRel> jcicRel, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicRel == null || jcicRel.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicRelReposDay.deleteAll(jcicRel);	
      jcicRelReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicRelReposMon.deleteAll(jcicRel);	
      jcicRelReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicRelReposHist.deleteAll(jcicRel);
      jcicRelReposHist.flush();
    }
    else {
      jcicRelRepos.deleteAll(jcicRel);
      jcicRelRepos.flush();
    }
  }

  @Override
  public void Usp_L8_JcicRel_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jcicRelReposDay.uspL8JcicrelUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jcicRelReposMon.uspL8JcicrelUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jcicRelReposHist.uspL8JcicrelUpd(TBSDYF, EmpNo);
   else
      jcicRelRepos.uspL8JcicrelUpd(TBSDYF, EmpNo);
  }

}
