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
import com.st1.itx.db.domain.PfInsCheck;
import com.st1.itx.db.domain.PfInsCheckId;
import com.st1.itx.db.repository.online.PfInsCheckRepository;
import com.st1.itx.db.repository.day.PfInsCheckRepositoryDay;
import com.st1.itx.db.repository.mon.PfInsCheckRepositoryMon;
import com.st1.itx.db.repository.hist.PfInsCheckRepositoryHist;
import com.st1.itx.db.service.PfInsCheckService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("pfInsCheckService")
@Repository
public class PfInsCheckServiceImpl extends ASpringJpaParm implements PfInsCheckService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private PfInsCheckRepository pfInsCheckRepos;

  @Autowired
  private PfInsCheckRepositoryDay pfInsCheckReposDay;

  @Autowired
  private PfInsCheckRepositoryMon pfInsCheckReposMon;

  @Autowired
  private PfInsCheckRepositoryHist pfInsCheckReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(pfInsCheckRepos);
    org.junit.Assert.assertNotNull(pfInsCheckReposDay);
    org.junit.Assert.assertNotNull(pfInsCheckReposMon);
    org.junit.Assert.assertNotNull(pfInsCheckReposHist);
  }

  @Override
  public PfInsCheck findById(PfInsCheckId pfInsCheckId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + pfInsCheckId);
    Optional<PfInsCheck> pfInsCheck = null;
    if (dbName.equals(ContentName.onDay))
      pfInsCheck = pfInsCheckReposDay.findById(pfInsCheckId);
    else if (dbName.equals(ContentName.onMon))
      pfInsCheck = pfInsCheckReposMon.findById(pfInsCheckId);
    else if (dbName.equals(ContentName.onHist))
      pfInsCheck = pfInsCheckReposHist.findById(pfInsCheckId);
    else 
      pfInsCheck = pfInsCheckRepos.findById(pfInsCheckId);
    PfInsCheck obj = pfInsCheck.isPresent() ? pfInsCheck.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<PfInsCheck> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfInsCheck> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Kind", "CustNo", "FacmNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Kind", "CustNo", "FacmNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = pfInsCheckReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfInsCheckReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfInsCheckReposHist.findAll(pageable);
    else 
      slice = pfInsCheckRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfInsCheck> findCheckWorkMonthEq(int checkWorkMonth_0, int kind_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfInsCheck> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCheckWorkMonthEq " + dbName + " : " + "checkWorkMonth_0 : " + checkWorkMonth_0 + " kind_1 : " +  kind_1);
    if (dbName.equals(ContentName.onDay))
      slice = pfInsCheckReposDay.findAllByCheckWorkMonthIsAndKindIsOrderByCustNoAscFacmNoAsc(checkWorkMonth_0, kind_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfInsCheckReposMon.findAllByCheckWorkMonthIsAndKindIsOrderByCustNoAscFacmNoAsc(checkWorkMonth_0, kind_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfInsCheckReposHist.findAllByCheckWorkMonthIsAndKindIsOrderByCustNoAscFacmNoAsc(checkWorkMonth_0, kind_1, pageable);
    else 
      slice = pfInsCheckRepos.findAllByCheckWorkMonthIsAndKindIsOrderByCustNoAscFacmNoAsc(checkWorkMonth_0, kind_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PfInsCheck holdById(PfInsCheckId pfInsCheckId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + pfInsCheckId);
    Optional<PfInsCheck> pfInsCheck = null;
    if (dbName.equals(ContentName.onDay))
      pfInsCheck = pfInsCheckReposDay.findByPfInsCheckId(pfInsCheckId);
    else if (dbName.equals(ContentName.onMon))
      pfInsCheck = pfInsCheckReposMon.findByPfInsCheckId(pfInsCheckId);
    else if (dbName.equals(ContentName.onHist))
      pfInsCheck = pfInsCheckReposHist.findByPfInsCheckId(pfInsCheckId);
    else 
      pfInsCheck = pfInsCheckRepos.findByPfInsCheckId(pfInsCheckId);
    return pfInsCheck.isPresent() ? pfInsCheck.get() : null;
  }

  @Override
  public PfInsCheck holdById(PfInsCheck pfInsCheck, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + pfInsCheck.getPfInsCheckId());
    Optional<PfInsCheck> pfInsCheckT = null;
    if (dbName.equals(ContentName.onDay))
      pfInsCheckT = pfInsCheckReposDay.findByPfInsCheckId(pfInsCheck.getPfInsCheckId());
    else if (dbName.equals(ContentName.onMon))
      pfInsCheckT = pfInsCheckReposMon.findByPfInsCheckId(pfInsCheck.getPfInsCheckId());
    else if (dbName.equals(ContentName.onHist))
      pfInsCheckT = pfInsCheckReposHist.findByPfInsCheckId(pfInsCheck.getPfInsCheckId());
    else 
      pfInsCheckT = pfInsCheckRepos.findByPfInsCheckId(pfInsCheck.getPfInsCheckId());
    return pfInsCheckT.isPresent() ? pfInsCheckT.get() : null;
  }

  @Override
  public PfInsCheck insert(PfInsCheck pfInsCheck, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + pfInsCheck.getPfInsCheckId());
    if (this.findById(pfInsCheck.getPfInsCheckId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      pfInsCheck.setCreateEmpNo(empNot);

    if(pfInsCheck.getLastUpdateEmpNo() == null || pfInsCheck.getLastUpdateEmpNo().isEmpty())
      pfInsCheck.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfInsCheckReposDay.saveAndFlush(pfInsCheck);	
    else if (dbName.equals(ContentName.onMon))
      return pfInsCheckReposMon.saveAndFlush(pfInsCheck);
    else if (dbName.equals(ContentName.onHist))
      return pfInsCheckReposHist.saveAndFlush(pfInsCheck);
    else 
    return pfInsCheckRepos.saveAndFlush(pfInsCheck);
  }

  @Override
  public PfInsCheck update(PfInsCheck pfInsCheck, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + pfInsCheck.getPfInsCheckId());
    if (!empNot.isEmpty())
      pfInsCheck.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfInsCheckReposDay.saveAndFlush(pfInsCheck);	
    else if (dbName.equals(ContentName.onMon))
      return pfInsCheckReposMon.saveAndFlush(pfInsCheck);
    else if (dbName.equals(ContentName.onHist))
      return pfInsCheckReposHist.saveAndFlush(pfInsCheck);
    else 
    return pfInsCheckRepos.saveAndFlush(pfInsCheck);
  }

  @Override
  public PfInsCheck update2(PfInsCheck pfInsCheck, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + pfInsCheck.getPfInsCheckId());
    if (!empNot.isEmpty())
      pfInsCheck.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      pfInsCheckReposDay.saveAndFlush(pfInsCheck);	
    else if (dbName.equals(ContentName.onMon))
      pfInsCheckReposMon.saveAndFlush(pfInsCheck);
    else if (dbName.equals(ContentName.onHist))
        pfInsCheckReposHist.saveAndFlush(pfInsCheck);
    else 
      pfInsCheckRepos.saveAndFlush(pfInsCheck);	
    return this.findById(pfInsCheck.getPfInsCheckId());
  }

  @Override
  public void delete(PfInsCheck pfInsCheck, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + pfInsCheck.getPfInsCheckId());
    if (dbName.equals(ContentName.onDay)) {
      pfInsCheckReposDay.delete(pfInsCheck);	
      pfInsCheckReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfInsCheckReposMon.delete(pfInsCheck);	
      pfInsCheckReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfInsCheckReposHist.delete(pfInsCheck);
      pfInsCheckReposHist.flush();
    }
    else {
      pfInsCheckRepos.delete(pfInsCheck);
      pfInsCheckRepos.flush();
    }
   }

  @Override
  public void insertAll(List<PfInsCheck> pfInsCheck, TitaVo... titaVo) throws DBException {
    if (pfInsCheck == null || pfInsCheck.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (PfInsCheck t : pfInsCheck){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      pfInsCheck = pfInsCheckReposDay.saveAll(pfInsCheck);	
      pfInsCheckReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfInsCheck = pfInsCheckReposMon.saveAll(pfInsCheck);	
      pfInsCheckReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfInsCheck = pfInsCheckReposHist.saveAll(pfInsCheck);
      pfInsCheckReposHist.flush();
    }
    else {
      pfInsCheck = pfInsCheckRepos.saveAll(pfInsCheck);
      pfInsCheckRepos.flush();
    }
    }

  @Override
  public void updateAll(List<PfInsCheck> pfInsCheck, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (pfInsCheck == null || pfInsCheck.size() == 0)
      throw new DBException(6);

    for (PfInsCheck t : pfInsCheck) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      pfInsCheck = pfInsCheckReposDay.saveAll(pfInsCheck);	
      pfInsCheckReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfInsCheck = pfInsCheckReposMon.saveAll(pfInsCheck);	
      pfInsCheckReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfInsCheck = pfInsCheckReposHist.saveAll(pfInsCheck);
      pfInsCheckReposHist.flush();
    }
    else {
      pfInsCheck = pfInsCheckRepos.saveAll(pfInsCheck);
      pfInsCheckRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<PfInsCheck> pfInsCheck, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (pfInsCheck == null || pfInsCheck.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      pfInsCheckReposDay.deleteAll(pfInsCheck);	
      pfInsCheckReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfInsCheckReposMon.deleteAll(pfInsCheck);	
      pfInsCheckReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfInsCheckReposHist.deleteAll(pfInsCheck);
      pfInsCheckReposHist.flush();
    }
    else {
      pfInsCheckRepos.deleteAll(pfInsCheck);
      pfInsCheckRepos.flush();
    }
  }

}
