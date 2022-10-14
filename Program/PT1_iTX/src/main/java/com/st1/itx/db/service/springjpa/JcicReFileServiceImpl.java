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
import com.st1.itx.db.domain.JcicReFile;
import com.st1.itx.db.domain.JcicReFileId;
import com.st1.itx.db.repository.online.JcicReFileRepository;
import com.st1.itx.db.repository.day.JcicReFileRepositoryDay;
import com.st1.itx.db.repository.mon.JcicReFileRepositoryMon;
import com.st1.itx.db.repository.hist.JcicReFileRepositoryHist;
import com.st1.itx.db.service.JcicReFileService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicReFileService")
@Repository
public class JcicReFileServiceImpl extends ASpringJpaParm implements JcicReFileService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicReFileRepository jcicReFileRepos;

  @Autowired
  private JcicReFileRepositoryDay jcicReFileReposDay;

  @Autowired
  private JcicReFileRepositoryMon jcicReFileReposMon;

  @Autowired
  private JcicReFileRepositoryHist jcicReFileReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicReFileRepos);
    org.junit.Assert.assertNotNull(jcicReFileReposDay);
    org.junit.Assert.assertNotNull(jcicReFileReposMon);
    org.junit.Assert.assertNotNull(jcicReFileReposHist);
  }

  @Override
  public JcicReFile findById(JcicReFileId jcicReFileId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicReFileId);
    Optional<JcicReFile> jcicReFile = null;
    if (dbName.equals(ContentName.onDay))
      jcicReFile = jcicReFileReposDay.findById(jcicReFileId);
    else if (dbName.equals(ContentName.onMon))
      jcicReFile = jcicReFileReposMon.findById(jcicReFileId);
    else if (dbName.equals(ContentName.onHist))
      jcicReFile = jcicReFileReposHist.findById(jcicReFileId);
    else 
      jcicReFile = jcicReFileRepos.findById(jcicReFileId);
    JcicReFile obj = jcicReFile.isPresent() ? jcicReFile.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicReFile> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicReFile> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "JcicDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "JcicDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicReFileReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicReFileReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicReFileReposHist.findAll(pageable);
    else 
      slice = jcicReFileRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicReFile holdById(JcicReFileId jcicReFileId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicReFileId);
    Optional<JcicReFile> jcicReFile = null;
    if (dbName.equals(ContentName.onDay))
      jcicReFile = jcicReFileReposDay.findByJcicReFileId(jcicReFileId);
    else if (dbName.equals(ContentName.onMon))
      jcicReFile = jcicReFileReposMon.findByJcicReFileId(jcicReFileId);
    else if (dbName.equals(ContentName.onHist))
      jcicReFile = jcicReFileReposHist.findByJcicReFileId(jcicReFileId);
    else 
      jcicReFile = jcicReFileRepos.findByJcicReFileId(jcicReFileId);
    return jcicReFile.isPresent() ? jcicReFile.get() : null;
  }

  @Override
  public JcicReFile holdById(JcicReFile jcicReFile, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicReFile.getJcicReFileId());
    Optional<JcicReFile> jcicReFileT = null;
    if (dbName.equals(ContentName.onDay))
      jcicReFileT = jcicReFileReposDay.findByJcicReFileId(jcicReFile.getJcicReFileId());
    else if (dbName.equals(ContentName.onMon))
      jcicReFileT = jcicReFileReposMon.findByJcicReFileId(jcicReFile.getJcicReFileId());
    else if (dbName.equals(ContentName.onHist))
      jcicReFileT = jcicReFileReposHist.findByJcicReFileId(jcicReFile.getJcicReFileId());
    else 
      jcicReFileT = jcicReFileRepos.findByJcicReFileId(jcicReFile.getJcicReFileId());
    return jcicReFileT.isPresent() ? jcicReFileT.get() : null;
  }

  @Override
  public JcicReFile insert(JcicReFile jcicReFile, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicReFile.getJcicReFileId());
    if (this.findById(jcicReFile.getJcicReFileId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicReFile.setCreateEmpNo(empNot);

    if(jcicReFile.getLastUpdateEmpNo() == null || jcicReFile.getLastUpdateEmpNo().isEmpty())
      jcicReFile.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicReFileReposDay.saveAndFlush(jcicReFile);	
    else if (dbName.equals(ContentName.onMon))
      return jcicReFileReposMon.saveAndFlush(jcicReFile);
    else if (dbName.equals(ContentName.onHist))
      return jcicReFileReposHist.saveAndFlush(jcicReFile);
    else 
    return jcicReFileRepos.saveAndFlush(jcicReFile);
  }

  @Override
  public JcicReFile update(JcicReFile jcicReFile, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicReFile.getJcicReFileId());
    if (!empNot.isEmpty())
      jcicReFile.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicReFileReposDay.saveAndFlush(jcicReFile);	
    else if (dbName.equals(ContentName.onMon))
      return jcicReFileReposMon.saveAndFlush(jcicReFile);
    else if (dbName.equals(ContentName.onHist))
      return jcicReFileReposHist.saveAndFlush(jcicReFile);
    else 
    return jcicReFileRepos.saveAndFlush(jcicReFile);
  }

  @Override
  public JcicReFile update2(JcicReFile jcicReFile, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicReFile.getJcicReFileId());
    if (!empNot.isEmpty())
      jcicReFile.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicReFileReposDay.saveAndFlush(jcicReFile);	
    else if (dbName.equals(ContentName.onMon))
      jcicReFileReposMon.saveAndFlush(jcicReFile);
    else if (dbName.equals(ContentName.onHist))
        jcicReFileReposHist.saveAndFlush(jcicReFile);
    else 
      jcicReFileRepos.saveAndFlush(jcicReFile);	
    return this.findById(jcicReFile.getJcicReFileId());
  }

  @Override
  public void delete(JcicReFile jcicReFile, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicReFile.getJcicReFileId());
    if (dbName.equals(ContentName.onDay)) {
      jcicReFileReposDay.delete(jcicReFile);	
      jcicReFileReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicReFileReposMon.delete(jcicReFile);	
      jcicReFileReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicReFileReposHist.delete(jcicReFile);
      jcicReFileReposHist.flush();
    }
    else {
      jcicReFileRepos.delete(jcicReFile);
      jcicReFileRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicReFile> jcicReFile, TitaVo... titaVo) throws DBException {
    if (jcicReFile == null || jcicReFile.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicReFile t : jcicReFile){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicReFile = jcicReFileReposDay.saveAll(jcicReFile);	
      jcicReFileReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicReFile = jcicReFileReposMon.saveAll(jcicReFile);	
      jcicReFileReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicReFile = jcicReFileReposHist.saveAll(jcicReFile);
      jcicReFileReposHist.flush();
    }
    else {
      jcicReFile = jcicReFileRepos.saveAll(jcicReFile);
      jcicReFileRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicReFile> jcicReFile, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicReFile == null || jcicReFile.size() == 0)
      throw new DBException(6);

    for (JcicReFile t : jcicReFile) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicReFile = jcicReFileReposDay.saveAll(jcicReFile);	
      jcicReFileReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicReFile = jcicReFileReposMon.saveAll(jcicReFile);	
      jcicReFileReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicReFile = jcicReFileReposHist.saveAll(jcicReFile);
      jcicReFileReposHist.flush();
    }
    else {
      jcicReFile = jcicReFileRepos.saveAll(jcicReFile);
      jcicReFileRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicReFile> jcicReFile, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicReFile == null || jcicReFile.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicReFileReposDay.deleteAll(jcicReFile);	
      jcicReFileReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicReFileReposMon.deleteAll(jcicReFile);	
      jcicReFileReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicReFileReposHist.deleteAll(jcicReFile);
      jcicReFileReposHist.flush();
    }
    else {
      jcicReFileRepos.deleteAll(jcicReFile);
      jcicReFileRepos.flush();
    }
  }

}
