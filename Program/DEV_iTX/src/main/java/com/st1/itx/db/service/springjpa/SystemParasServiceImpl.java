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
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.repository.online.SystemParasRepository;
import com.st1.itx.db.repository.day.SystemParasRepositoryDay;
import com.st1.itx.db.repository.mon.SystemParasRepositoryMon;
import com.st1.itx.db.repository.hist.SystemParasRepositoryHist;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("systemParasService")
@Repository
public class SystemParasServiceImpl extends ASpringJpaParm implements SystemParasService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private SystemParasRepository systemParasRepos;

  @Autowired
  private SystemParasRepositoryDay systemParasReposDay;

  @Autowired
  private SystemParasRepositoryMon systemParasReposMon;

  @Autowired
  private SystemParasRepositoryHist systemParasReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(systemParasRepos);
    org.junit.Assert.assertNotNull(systemParasReposDay);
    org.junit.Assert.assertNotNull(systemParasReposMon);
    org.junit.Assert.assertNotNull(systemParasReposHist);
  }

  @Override
  public SystemParas findById(String businessType, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + businessType);
    Optional<SystemParas> systemParas = null;
    if (dbName.equals(ContentName.onDay))
      systemParas = systemParasReposDay.findById(businessType);
    else if (dbName.equals(ContentName.onMon))
      systemParas = systemParasReposMon.findById(businessType);
    else if (dbName.equals(ContentName.onHist))
      systemParas = systemParasReposHist.findById(businessType);
    else 
      systemParas = systemParasRepos.findById(businessType);
    SystemParas obj = systemParas.isPresent() ? systemParas.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<SystemParas> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<SystemParas> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "BusinessType"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "BusinessType"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = systemParasReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = systemParasReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = systemParasReposHist.findAll(pageable);
    else 
      slice = systemParasRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public SystemParas holdById(String businessType, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + businessType);
    Optional<SystemParas> systemParas = null;
    if (dbName.equals(ContentName.onDay))
      systemParas = systemParasReposDay.findByBusinessType(businessType);
    else if (dbName.equals(ContentName.onMon))
      systemParas = systemParasReposMon.findByBusinessType(businessType);
    else if (dbName.equals(ContentName.onHist))
      systemParas = systemParasReposHist.findByBusinessType(businessType);
    else 
      systemParas = systemParasRepos.findByBusinessType(businessType);
    return systemParas.isPresent() ? systemParas.get() : null;
  }

  @Override
  public SystemParas holdById(SystemParas systemParas, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + systemParas.getBusinessType());
    Optional<SystemParas> systemParasT = null;
    if (dbName.equals(ContentName.onDay))
      systemParasT = systemParasReposDay.findByBusinessType(systemParas.getBusinessType());
    else if (dbName.equals(ContentName.onMon))
      systemParasT = systemParasReposMon.findByBusinessType(systemParas.getBusinessType());
    else if (dbName.equals(ContentName.onHist))
      systemParasT = systemParasReposHist.findByBusinessType(systemParas.getBusinessType());
    else 
      systemParasT = systemParasRepos.findByBusinessType(systemParas.getBusinessType());
    return systemParasT.isPresent() ? systemParasT.get() : null;
  }

  @Override
  public SystemParas insert(SystemParas systemParas, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + systemParas.getBusinessType());
    if (this.findById(systemParas.getBusinessType()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      systemParas.setCreateEmpNo(empNot);

    if(systemParas.getLastUpdateEmpNo() == null || systemParas.getLastUpdateEmpNo().isEmpty())
      systemParas.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return systemParasReposDay.saveAndFlush(systemParas);	
    else if (dbName.equals(ContentName.onMon))
      return systemParasReposMon.saveAndFlush(systemParas);
    else if (dbName.equals(ContentName.onHist))
      return systemParasReposHist.saveAndFlush(systemParas);
    else 
    return systemParasRepos.saveAndFlush(systemParas);
  }

  @Override
  public SystemParas update(SystemParas systemParas, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + systemParas.getBusinessType());
    if (!empNot.isEmpty())
      systemParas.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return systemParasReposDay.saveAndFlush(systemParas);	
    else if (dbName.equals(ContentName.onMon))
      return systemParasReposMon.saveAndFlush(systemParas);
    else if (dbName.equals(ContentName.onHist))
      return systemParasReposHist.saveAndFlush(systemParas);
    else 
    return systemParasRepos.saveAndFlush(systemParas);
  }

  @Override
  public SystemParas update2(SystemParas systemParas, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + systemParas.getBusinessType());
    if (!empNot.isEmpty())
      systemParas.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      systemParasReposDay.saveAndFlush(systemParas);	
    else if (dbName.equals(ContentName.onMon))
      systemParasReposMon.saveAndFlush(systemParas);
    else if (dbName.equals(ContentName.onHist))
        systemParasReposHist.saveAndFlush(systemParas);
    else 
      systemParasRepos.saveAndFlush(systemParas);	
    return this.findById(systemParas.getBusinessType());
  }

  @Override
  public void delete(SystemParas systemParas, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + systemParas.getBusinessType());
    if (dbName.equals(ContentName.onDay)) {
      systemParasReposDay.delete(systemParas);	
      systemParasReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      systemParasReposMon.delete(systemParas);	
      systemParasReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      systemParasReposHist.delete(systemParas);
      systemParasReposHist.flush();
    }
    else {
      systemParasRepos.delete(systemParas);
      systemParasRepos.flush();
    }
   }

  @Override
  public void insertAll(List<SystemParas> systemParas, TitaVo... titaVo) throws DBException {
    if (systemParas == null || systemParas.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (SystemParas t : systemParas){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      systemParas = systemParasReposDay.saveAll(systemParas);	
      systemParasReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      systemParas = systemParasReposMon.saveAll(systemParas);	
      systemParasReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      systemParas = systemParasReposHist.saveAll(systemParas);
      systemParasReposHist.flush();
    }
    else {
      systemParas = systemParasRepos.saveAll(systemParas);
      systemParasRepos.flush();
    }
    }

  @Override
  public void updateAll(List<SystemParas> systemParas, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (systemParas == null || systemParas.size() == 0)
      throw new DBException(6);

    for (SystemParas t : systemParas) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      systemParas = systemParasReposDay.saveAll(systemParas);	
      systemParasReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      systemParas = systemParasReposMon.saveAll(systemParas);	
      systemParasReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      systemParas = systemParasReposHist.saveAll(systemParas);
      systemParasReposHist.flush();
    }
    else {
      systemParas = systemParasRepos.saveAll(systemParas);
      systemParasRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<SystemParas> systemParas, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (systemParas == null || systemParas.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      systemParasReposDay.deleteAll(systemParas);	
      systemParasReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      systemParasReposMon.deleteAll(systemParas);	
      systemParasReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      systemParasReposHist.deleteAll(systemParas);
      systemParasReposHist.flush();
    }
    else {
      systemParasRepos.deleteAll(systemParas);
      systemParasRepos.flush();
    }
  }

}
