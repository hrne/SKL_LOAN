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
import com.st1.itx.db.domain.Lagdtp;
import com.st1.itx.db.domain.LagdtpId;
import com.st1.itx.db.repository.online.LagdtpRepository;
import com.st1.itx.db.repository.day.LagdtpRepositoryDay;
import com.st1.itx.db.repository.mon.LagdtpRepositoryMon;
import com.st1.itx.db.repository.hist.LagdtpRepositoryHist;
import com.st1.itx.db.service.LagdtpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("lagdtpService")
@Repository
public class LagdtpServiceImpl extends ASpringJpaParm implements LagdtpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LagdtpRepository lagdtpRepos;

  @Autowired
  private LagdtpRepositoryDay lagdtpReposDay;

  @Autowired
  private LagdtpRepositoryMon lagdtpReposMon;

  @Autowired
  private LagdtpRepositoryHist lagdtpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(lagdtpRepos);
    org.junit.Assert.assertNotNull(lagdtpReposDay);
    org.junit.Assert.assertNotNull(lagdtpReposMon);
    org.junit.Assert.assertNotNull(lagdtpReposHist);
  }

  @Override
  public Lagdtp findById(LagdtpId lagdtpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + lagdtpId);
    Optional<Lagdtp> lagdtp = null;
    if (dbName.equals(ContentName.onDay))
      lagdtp = lagdtpReposDay.findById(lagdtpId);
    else if (dbName.equals(ContentName.onMon))
      lagdtp = lagdtpReposMon.findById(lagdtpId);
    else if (dbName.equals(ContentName.onHist))
      lagdtp = lagdtpReposHist.findById(lagdtpId);
    else 
      lagdtp = lagdtpRepos.findById(lagdtpId);
    Lagdtp obj = lagdtp.isPresent() ? lagdtp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<Lagdtp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<Lagdtp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Cusbrh", "Gdrid1", "Gdrid2", "Gdrnum"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Cusbrh", "Gdrid1", "Gdrid2", "Gdrnum"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = lagdtpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = lagdtpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = lagdtpReposHist.findAll(pageable);
    else 
      slice = lagdtpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Lagdtp holdById(LagdtpId lagdtpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + lagdtpId);
    Optional<Lagdtp> lagdtp = null;
    if (dbName.equals(ContentName.onDay))
      lagdtp = lagdtpReposDay.findByLagdtpId(lagdtpId);
    else if (dbName.equals(ContentName.onMon))
      lagdtp = lagdtpReposMon.findByLagdtpId(lagdtpId);
    else if (dbName.equals(ContentName.onHist))
      lagdtp = lagdtpReposHist.findByLagdtpId(lagdtpId);
    else 
      lagdtp = lagdtpRepos.findByLagdtpId(lagdtpId);
    return lagdtp.isPresent() ? lagdtp.get() : null;
  }

  @Override
  public Lagdtp holdById(Lagdtp lagdtp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + lagdtp.getLagdtpId());
    Optional<Lagdtp> lagdtpT = null;
    if (dbName.equals(ContentName.onDay))
      lagdtpT = lagdtpReposDay.findByLagdtpId(lagdtp.getLagdtpId());
    else if (dbName.equals(ContentName.onMon))
      lagdtpT = lagdtpReposMon.findByLagdtpId(lagdtp.getLagdtpId());
    else if (dbName.equals(ContentName.onHist))
      lagdtpT = lagdtpReposHist.findByLagdtpId(lagdtp.getLagdtpId());
    else 
      lagdtpT = lagdtpRepos.findByLagdtpId(lagdtp.getLagdtpId());
    return lagdtpT.isPresent() ? lagdtpT.get() : null;
  }

  @Override
  public Lagdtp insert(Lagdtp lagdtp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + lagdtp.getLagdtpId());
    if (this.findById(lagdtp.getLagdtpId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      lagdtp.setCreateEmpNo(empNot);

    if(lagdtp.getLastUpdateEmpNo() == null || lagdtp.getLastUpdateEmpNo().isEmpty())
      lagdtp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return lagdtpReposDay.saveAndFlush(lagdtp);	
    else if (dbName.equals(ContentName.onMon))
      return lagdtpReposMon.saveAndFlush(lagdtp);
    else if (dbName.equals(ContentName.onHist))
      return lagdtpReposHist.saveAndFlush(lagdtp);
    else 
    return lagdtpRepos.saveAndFlush(lagdtp);
  }

  @Override
  public Lagdtp update(Lagdtp lagdtp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + lagdtp.getLagdtpId());
    if (!empNot.isEmpty())
      lagdtp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return lagdtpReposDay.saveAndFlush(lagdtp);	
    else if (dbName.equals(ContentName.onMon))
      return lagdtpReposMon.saveAndFlush(lagdtp);
    else if (dbName.equals(ContentName.onHist))
      return lagdtpReposHist.saveAndFlush(lagdtp);
    else 
    return lagdtpRepos.saveAndFlush(lagdtp);
  }

  @Override
  public Lagdtp update2(Lagdtp lagdtp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + lagdtp.getLagdtpId());
    if (!empNot.isEmpty())
      lagdtp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      lagdtpReposDay.saveAndFlush(lagdtp);	
    else if (dbName.equals(ContentName.onMon))
      lagdtpReposMon.saveAndFlush(lagdtp);
    else if (dbName.equals(ContentName.onHist))
        lagdtpReposHist.saveAndFlush(lagdtp);
    else 
      lagdtpRepos.saveAndFlush(lagdtp);	
    return this.findById(lagdtp.getLagdtpId());
  }

  @Override
  public void delete(Lagdtp lagdtp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + lagdtp.getLagdtpId());
    if (dbName.equals(ContentName.onDay)) {
      lagdtpReposDay.delete(lagdtp);	
      lagdtpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      lagdtpReposMon.delete(lagdtp);	
      lagdtpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      lagdtpReposHist.delete(lagdtp);
      lagdtpReposHist.flush();
    }
    else {
      lagdtpRepos.delete(lagdtp);
      lagdtpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<Lagdtp> lagdtp, TitaVo... titaVo) throws DBException {
    if (lagdtp == null || lagdtp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (Lagdtp t : lagdtp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      lagdtp = lagdtpReposDay.saveAll(lagdtp);	
      lagdtpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      lagdtp = lagdtpReposMon.saveAll(lagdtp);	
      lagdtpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      lagdtp = lagdtpReposHist.saveAll(lagdtp);
      lagdtpReposHist.flush();
    }
    else {
      lagdtp = lagdtpRepos.saveAll(lagdtp);
      lagdtpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<Lagdtp> lagdtp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (lagdtp == null || lagdtp.size() == 0)
      throw new DBException(6);

    for (Lagdtp t : lagdtp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      lagdtp = lagdtpReposDay.saveAll(lagdtp);	
      lagdtpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      lagdtp = lagdtpReposMon.saveAll(lagdtp);	
      lagdtpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      lagdtp = lagdtpReposHist.saveAll(lagdtp);
      lagdtpReposHist.flush();
    }
    else {
      lagdtp = lagdtpRepos.saveAll(lagdtp);
      lagdtpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<Lagdtp> lagdtp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (lagdtp == null || lagdtp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      lagdtpReposDay.deleteAll(lagdtp);	
      lagdtpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      lagdtpReposMon.deleteAll(lagdtp);	
      lagdtpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      lagdtpReposHist.deleteAll(lagdtp);
      lagdtpReposHist.flush();
    }
    else {
      lagdtpRepos.deleteAll(lagdtp);
      lagdtpRepos.flush();
    }
  }

}
