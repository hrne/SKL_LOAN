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
import com.st1.itx.db.domain.FinReportRate;
import com.st1.itx.db.domain.FinReportRateId;
import com.st1.itx.db.repository.online.FinReportRateRepository;
import com.st1.itx.db.repository.day.FinReportRateRepositoryDay;
import com.st1.itx.db.repository.mon.FinReportRateRepositoryMon;
import com.st1.itx.db.repository.hist.FinReportRateRepositoryHist;
import com.st1.itx.db.service.FinReportRateService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("finReportRateService")
@Repository
public class FinReportRateServiceImpl extends ASpringJpaParm implements FinReportRateService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private FinReportRateRepository finReportRateRepos;

  @Autowired
  private FinReportRateRepositoryDay finReportRateReposDay;

  @Autowired
  private FinReportRateRepositoryMon finReportRateReposMon;

  @Autowired
  private FinReportRateRepositoryHist finReportRateReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(finReportRateRepos);
    org.junit.Assert.assertNotNull(finReportRateReposDay);
    org.junit.Assert.assertNotNull(finReportRateReposMon);
    org.junit.Assert.assertNotNull(finReportRateReposHist);
  }

  @Override
  public FinReportRate findById(FinReportRateId finReportRateId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + finReportRateId);
    Optional<FinReportRate> finReportRate = null;
    if (dbName.equals(ContentName.onDay))
      finReportRate = finReportRateReposDay.findById(finReportRateId);
    else if (dbName.equals(ContentName.onMon))
      finReportRate = finReportRateReposMon.findById(finReportRateId);
    else if (dbName.equals(ContentName.onHist))
      finReportRate = finReportRateReposHist.findById(finReportRateId);
    else 
      finReportRate = finReportRateRepos.findById(finReportRateId);
    FinReportRate obj = finReportRate.isPresent() ? finReportRate.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<FinReportRate> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FinReportRate> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustUKey", "UKey"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustUKey", "UKey"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = finReportRateReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = finReportRateReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = finReportRateReposHist.findAll(pageable);
    else 
      slice = finReportRateRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<FinReportRate> findCustUKey(String custUKey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FinReportRate> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCustUKey " + dbName + " : " + "custUKey_0 : " + custUKey_0);
    if (dbName.equals(ContentName.onDay))
      slice = finReportRateReposDay.findAllByCustUKeyIs(custUKey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = finReportRateReposMon.findAllByCustUKeyIs(custUKey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = finReportRateReposHist.findAllByCustUKeyIs(custUKey_0, pageable);
    else 
      slice = finReportRateRepos.findAllByCustUKeyIs(custUKey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public FinReportRate holdById(FinReportRateId finReportRateId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + finReportRateId);
    Optional<FinReportRate> finReportRate = null;
    if (dbName.equals(ContentName.onDay))
      finReportRate = finReportRateReposDay.findByFinReportRateId(finReportRateId);
    else if (dbName.equals(ContentName.onMon))
      finReportRate = finReportRateReposMon.findByFinReportRateId(finReportRateId);
    else if (dbName.equals(ContentName.onHist))
      finReportRate = finReportRateReposHist.findByFinReportRateId(finReportRateId);
    else 
      finReportRate = finReportRateRepos.findByFinReportRateId(finReportRateId);
    return finReportRate.isPresent() ? finReportRate.get() : null;
  }

  @Override
  public FinReportRate holdById(FinReportRate finReportRate, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + finReportRate.getFinReportRateId());
    Optional<FinReportRate> finReportRateT = null;
    if (dbName.equals(ContentName.onDay))
      finReportRateT = finReportRateReposDay.findByFinReportRateId(finReportRate.getFinReportRateId());
    else if (dbName.equals(ContentName.onMon))
      finReportRateT = finReportRateReposMon.findByFinReportRateId(finReportRate.getFinReportRateId());
    else if (dbName.equals(ContentName.onHist))
      finReportRateT = finReportRateReposHist.findByFinReportRateId(finReportRate.getFinReportRateId());
    else 
      finReportRateT = finReportRateRepos.findByFinReportRateId(finReportRate.getFinReportRateId());
    return finReportRateT.isPresent() ? finReportRateT.get() : null;
  }

  @Override
  public FinReportRate insert(FinReportRate finReportRate, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + finReportRate.getFinReportRateId());
    if (this.findById(finReportRate.getFinReportRateId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      finReportRate.setCreateEmpNo(empNot);

    if(finReportRate.getLastUpdateEmpNo() == null || finReportRate.getLastUpdateEmpNo().isEmpty())
      finReportRate.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return finReportRateReposDay.saveAndFlush(finReportRate);	
    else if (dbName.equals(ContentName.onMon))
      return finReportRateReposMon.saveAndFlush(finReportRate);
    else if (dbName.equals(ContentName.onHist))
      return finReportRateReposHist.saveAndFlush(finReportRate);
    else 
    return finReportRateRepos.saveAndFlush(finReportRate);
  }

  @Override
  public FinReportRate update(FinReportRate finReportRate, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + finReportRate.getFinReportRateId());
    if (!empNot.isEmpty())
      finReportRate.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return finReportRateReposDay.saveAndFlush(finReportRate);	
    else if (dbName.equals(ContentName.onMon))
      return finReportRateReposMon.saveAndFlush(finReportRate);
    else if (dbName.equals(ContentName.onHist))
      return finReportRateReposHist.saveAndFlush(finReportRate);
    else 
    return finReportRateRepos.saveAndFlush(finReportRate);
  }

  @Override
  public FinReportRate update2(FinReportRate finReportRate, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + finReportRate.getFinReportRateId());
    if (!empNot.isEmpty())
      finReportRate.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      finReportRateReposDay.saveAndFlush(finReportRate);	
    else if (dbName.equals(ContentName.onMon))
      finReportRateReposMon.saveAndFlush(finReportRate);
    else if (dbName.equals(ContentName.onHist))
        finReportRateReposHist.saveAndFlush(finReportRate);
    else 
      finReportRateRepos.saveAndFlush(finReportRate);	
    return this.findById(finReportRate.getFinReportRateId());
  }

  @Override
  public void delete(FinReportRate finReportRate, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + finReportRate.getFinReportRateId());
    if (dbName.equals(ContentName.onDay)) {
      finReportRateReposDay.delete(finReportRate);	
      finReportRateReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportRateReposMon.delete(finReportRate);	
      finReportRateReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportRateReposHist.delete(finReportRate);
      finReportRateReposHist.flush();
    }
    else {
      finReportRateRepos.delete(finReportRate);
      finReportRateRepos.flush();
    }
   }

  @Override
  public void insertAll(List<FinReportRate> finReportRate, TitaVo... titaVo) throws DBException {
    if (finReportRate == null || finReportRate.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (FinReportRate t : finReportRate){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      finReportRate = finReportRateReposDay.saveAll(finReportRate);	
      finReportRateReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportRate = finReportRateReposMon.saveAll(finReportRate);	
      finReportRateReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportRate = finReportRateReposHist.saveAll(finReportRate);
      finReportRateReposHist.flush();
    }
    else {
      finReportRate = finReportRateRepos.saveAll(finReportRate);
      finReportRateRepos.flush();
    }
    }

  @Override
  public void updateAll(List<FinReportRate> finReportRate, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (finReportRate == null || finReportRate.size() == 0)
      throw new DBException(6);

    for (FinReportRate t : finReportRate) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      finReportRate = finReportRateReposDay.saveAll(finReportRate);	
      finReportRateReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportRate = finReportRateReposMon.saveAll(finReportRate);	
      finReportRateReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportRate = finReportRateReposHist.saveAll(finReportRate);
      finReportRateReposHist.flush();
    }
    else {
      finReportRate = finReportRateRepos.saveAll(finReportRate);
      finReportRateRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<FinReportRate> finReportRate, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (finReportRate == null || finReportRate.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      finReportRateReposDay.deleteAll(finReportRate);	
      finReportRateReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportRateReposMon.deleteAll(finReportRate);	
      finReportRateReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportRateReposHist.deleteAll(finReportRate);
      finReportRateReposHist.flush();
    }
    else {
      finReportRateRepos.deleteAll(finReportRate);
      finReportRateRepos.flush();
    }
  }

}
