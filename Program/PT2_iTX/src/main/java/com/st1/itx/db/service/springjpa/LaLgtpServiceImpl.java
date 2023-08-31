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
import com.st1.itx.db.domain.LaLgtp;
import com.st1.itx.db.domain.LaLgtpId;
import com.st1.itx.db.repository.online.LaLgtpRepository;
import com.st1.itx.db.repository.day.LaLgtpRepositoryDay;
import com.st1.itx.db.repository.mon.LaLgtpRepositoryMon;
import com.st1.itx.db.repository.hist.LaLgtpRepositoryHist;
import com.st1.itx.db.service.LaLgtpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("laLgtpService")
@Repository
public class LaLgtpServiceImpl extends ASpringJpaParm implements LaLgtpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LaLgtpRepository laLgtpRepos;

  @Autowired
  private LaLgtpRepositoryDay laLgtpReposDay;

  @Autowired
  private LaLgtpRepositoryMon laLgtpReposMon;

  @Autowired
  private LaLgtpRepositoryHist laLgtpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(laLgtpRepos);
    org.junit.Assert.assertNotNull(laLgtpReposDay);
    org.junit.Assert.assertNotNull(laLgtpReposMon);
    org.junit.Assert.assertNotNull(laLgtpReposHist);
  }

  @Override
  public LaLgtp findById(LaLgtpId laLgtpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + laLgtpId);
    Optional<LaLgtp> laLgtp = null;
    if (dbName.equals(ContentName.onDay))
      laLgtp = laLgtpReposDay.findById(laLgtpId);
    else if (dbName.equals(ContentName.onMon))
      laLgtp = laLgtpReposMon.findById(laLgtpId);
    else if (dbName.equals(ContentName.onHist))
      laLgtp = laLgtpReposHist.findById(laLgtpId);
    else 
      laLgtp = laLgtpRepos.findById(laLgtpId);
    LaLgtp obj = laLgtp.isPresent() ? laLgtp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LaLgtp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LaLgtp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Cusbrh", "Gdrid1", "Gdrid2", "Gdrnum", "Lgtseq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Cusbrh", "Gdrid1", "Gdrid2", "Gdrnum", "Lgtseq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = laLgtpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = laLgtpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = laLgtpReposHist.findAll(pageable);
    else 
      slice = laLgtpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LaLgtp holdById(LaLgtpId laLgtpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + laLgtpId);
    Optional<LaLgtp> laLgtp = null;
    if (dbName.equals(ContentName.onDay))
      laLgtp = laLgtpReposDay.findByLaLgtpId(laLgtpId);
    else if (dbName.equals(ContentName.onMon))
      laLgtp = laLgtpReposMon.findByLaLgtpId(laLgtpId);
    else if (dbName.equals(ContentName.onHist))
      laLgtp = laLgtpReposHist.findByLaLgtpId(laLgtpId);
    else 
      laLgtp = laLgtpRepos.findByLaLgtpId(laLgtpId);
    return laLgtp.isPresent() ? laLgtp.get() : null;
  }

  @Override
  public LaLgtp holdById(LaLgtp laLgtp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + laLgtp.getLaLgtpId());
    Optional<LaLgtp> laLgtpT = null;
    if (dbName.equals(ContentName.onDay))
      laLgtpT = laLgtpReposDay.findByLaLgtpId(laLgtp.getLaLgtpId());
    else if (dbName.equals(ContentName.onMon))
      laLgtpT = laLgtpReposMon.findByLaLgtpId(laLgtp.getLaLgtpId());
    else if (dbName.equals(ContentName.onHist))
      laLgtpT = laLgtpReposHist.findByLaLgtpId(laLgtp.getLaLgtpId());
    else 
      laLgtpT = laLgtpRepos.findByLaLgtpId(laLgtp.getLaLgtpId());
    return laLgtpT.isPresent() ? laLgtpT.get() : null;
  }

  @Override
  public LaLgtp insert(LaLgtp laLgtp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + laLgtp.getLaLgtpId());
    if (this.findById(laLgtp.getLaLgtpId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      laLgtp.setCreateEmpNo(empNot);

    if(laLgtp.getLastUpdateEmpNo() == null || laLgtp.getLastUpdateEmpNo().isEmpty())
      laLgtp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return laLgtpReposDay.saveAndFlush(laLgtp);	
    else if (dbName.equals(ContentName.onMon))
      return laLgtpReposMon.saveAndFlush(laLgtp);
    else if (dbName.equals(ContentName.onHist))
      return laLgtpReposHist.saveAndFlush(laLgtp);
    else 
    return laLgtpRepos.saveAndFlush(laLgtp);
  }

  @Override
  public LaLgtp update(LaLgtp laLgtp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + laLgtp.getLaLgtpId());
    if (!empNot.isEmpty())
      laLgtp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return laLgtpReposDay.saveAndFlush(laLgtp);	
    else if (dbName.equals(ContentName.onMon))
      return laLgtpReposMon.saveAndFlush(laLgtp);
    else if (dbName.equals(ContentName.onHist))
      return laLgtpReposHist.saveAndFlush(laLgtp);
    else 
    return laLgtpRepos.saveAndFlush(laLgtp);
  }

  @Override
  public LaLgtp update2(LaLgtp laLgtp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + laLgtp.getLaLgtpId());
    if (!empNot.isEmpty())
      laLgtp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      laLgtpReposDay.saveAndFlush(laLgtp);	
    else if (dbName.equals(ContentName.onMon))
      laLgtpReposMon.saveAndFlush(laLgtp);
    else if (dbName.equals(ContentName.onHist))
        laLgtpReposHist.saveAndFlush(laLgtp);
    else 
      laLgtpRepos.saveAndFlush(laLgtp);	
    return this.findById(laLgtp.getLaLgtpId());
  }

  @Override
  public void delete(LaLgtp laLgtp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + laLgtp.getLaLgtpId());
    if (dbName.equals(ContentName.onDay)) {
      laLgtpReposDay.delete(laLgtp);	
      laLgtpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      laLgtpReposMon.delete(laLgtp);	
      laLgtpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      laLgtpReposHist.delete(laLgtp);
      laLgtpReposHist.flush();
    }
    else {
      laLgtpRepos.delete(laLgtp);
      laLgtpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LaLgtp> laLgtp, TitaVo... titaVo) throws DBException {
    if (laLgtp == null || laLgtp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (LaLgtp t : laLgtp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      laLgtp = laLgtpReposDay.saveAll(laLgtp);	
      laLgtpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      laLgtp = laLgtpReposMon.saveAll(laLgtp);	
      laLgtpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      laLgtp = laLgtpReposHist.saveAll(laLgtp);
      laLgtpReposHist.flush();
    }
    else {
      laLgtp = laLgtpRepos.saveAll(laLgtp);
      laLgtpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LaLgtp> laLgtp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (laLgtp == null || laLgtp.size() == 0)
      throw new DBException(6);

    for (LaLgtp t : laLgtp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      laLgtp = laLgtpReposDay.saveAll(laLgtp);	
      laLgtpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      laLgtp = laLgtpReposMon.saveAll(laLgtp);	
      laLgtpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      laLgtp = laLgtpReposHist.saveAll(laLgtp);
      laLgtpReposHist.flush();
    }
    else {
      laLgtp = laLgtpRepos.saveAll(laLgtp);
      laLgtpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LaLgtp> laLgtp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (laLgtp == null || laLgtp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      laLgtpReposDay.deleteAll(laLgtp);	
      laLgtpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      laLgtpReposMon.deleteAll(laLgtp);	
      laLgtpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      laLgtpReposHist.deleteAll(laLgtp);
      laLgtpReposHist.flush();
    }
    else {
      laLgtpRepos.deleteAll(laLgtp);
      laLgtpRepos.flush();
    }
  }

}
