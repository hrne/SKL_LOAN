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
import com.st1.itx.db.domain.AcCheque;
import com.st1.itx.db.domain.AcChequeId;
import com.st1.itx.db.repository.online.AcChequeRepository;
import com.st1.itx.db.repository.day.AcChequeRepositoryDay;
import com.st1.itx.db.repository.mon.AcChequeRepositoryMon;
import com.st1.itx.db.repository.hist.AcChequeRepositoryHist;
import com.st1.itx.db.service.AcChequeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("acChequeService")
@Repository
public class AcChequeServiceImpl extends ASpringJpaParm implements AcChequeService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private AcChequeRepository acChequeRepos;

  @Autowired
  private AcChequeRepositoryDay acChequeReposDay;

  @Autowired
  private AcChequeRepositoryMon acChequeReposMon;

  @Autowired
  private AcChequeRepositoryHist acChequeReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(acChequeRepos);
    org.junit.Assert.assertNotNull(acChequeReposDay);
    org.junit.Assert.assertNotNull(acChequeReposMon);
    org.junit.Assert.assertNotNull(acChequeReposHist);
  }

  @Override
  public AcCheque findById(AcChequeId acChequeId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + acChequeId);
    Optional<AcCheque> acCheque = null;
    if (dbName.equals(ContentName.onDay))
      acCheque = acChequeReposDay.findById(acChequeId);
    else if (dbName.equals(ContentName.onMon))
      acCheque = acChequeReposMon.findById(acChequeId);
    else if (dbName.equals(ContentName.onHist))
      acCheque = acChequeReposHist.findById(acChequeId);
    else 
      acCheque = acChequeRepos.findById(acChequeId);
    AcCheque obj = acCheque.isPresent() ? acCheque.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<AcCheque> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcCheque> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataDate", "UnitCode", "ChequeSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataDate", "UnitCode", "ChequeSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = acChequeReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acChequeReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acChequeReposHist.findAll(pageable);
    else 
      slice = acChequeRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AcCheque holdById(AcChequeId acChequeId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + acChequeId);
    Optional<AcCheque> acCheque = null;
    if (dbName.equals(ContentName.onDay))
      acCheque = acChequeReposDay.findByAcChequeId(acChequeId);
    else if (dbName.equals(ContentName.onMon))
      acCheque = acChequeReposMon.findByAcChequeId(acChequeId);
    else if (dbName.equals(ContentName.onHist))
      acCheque = acChequeReposHist.findByAcChequeId(acChequeId);
    else 
      acCheque = acChequeRepos.findByAcChequeId(acChequeId);
    return acCheque.isPresent() ? acCheque.get() : null;
  }

  @Override
  public AcCheque holdById(AcCheque acCheque, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + acCheque.getAcChequeId());
    Optional<AcCheque> acChequeT = null;
    if (dbName.equals(ContentName.onDay))
      acChequeT = acChequeReposDay.findByAcChequeId(acCheque.getAcChequeId());
    else if (dbName.equals(ContentName.onMon))
      acChequeT = acChequeReposMon.findByAcChequeId(acCheque.getAcChequeId());
    else if (dbName.equals(ContentName.onHist))
      acChequeT = acChequeReposHist.findByAcChequeId(acCheque.getAcChequeId());
    else 
      acChequeT = acChequeRepos.findByAcChequeId(acCheque.getAcChequeId());
    return acChequeT.isPresent() ? acChequeT.get() : null;
  }

  @Override
  public AcCheque insert(AcCheque acCheque, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + acCheque.getAcChequeId());
    if (this.findById(acCheque.getAcChequeId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      acCheque.setCreateEmpNo(empNot);

    if(acCheque.getLastUpdateEmpNo() == null || acCheque.getLastUpdateEmpNo().isEmpty())
      acCheque.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return acChequeReposDay.saveAndFlush(acCheque);	
    else if (dbName.equals(ContentName.onMon))
      return acChequeReposMon.saveAndFlush(acCheque);
    else if (dbName.equals(ContentName.onHist))
      return acChequeReposHist.saveAndFlush(acCheque);
    else 
    return acChequeRepos.saveAndFlush(acCheque);
  }

  @Override
  public AcCheque update(AcCheque acCheque, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + acCheque.getAcChequeId());
    if (!empNot.isEmpty())
      acCheque.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return acChequeReposDay.saveAndFlush(acCheque);	
    else if (dbName.equals(ContentName.onMon))
      return acChequeReposMon.saveAndFlush(acCheque);
    else if (dbName.equals(ContentName.onHist))
      return acChequeReposHist.saveAndFlush(acCheque);
    else 
    return acChequeRepos.saveAndFlush(acCheque);
  }

  @Override
  public AcCheque update2(AcCheque acCheque, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + acCheque.getAcChequeId());
    if (!empNot.isEmpty())
      acCheque.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      acChequeReposDay.saveAndFlush(acCheque);	
    else if (dbName.equals(ContentName.onMon))
      acChequeReposMon.saveAndFlush(acCheque);
    else if (dbName.equals(ContentName.onHist))
        acChequeReposHist.saveAndFlush(acCheque);
    else 
      acChequeRepos.saveAndFlush(acCheque);	
    return this.findById(acCheque.getAcChequeId());
  }

  @Override
  public void delete(AcCheque acCheque, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + acCheque.getAcChequeId());
    if (dbName.equals(ContentName.onDay)) {
      acChequeReposDay.delete(acCheque);	
      acChequeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acChequeReposMon.delete(acCheque);	
      acChequeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acChequeReposHist.delete(acCheque);
      acChequeReposHist.flush();
    }
    else {
      acChequeRepos.delete(acCheque);
      acChequeRepos.flush();
    }
   }

  @Override
  public void insertAll(List<AcCheque> acCheque, TitaVo... titaVo) throws DBException {
    if (acCheque == null || acCheque.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (AcCheque t : acCheque){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      acCheque = acChequeReposDay.saveAll(acCheque);	
      acChequeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acCheque = acChequeReposMon.saveAll(acCheque);	
      acChequeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acCheque = acChequeReposHist.saveAll(acCheque);
      acChequeReposHist.flush();
    }
    else {
      acCheque = acChequeRepos.saveAll(acCheque);
      acChequeRepos.flush();
    }
    }

  @Override
  public void updateAll(List<AcCheque> acCheque, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (acCheque == null || acCheque.size() == 0)
      throw new DBException(6);

    for (AcCheque t : acCheque) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      acCheque = acChequeReposDay.saveAll(acCheque);	
      acChequeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acCheque = acChequeReposMon.saveAll(acCheque);	
      acChequeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acCheque = acChequeReposHist.saveAll(acCheque);
      acChequeReposHist.flush();
    }
    else {
      acCheque = acChequeRepos.saveAll(acCheque);
      acChequeRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<AcCheque> acCheque, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (acCheque == null || acCheque.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      acChequeReposDay.deleteAll(acCheque);	
      acChequeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acChequeReposMon.deleteAll(acCheque);	
      acChequeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acChequeReposHist.deleteAll(acCheque);
      acChequeReposHist.flush();
    }
    else {
      acChequeRepos.deleteAll(acCheque);
      acChequeRepos.flush();
    }
  }

}
