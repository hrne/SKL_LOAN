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
import com.st1.itx.db.domain.FinReportQuality;
import com.st1.itx.db.domain.FinReportQualityId;
import com.st1.itx.db.repository.online.FinReportQualityRepository;
import com.st1.itx.db.repository.day.FinReportQualityRepositoryDay;
import com.st1.itx.db.repository.mon.FinReportQualityRepositoryMon;
import com.st1.itx.db.repository.hist.FinReportQualityRepositoryHist;
import com.st1.itx.db.service.FinReportQualityService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("finReportQualityService")
@Repository
public class FinReportQualityServiceImpl extends ASpringJpaParm implements FinReportQualityService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private FinReportQualityRepository finReportQualityRepos;

  @Autowired
  private FinReportQualityRepositoryDay finReportQualityReposDay;

  @Autowired
  private FinReportQualityRepositoryMon finReportQualityReposMon;

  @Autowired
  private FinReportQualityRepositoryHist finReportQualityReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(finReportQualityRepos);
    org.junit.Assert.assertNotNull(finReportQualityReposDay);
    org.junit.Assert.assertNotNull(finReportQualityReposMon);
    org.junit.Assert.assertNotNull(finReportQualityReposHist);
  }

  @Override
  public FinReportQuality findById(FinReportQualityId finReportQualityId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + finReportQualityId);
    Optional<FinReportQuality> finReportQuality = null;
    if (dbName.equals(ContentName.onDay))
      finReportQuality = finReportQualityReposDay.findById(finReportQualityId);
    else if (dbName.equals(ContentName.onMon))
      finReportQuality = finReportQualityReposMon.findById(finReportQualityId);
    else if (dbName.equals(ContentName.onHist))
      finReportQuality = finReportQualityReposHist.findById(finReportQualityId);
    else 
      finReportQuality = finReportQualityRepos.findById(finReportQualityId);
    FinReportQuality obj = finReportQuality.isPresent() ? finReportQuality.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<FinReportQuality> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FinReportQuality> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustUKey", "Ukey"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustUKey", "Ukey"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = finReportQualityReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = finReportQualityReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = finReportQualityReposHist.findAll(pageable);
    else 
      slice = finReportQualityRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<FinReportQuality> findCustUKey(String custUKey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FinReportQuality> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCustUKey " + dbName + " : " + "custUKey_0 : " + custUKey_0);
    if (dbName.equals(ContentName.onDay))
      slice = finReportQualityReposDay.findAllByCustUKeyIs(custUKey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = finReportQualityReposMon.findAllByCustUKeyIs(custUKey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = finReportQualityReposHist.findAllByCustUKeyIs(custUKey_0, pageable);
    else 
      slice = finReportQualityRepos.findAllByCustUKeyIs(custUKey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public FinReportQuality holdById(FinReportQualityId finReportQualityId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + finReportQualityId);
    Optional<FinReportQuality> finReportQuality = null;
    if (dbName.equals(ContentName.onDay))
      finReportQuality = finReportQualityReposDay.findByFinReportQualityId(finReportQualityId);
    else if (dbName.equals(ContentName.onMon))
      finReportQuality = finReportQualityReposMon.findByFinReportQualityId(finReportQualityId);
    else if (dbName.equals(ContentName.onHist))
      finReportQuality = finReportQualityReposHist.findByFinReportQualityId(finReportQualityId);
    else 
      finReportQuality = finReportQualityRepos.findByFinReportQualityId(finReportQualityId);
    return finReportQuality.isPresent() ? finReportQuality.get() : null;
  }

  @Override
  public FinReportQuality holdById(FinReportQuality finReportQuality, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + finReportQuality.getFinReportQualityId());
    Optional<FinReportQuality> finReportQualityT = null;
    if (dbName.equals(ContentName.onDay))
      finReportQualityT = finReportQualityReposDay.findByFinReportQualityId(finReportQuality.getFinReportQualityId());
    else if (dbName.equals(ContentName.onMon))
      finReportQualityT = finReportQualityReposMon.findByFinReportQualityId(finReportQuality.getFinReportQualityId());
    else if (dbName.equals(ContentName.onHist))
      finReportQualityT = finReportQualityReposHist.findByFinReportQualityId(finReportQuality.getFinReportQualityId());
    else 
      finReportQualityT = finReportQualityRepos.findByFinReportQualityId(finReportQuality.getFinReportQualityId());
    return finReportQualityT.isPresent() ? finReportQualityT.get() : null;
  }

  @Override
  public FinReportQuality insert(FinReportQuality finReportQuality, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + finReportQuality.getFinReportQualityId());
    if (this.findById(finReportQuality.getFinReportQualityId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      finReportQuality.setCreateEmpNo(empNot);

    if(finReportQuality.getLastUpdateEmpNo() == null || finReportQuality.getLastUpdateEmpNo().isEmpty())
      finReportQuality.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return finReportQualityReposDay.saveAndFlush(finReportQuality);	
    else if (dbName.equals(ContentName.onMon))
      return finReportQualityReposMon.saveAndFlush(finReportQuality);
    else if (dbName.equals(ContentName.onHist))
      return finReportQualityReposHist.saveAndFlush(finReportQuality);
    else 
    return finReportQualityRepos.saveAndFlush(finReportQuality);
  }

  @Override
  public FinReportQuality update(FinReportQuality finReportQuality, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + finReportQuality.getFinReportQualityId());
    if (!empNot.isEmpty())
      finReportQuality.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return finReportQualityReposDay.saveAndFlush(finReportQuality);	
    else if (dbName.equals(ContentName.onMon))
      return finReportQualityReposMon.saveAndFlush(finReportQuality);
    else if (dbName.equals(ContentName.onHist))
      return finReportQualityReposHist.saveAndFlush(finReportQuality);
    else 
    return finReportQualityRepos.saveAndFlush(finReportQuality);
  }

  @Override
  public FinReportQuality update2(FinReportQuality finReportQuality, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + finReportQuality.getFinReportQualityId());
    if (!empNot.isEmpty())
      finReportQuality.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      finReportQualityReposDay.saveAndFlush(finReportQuality);	
    else if (dbName.equals(ContentName.onMon))
      finReportQualityReposMon.saveAndFlush(finReportQuality);
    else if (dbName.equals(ContentName.onHist))
        finReportQualityReposHist.saveAndFlush(finReportQuality);
    else 
      finReportQualityRepos.saveAndFlush(finReportQuality);	
    return this.findById(finReportQuality.getFinReportQualityId());
  }

  @Override
  public void delete(FinReportQuality finReportQuality, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + finReportQuality.getFinReportQualityId());
    if (dbName.equals(ContentName.onDay)) {
      finReportQualityReposDay.delete(finReportQuality);	
      finReportQualityReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportQualityReposMon.delete(finReportQuality);	
      finReportQualityReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportQualityReposHist.delete(finReportQuality);
      finReportQualityReposHist.flush();
    }
    else {
      finReportQualityRepos.delete(finReportQuality);
      finReportQualityRepos.flush();
    }
   }

  @Override
  public void insertAll(List<FinReportQuality> finReportQuality, TitaVo... titaVo) throws DBException {
    if (finReportQuality == null || finReportQuality.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (FinReportQuality t : finReportQuality){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      finReportQuality = finReportQualityReposDay.saveAll(finReportQuality);	
      finReportQualityReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportQuality = finReportQualityReposMon.saveAll(finReportQuality);	
      finReportQualityReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportQuality = finReportQualityReposHist.saveAll(finReportQuality);
      finReportQualityReposHist.flush();
    }
    else {
      finReportQuality = finReportQualityRepos.saveAll(finReportQuality);
      finReportQualityRepos.flush();
    }
    }

  @Override
  public void updateAll(List<FinReportQuality> finReportQuality, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (finReportQuality == null || finReportQuality.size() == 0)
      throw new DBException(6);

    for (FinReportQuality t : finReportQuality) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      finReportQuality = finReportQualityReposDay.saveAll(finReportQuality);	
      finReportQualityReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportQuality = finReportQualityReposMon.saveAll(finReportQuality);	
      finReportQualityReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportQuality = finReportQualityReposHist.saveAll(finReportQuality);
      finReportQualityReposHist.flush();
    }
    else {
      finReportQuality = finReportQualityRepos.saveAll(finReportQuality);
      finReportQualityRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<FinReportQuality> finReportQuality, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (finReportQuality == null || finReportQuality.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      finReportQualityReposDay.deleteAll(finReportQuality);	
      finReportQualityReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportQualityReposMon.deleteAll(finReportQuality);	
      finReportQualityReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportQualityReposHist.deleteAll(finReportQuality);
      finReportQualityReposHist.flush();
    }
    else {
      finReportQualityRepos.deleteAll(finReportQuality);
      finReportQualityRepos.flush();
    }
  }

}
