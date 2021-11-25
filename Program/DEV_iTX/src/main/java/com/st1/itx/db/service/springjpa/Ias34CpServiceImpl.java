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
import com.st1.itx.db.domain.Ias34Cp;
import com.st1.itx.db.domain.Ias34CpId;
import com.st1.itx.db.repository.online.Ias34CpRepository;
import com.st1.itx.db.repository.day.Ias34CpRepositoryDay;
import com.st1.itx.db.repository.mon.Ias34CpRepositoryMon;
import com.st1.itx.db.repository.hist.Ias34CpRepositoryHist;
import com.st1.itx.db.service.Ias34CpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("ias34CpService")
@Repository
public class Ias34CpServiceImpl extends ASpringJpaParm implements Ias34CpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private Ias34CpRepository ias34CpRepos;

  @Autowired
  private Ias34CpRepositoryDay ias34CpReposDay;

  @Autowired
  private Ias34CpRepositoryMon ias34CpReposMon;

  @Autowired
  private Ias34CpRepositoryHist ias34CpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(ias34CpRepos);
    org.junit.Assert.assertNotNull(ias34CpReposDay);
    org.junit.Assert.assertNotNull(ias34CpReposMon);
    org.junit.Assert.assertNotNull(ias34CpReposHist);
  }

  @Override
  public Ias34Cp findById(Ias34CpId ias34CpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + ias34CpId);
    Optional<Ias34Cp> ias34Cp = null;
    if (dbName.equals(ContentName.onDay))
      ias34Cp = ias34CpReposDay.findById(ias34CpId);
    else if (dbName.equals(ContentName.onMon))
      ias34Cp = ias34CpReposMon.findById(ias34CpId);
    else if (dbName.equals(ContentName.onHist))
      ias34Cp = ias34CpReposHist.findById(ias34CpId);
    else 
      ias34Cp = ias34CpRepos.findById(ias34CpId);
    Ias34Cp obj = ias34Cp.isPresent() ? ias34Cp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<Ias34Cp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<Ias34Cp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo", "EffectDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo", "EffectDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = ias34CpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = ias34CpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = ias34CpReposHist.findAll(pageable);
    else 
      slice = ias34CpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Ias34Cp holdById(Ias34CpId ias34CpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + ias34CpId);
    Optional<Ias34Cp> ias34Cp = null;
    if (dbName.equals(ContentName.onDay))
      ias34Cp = ias34CpReposDay.findByIas34CpId(ias34CpId);
    else if (dbName.equals(ContentName.onMon))
      ias34Cp = ias34CpReposMon.findByIas34CpId(ias34CpId);
    else if (dbName.equals(ContentName.onHist))
      ias34Cp = ias34CpReposHist.findByIas34CpId(ias34CpId);
    else 
      ias34Cp = ias34CpRepos.findByIas34CpId(ias34CpId);
    return ias34Cp.isPresent() ? ias34Cp.get() : null;
  }

  @Override
  public Ias34Cp holdById(Ias34Cp ias34Cp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + ias34Cp.getIas34CpId());
    Optional<Ias34Cp> ias34CpT = null;
    if (dbName.equals(ContentName.onDay))
      ias34CpT = ias34CpReposDay.findByIas34CpId(ias34Cp.getIas34CpId());
    else if (dbName.equals(ContentName.onMon))
      ias34CpT = ias34CpReposMon.findByIas34CpId(ias34Cp.getIas34CpId());
    else if (dbName.equals(ContentName.onHist))
      ias34CpT = ias34CpReposHist.findByIas34CpId(ias34Cp.getIas34CpId());
    else 
      ias34CpT = ias34CpRepos.findByIas34CpId(ias34Cp.getIas34CpId());
    return ias34CpT.isPresent() ? ias34CpT.get() : null;
  }

  @Override
  public Ias34Cp insert(Ias34Cp ias34Cp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + ias34Cp.getIas34CpId());
    if (this.findById(ias34Cp.getIas34CpId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      ias34Cp.setCreateEmpNo(empNot);

    if(ias34Cp.getLastUpdateEmpNo() == null || ias34Cp.getLastUpdateEmpNo().isEmpty())
      ias34Cp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return ias34CpReposDay.saveAndFlush(ias34Cp);	
    else if (dbName.equals(ContentName.onMon))
      return ias34CpReposMon.saveAndFlush(ias34Cp);
    else if (dbName.equals(ContentName.onHist))
      return ias34CpReposHist.saveAndFlush(ias34Cp);
    else 
    return ias34CpRepos.saveAndFlush(ias34Cp);
  }

  @Override
  public Ias34Cp update(Ias34Cp ias34Cp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + ias34Cp.getIas34CpId());
    if (!empNot.isEmpty())
      ias34Cp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return ias34CpReposDay.saveAndFlush(ias34Cp);	
    else if (dbName.equals(ContentName.onMon))
      return ias34CpReposMon.saveAndFlush(ias34Cp);
    else if (dbName.equals(ContentName.onHist))
      return ias34CpReposHist.saveAndFlush(ias34Cp);
    else 
    return ias34CpRepos.saveAndFlush(ias34Cp);
  }

  @Override
  public Ias34Cp update2(Ias34Cp ias34Cp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + ias34Cp.getIas34CpId());
    if (!empNot.isEmpty())
      ias34Cp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      ias34CpReposDay.saveAndFlush(ias34Cp);	
    else if (dbName.equals(ContentName.onMon))
      ias34CpReposMon.saveAndFlush(ias34Cp);
    else if (dbName.equals(ContentName.onHist))
        ias34CpReposHist.saveAndFlush(ias34Cp);
    else 
      ias34CpRepos.saveAndFlush(ias34Cp);	
    return this.findById(ias34Cp.getIas34CpId());
  }

  @Override
  public void delete(Ias34Cp ias34Cp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + ias34Cp.getIas34CpId());
    if (dbName.equals(ContentName.onDay)) {
      ias34CpReposDay.delete(ias34Cp);	
      ias34CpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias34CpReposMon.delete(ias34Cp);	
      ias34CpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias34CpReposHist.delete(ias34Cp);
      ias34CpReposHist.flush();
    }
    else {
      ias34CpRepos.delete(ias34Cp);
      ias34CpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<Ias34Cp> ias34Cp, TitaVo... titaVo) throws DBException {
    if (ias34Cp == null || ias34Cp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (Ias34Cp t : ias34Cp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      ias34Cp = ias34CpReposDay.saveAll(ias34Cp);	
      ias34CpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias34Cp = ias34CpReposMon.saveAll(ias34Cp);	
      ias34CpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias34Cp = ias34CpReposHist.saveAll(ias34Cp);
      ias34CpReposHist.flush();
    }
    else {
      ias34Cp = ias34CpRepos.saveAll(ias34Cp);
      ias34CpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<Ias34Cp> ias34Cp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (ias34Cp == null || ias34Cp.size() == 0)
      throw new DBException(6);

    for (Ias34Cp t : ias34Cp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      ias34Cp = ias34CpReposDay.saveAll(ias34Cp);	
      ias34CpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias34Cp = ias34CpReposMon.saveAll(ias34Cp);	
      ias34CpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias34Cp = ias34CpReposHist.saveAll(ias34Cp);
      ias34CpReposHist.flush();
    }
    else {
      ias34Cp = ias34CpRepos.saveAll(ias34Cp);
      ias34CpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<Ias34Cp> ias34Cp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (ias34Cp == null || ias34Cp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      ias34CpReposDay.deleteAll(ias34Cp);	
      ias34CpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias34CpReposMon.deleteAll(ias34Cp);	
      ias34CpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias34CpReposHist.deleteAll(ias34Cp);
      ias34CpReposHist.flush();
    }
    else {
      ias34CpRepos.deleteAll(ias34Cp);
      ias34CpRepos.flush();
    }
  }

  @Override
  public void Usp_L7_Ias34Cp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      ias34CpReposDay.uspL7Ias34cpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      ias34CpReposMon.uspL7Ias34cpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      ias34CpReposHist.uspL7Ias34cpUpd(TBSDYF, EmpNo);
   else
      ias34CpRepos.uspL7Ias34cpUpd(TBSDYF, EmpNo);
  }

}
