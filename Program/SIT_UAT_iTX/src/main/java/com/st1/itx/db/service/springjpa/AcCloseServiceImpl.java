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
import com.st1.itx.db.domain.AcClose;
import com.st1.itx.db.domain.AcCloseId;
import com.st1.itx.db.repository.online.AcCloseRepository;
import com.st1.itx.db.repository.day.AcCloseRepositoryDay;
import com.st1.itx.db.repository.mon.AcCloseRepositoryMon;
import com.st1.itx.db.repository.hist.AcCloseRepositoryHist;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("acCloseService")
@Repository
public class AcCloseServiceImpl extends ASpringJpaParm implements AcCloseService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private AcCloseRepository acCloseRepos;

  @Autowired
  private AcCloseRepositoryDay acCloseReposDay;

  @Autowired
  private AcCloseRepositoryMon acCloseReposMon;

  @Autowired
  private AcCloseRepositoryHist acCloseReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(acCloseRepos);
    org.junit.Assert.assertNotNull(acCloseReposDay);
    org.junit.Assert.assertNotNull(acCloseReposMon);
    org.junit.Assert.assertNotNull(acCloseReposHist);
  }

  @Override
  public AcClose findById(AcCloseId acCloseId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + acCloseId);
    Optional<AcClose> acClose = null;
    if (dbName.equals(ContentName.onDay))
      acClose = acCloseReposDay.findById(acCloseId);
    else if (dbName.equals(ContentName.onMon))
      acClose = acCloseReposMon.findById(acCloseId);
    else if (dbName.equals(ContentName.onHist))
      acClose = acCloseReposHist.findById(acCloseId);
    else 
      acClose = acCloseRepos.findById(acCloseId);
    AcClose obj = acClose.isPresent() ? acClose.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<AcClose> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcClose> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AcDate", "BranchNo", "SecNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcDate", "BranchNo", "SecNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = acCloseReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acCloseReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acCloseReposHist.findAll(pageable);
    else 
      slice = acCloseRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcClose> acCloseBranchNoEq(int acDate_0, String branchNo_1, String secNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcClose> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acCloseBranchNoEq " + dbName + " : " + "acDate_0 : " + acDate_0 + " branchNo_1 : " +  branchNo_1 + " secNo_2 : " +  secNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = acCloseReposDay.findAllByAcDateIsAndBranchNoIsAndSecNoGreaterThanEqualOrderBySecNoDesc(acDate_0, branchNo_1, secNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acCloseReposMon.findAllByAcDateIsAndBranchNoIsAndSecNoGreaterThanEqualOrderBySecNoDesc(acDate_0, branchNo_1, secNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acCloseReposHist.findAllByAcDateIsAndBranchNoIsAndSecNoGreaterThanEqualOrderBySecNoDesc(acDate_0, branchNo_1, secNo_2, pageable);
    else 
      slice = acCloseRepos.findAllByAcDateIsAndBranchNoIsAndSecNoGreaterThanEqualOrderBySecNoDesc(acDate_0, branchNo_1, secNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AcClose holdById(AcCloseId acCloseId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + acCloseId);
    Optional<AcClose> acClose = null;
    if (dbName.equals(ContentName.onDay))
      acClose = acCloseReposDay.findByAcCloseId(acCloseId);
    else if (dbName.equals(ContentName.onMon))
      acClose = acCloseReposMon.findByAcCloseId(acCloseId);
    else if (dbName.equals(ContentName.onHist))
      acClose = acCloseReposHist.findByAcCloseId(acCloseId);
    else 
      acClose = acCloseRepos.findByAcCloseId(acCloseId);
    return acClose.isPresent() ? acClose.get() : null;
  }

  @Override
  public AcClose holdById(AcClose acClose, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + acClose.getAcCloseId());
    Optional<AcClose> acCloseT = null;
    if (dbName.equals(ContentName.onDay))
      acCloseT = acCloseReposDay.findByAcCloseId(acClose.getAcCloseId());
    else if (dbName.equals(ContentName.onMon))
      acCloseT = acCloseReposMon.findByAcCloseId(acClose.getAcCloseId());
    else if (dbName.equals(ContentName.onHist))
      acCloseT = acCloseReposHist.findByAcCloseId(acClose.getAcCloseId());
    else 
      acCloseT = acCloseRepos.findByAcCloseId(acClose.getAcCloseId());
    return acCloseT.isPresent() ? acCloseT.get() : null;
  }

  @Override
  public AcClose insert(AcClose acClose, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + acClose.getAcCloseId());
    if (this.findById(acClose.getAcCloseId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      acClose.setCreateEmpNo(empNot);

    if(acClose.getLastUpdateEmpNo() == null || acClose.getLastUpdateEmpNo().isEmpty())
      acClose.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return acCloseReposDay.saveAndFlush(acClose);	
    else if (dbName.equals(ContentName.onMon))
      return acCloseReposMon.saveAndFlush(acClose);
    else if (dbName.equals(ContentName.onHist))
      return acCloseReposHist.saveAndFlush(acClose);
    else 
    return acCloseRepos.saveAndFlush(acClose);
  }

  @Override
  public AcClose update(AcClose acClose, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + acClose.getAcCloseId());
    if (!empNot.isEmpty())
      acClose.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return acCloseReposDay.saveAndFlush(acClose);	
    else if (dbName.equals(ContentName.onMon))
      return acCloseReposMon.saveAndFlush(acClose);
    else if (dbName.equals(ContentName.onHist))
      return acCloseReposHist.saveAndFlush(acClose);
    else 
    return acCloseRepos.saveAndFlush(acClose);
  }

  @Override
  public AcClose update2(AcClose acClose, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + acClose.getAcCloseId());
    if (!empNot.isEmpty())
      acClose.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      acCloseReposDay.saveAndFlush(acClose);	
    else if (dbName.equals(ContentName.onMon))
      acCloseReposMon.saveAndFlush(acClose);
    else if (dbName.equals(ContentName.onHist))
        acCloseReposHist.saveAndFlush(acClose);
    else 
      acCloseRepos.saveAndFlush(acClose);	
    return this.findById(acClose.getAcCloseId());
  }

  @Override
  public void delete(AcClose acClose, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + acClose.getAcCloseId());
    if (dbName.equals(ContentName.onDay)) {
      acCloseReposDay.delete(acClose);	
      acCloseReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acCloseReposMon.delete(acClose);	
      acCloseReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acCloseReposHist.delete(acClose);
      acCloseReposHist.flush();
    }
    else {
      acCloseRepos.delete(acClose);
      acCloseRepos.flush();
    }
   }

  @Override
  public void insertAll(List<AcClose> acClose, TitaVo... titaVo) throws DBException {
    if (acClose == null || acClose.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (AcClose t : acClose){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      acClose = acCloseReposDay.saveAll(acClose);	
      acCloseReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acClose = acCloseReposMon.saveAll(acClose);	
      acCloseReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acClose = acCloseReposHist.saveAll(acClose);
      acCloseReposHist.flush();
    }
    else {
      acClose = acCloseRepos.saveAll(acClose);
      acCloseRepos.flush();
    }
    }

  @Override
  public void updateAll(List<AcClose> acClose, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (acClose == null || acClose.size() == 0)
      throw new DBException(6);

    for (AcClose t : acClose) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      acClose = acCloseReposDay.saveAll(acClose);	
      acCloseReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acClose = acCloseReposMon.saveAll(acClose);	
      acCloseReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acClose = acCloseReposHist.saveAll(acClose);
      acCloseReposHist.flush();
    }
    else {
      acClose = acCloseRepos.saveAll(acClose);
      acCloseRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<AcClose> acClose, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (acClose == null || acClose.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      acCloseReposDay.deleteAll(acClose);	
      acCloseReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acCloseReposMon.deleteAll(acClose);	
      acCloseReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acCloseReposHist.deleteAll(acClose);
      acCloseReposHist.flush();
    }
    else {
      acCloseRepos.deleteAll(acClose);
      acCloseRepos.flush();
    }
  }

}
