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
import com.st1.itx.db.domain.Ias34Ep;
import com.st1.itx.db.domain.Ias34EpId;
import com.st1.itx.db.repository.online.Ias34EpRepository;
import com.st1.itx.db.repository.day.Ias34EpRepositoryDay;
import com.st1.itx.db.repository.mon.Ias34EpRepositoryMon;
import com.st1.itx.db.repository.hist.Ias34EpRepositoryHist;
import com.st1.itx.db.service.Ias34EpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("ias34EpService")
@Repository
public class Ias34EpServiceImpl extends ASpringJpaParm implements Ias34EpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private Ias34EpRepository ias34EpRepos;

  @Autowired
  private Ias34EpRepositoryDay ias34EpReposDay;

  @Autowired
  private Ias34EpRepositoryMon ias34EpReposMon;

  @Autowired
  private Ias34EpRepositoryHist ias34EpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(ias34EpRepos);
    org.junit.Assert.assertNotNull(ias34EpReposDay);
    org.junit.Assert.assertNotNull(ias34EpReposMon);
    org.junit.Assert.assertNotNull(ias34EpReposHist);
  }

  @Override
  public Ias34Ep findById(Ias34EpId ias34EpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + ias34EpId);
    Optional<Ias34Ep> ias34Ep = null;
    if (dbName.equals(ContentName.onDay))
      ias34Ep = ias34EpReposDay.findById(ias34EpId);
    else if (dbName.equals(ContentName.onMon))
      ias34Ep = ias34EpReposMon.findById(ias34EpId);
    else if (dbName.equals(ContentName.onHist))
      ias34Ep = ias34EpReposHist.findById(ias34EpId);
    else 
      ias34Ep = ias34EpRepos.findById(ias34EpId);
    Ias34Ep obj = ias34Ep.isPresent() ? ias34Ep.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<Ias34Ep> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<Ias34Ep> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo", "AcCode", "Status"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo", "AcCode", "Status"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = ias34EpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = ias34EpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = ias34EpReposHist.findAll(pageable);
    else 
      slice = ias34EpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Ias34Ep holdById(Ias34EpId ias34EpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + ias34EpId);
    Optional<Ias34Ep> ias34Ep = null;
    if (dbName.equals(ContentName.onDay))
      ias34Ep = ias34EpReposDay.findByIas34EpId(ias34EpId);
    else if (dbName.equals(ContentName.onMon))
      ias34Ep = ias34EpReposMon.findByIas34EpId(ias34EpId);
    else if (dbName.equals(ContentName.onHist))
      ias34Ep = ias34EpReposHist.findByIas34EpId(ias34EpId);
    else 
      ias34Ep = ias34EpRepos.findByIas34EpId(ias34EpId);
    return ias34Ep.isPresent() ? ias34Ep.get() : null;
  }

  @Override
  public Ias34Ep holdById(Ias34Ep ias34Ep, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + ias34Ep.getIas34EpId());
    Optional<Ias34Ep> ias34EpT = null;
    if (dbName.equals(ContentName.onDay))
      ias34EpT = ias34EpReposDay.findByIas34EpId(ias34Ep.getIas34EpId());
    else if (dbName.equals(ContentName.onMon))
      ias34EpT = ias34EpReposMon.findByIas34EpId(ias34Ep.getIas34EpId());
    else if (dbName.equals(ContentName.onHist))
      ias34EpT = ias34EpReposHist.findByIas34EpId(ias34Ep.getIas34EpId());
    else 
      ias34EpT = ias34EpRepos.findByIas34EpId(ias34Ep.getIas34EpId());
    return ias34EpT.isPresent() ? ias34EpT.get() : null;
  }

  @Override
  public Ias34Ep insert(Ias34Ep ias34Ep, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + ias34Ep.getIas34EpId());
    if (this.findById(ias34Ep.getIas34EpId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      ias34Ep.setCreateEmpNo(empNot);

    if(ias34Ep.getLastUpdateEmpNo() == null || ias34Ep.getLastUpdateEmpNo().isEmpty())
      ias34Ep.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return ias34EpReposDay.saveAndFlush(ias34Ep);	
    else if (dbName.equals(ContentName.onMon))
      return ias34EpReposMon.saveAndFlush(ias34Ep);
    else if (dbName.equals(ContentName.onHist))
      return ias34EpReposHist.saveAndFlush(ias34Ep);
    else 
    return ias34EpRepos.saveAndFlush(ias34Ep);
  }

  @Override
  public Ias34Ep update(Ias34Ep ias34Ep, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + ias34Ep.getIas34EpId());
    if (!empNot.isEmpty())
      ias34Ep.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return ias34EpReposDay.saveAndFlush(ias34Ep);	
    else if (dbName.equals(ContentName.onMon))
      return ias34EpReposMon.saveAndFlush(ias34Ep);
    else if (dbName.equals(ContentName.onHist))
      return ias34EpReposHist.saveAndFlush(ias34Ep);
    else 
    return ias34EpRepos.saveAndFlush(ias34Ep);
  }

  @Override
  public Ias34Ep update2(Ias34Ep ias34Ep, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + ias34Ep.getIas34EpId());
    if (!empNot.isEmpty())
      ias34Ep.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      ias34EpReposDay.saveAndFlush(ias34Ep);	
    else if (dbName.equals(ContentName.onMon))
      ias34EpReposMon.saveAndFlush(ias34Ep);
    else if (dbName.equals(ContentName.onHist))
        ias34EpReposHist.saveAndFlush(ias34Ep);
    else 
      ias34EpRepos.saveAndFlush(ias34Ep);	
    return this.findById(ias34Ep.getIas34EpId());
  }

  @Override
  public void delete(Ias34Ep ias34Ep, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + ias34Ep.getIas34EpId());
    if (dbName.equals(ContentName.onDay)) {
      ias34EpReposDay.delete(ias34Ep);	
      ias34EpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias34EpReposMon.delete(ias34Ep);	
      ias34EpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias34EpReposHist.delete(ias34Ep);
      ias34EpReposHist.flush();
    }
    else {
      ias34EpRepos.delete(ias34Ep);
      ias34EpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<Ias34Ep> ias34Ep, TitaVo... titaVo) throws DBException {
    if (ias34Ep == null || ias34Ep.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (Ias34Ep t : ias34Ep){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      ias34Ep = ias34EpReposDay.saveAll(ias34Ep);	
      ias34EpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias34Ep = ias34EpReposMon.saveAll(ias34Ep);	
      ias34EpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias34Ep = ias34EpReposHist.saveAll(ias34Ep);
      ias34EpReposHist.flush();
    }
    else {
      ias34Ep = ias34EpRepos.saveAll(ias34Ep);
      ias34EpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<Ias34Ep> ias34Ep, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (ias34Ep == null || ias34Ep.size() == 0)
      throw new DBException(6);

    for (Ias34Ep t : ias34Ep) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      ias34Ep = ias34EpReposDay.saveAll(ias34Ep);	
      ias34EpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias34Ep = ias34EpReposMon.saveAll(ias34Ep);	
      ias34EpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias34Ep = ias34EpReposHist.saveAll(ias34Ep);
      ias34EpReposHist.flush();
    }
    else {
      ias34Ep = ias34EpRepos.saveAll(ias34Ep);
      ias34EpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<Ias34Ep> ias34Ep, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (ias34Ep == null || ias34Ep.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      ias34EpReposDay.deleteAll(ias34Ep);	
      ias34EpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias34EpReposMon.deleteAll(ias34Ep);	
      ias34EpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias34EpReposHist.deleteAll(ias34Ep);
      ias34EpReposHist.flush();
    }
    else {
      ias34EpRepos.deleteAll(ias34Ep);
      ias34EpRepos.flush();
    }
  }

  @Override
  public void Usp_L7_Ias34Ep_Upd(int TBSDYF, String EmpNo, int NewAcFg, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      ias34EpReposDay.uspL7Ias34epUpd(TBSDYF, EmpNo, NewAcFg);
    else if (dbName.equals(ContentName.onMon))
      ias34EpReposMon.uspL7Ias34epUpd(TBSDYF, EmpNo, NewAcFg);
    else if (dbName.equals(ContentName.onHist))
      ias34EpReposHist.uspL7Ias34epUpd(TBSDYF, EmpNo, NewAcFg);
   else
      ias34EpRepos.uspL7Ias34epUpd(TBSDYF, EmpNo, NewAcFg);
  }

}
