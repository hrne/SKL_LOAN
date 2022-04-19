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
import com.st1.itx.db.domain.FinReportProfit;
import com.st1.itx.db.domain.FinReportProfitId;
import com.st1.itx.db.repository.online.FinReportProfitRepository;
import com.st1.itx.db.repository.day.FinReportProfitRepositoryDay;
import com.st1.itx.db.repository.mon.FinReportProfitRepositoryMon;
import com.st1.itx.db.repository.hist.FinReportProfitRepositoryHist;
import com.st1.itx.db.service.FinReportProfitService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("finReportProfitService")
@Repository
public class FinReportProfitServiceImpl extends ASpringJpaParm implements FinReportProfitService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private FinReportProfitRepository finReportProfitRepos;

  @Autowired
  private FinReportProfitRepositoryDay finReportProfitReposDay;

  @Autowired
  private FinReportProfitRepositoryMon finReportProfitReposMon;

  @Autowired
  private FinReportProfitRepositoryHist finReportProfitReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(finReportProfitRepos);
    org.junit.Assert.assertNotNull(finReportProfitReposDay);
    org.junit.Assert.assertNotNull(finReportProfitReposMon);
    org.junit.Assert.assertNotNull(finReportProfitReposHist);
  }

  @Override
  public FinReportProfit findById(FinReportProfitId finReportProfitId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + finReportProfitId);
    Optional<FinReportProfit> finReportProfit = null;
    if (dbName.equals(ContentName.onDay))
      finReportProfit = finReportProfitReposDay.findById(finReportProfitId);
    else if (dbName.equals(ContentName.onMon))
      finReportProfit = finReportProfitReposMon.findById(finReportProfitId);
    else if (dbName.equals(ContentName.onHist))
      finReportProfit = finReportProfitReposHist.findById(finReportProfitId);
    else 
      finReportProfit = finReportProfitRepos.findById(finReportProfitId);
    FinReportProfit obj = finReportProfit.isPresent() ? finReportProfit.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<FinReportProfit> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FinReportProfit> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustUKey", "Ukey"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustUKey", "Ukey"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = finReportProfitReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = finReportProfitReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = finReportProfitReposHist.findAll(pageable);
    else 
      slice = finReportProfitRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public FinReportProfit findByUkeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findByUkeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<FinReportProfit> finReportProfitT = null;
    if (dbName.equals(ContentName.onDay))
      finReportProfitT = finReportProfitReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      finReportProfitT = finReportProfitReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      finReportProfitT = finReportProfitReposHist.findTopByUkeyIs(ukey_0);
    else 
      finReportProfitT = finReportProfitRepos.findTopByUkeyIs(ukey_0);

    return finReportProfitT.isPresent() ? finReportProfitT.get() : null;
  }

  @Override
  public FinReportProfit holdById(FinReportProfitId finReportProfitId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + finReportProfitId);
    Optional<FinReportProfit> finReportProfit = null;
    if (dbName.equals(ContentName.onDay))
      finReportProfit = finReportProfitReposDay.findByFinReportProfitId(finReportProfitId);
    else if (dbName.equals(ContentName.onMon))
      finReportProfit = finReportProfitReposMon.findByFinReportProfitId(finReportProfitId);
    else if (dbName.equals(ContentName.onHist))
      finReportProfit = finReportProfitReposHist.findByFinReportProfitId(finReportProfitId);
    else 
      finReportProfit = finReportProfitRepos.findByFinReportProfitId(finReportProfitId);
    return finReportProfit.isPresent() ? finReportProfit.get() : null;
  }

  @Override
  public FinReportProfit holdById(FinReportProfit finReportProfit, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + finReportProfit.getFinReportProfitId());
    Optional<FinReportProfit> finReportProfitT = null;
    if (dbName.equals(ContentName.onDay))
      finReportProfitT = finReportProfitReposDay.findByFinReportProfitId(finReportProfit.getFinReportProfitId());
    else if (dbName.equals(ContentName.onMon))
      finReportProfitT = finReportProfitReposMon.findByFinReportProfitId(finReportProfit.getFinReportProfitId());
    else if (dbName.equals(ContentName.onHist))
      finReportProfitT = finReportProfitReposHist.findByFinReportProfitId(finReportProfit.getFinReportProfitId());
    else 
      finReportProfitT = finReportProfitRepos.findByFinReportProfitId(finReportProfit.getFinReportProfitId());
    return finReportProfitT.isPresent() ? finReportProfitT.get() : null;
  }

  @Override
  public FinReportProfit insert(FinReportProfit finReportProfit, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + finReportProfit.getFinReportProfitId());
    if (this.findById(finReportProfit.getFinReportProfitId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      finReportProfit.setCreateEmpNo(empNot);

    if(finReportProfit.getLastUpdateEmpNo() == null || finReportProfit.getLastUpdateEmpNo().isEmpty())
      finReportProfit.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return finReportProfitReposDay.saveAndFlush(finReportProfit);	
    else if (dbName.equals(ContentName.onMon))
      return finReportProfitReposMon.saveAndFlush(finReportProfit);
    else if (dbName.equals(ContentName.onHist))
      return finReportProfitReposHist.saveAndFlush(finReportProfit);
    else 
    return finReportProfitRepos.saveAndFlush(finReportProfit);
  }

  @Override
  public FinReportProfit update(FinReportProfit finReportProfit, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + finReportProfit.getFinReportProfitId());
    if (!empNot.isEmpty())
      finReportProfit.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return finReportProfitReposDay.saveAndFlush(finReportProfit);	
    else if (dbName.equals(ContentName.onMon))
      return finReportProfitReposMon.saveAndFlush(finReportProfit);
    else if (dbName.equals(ContentName.onHist))
      return finReportProfitReposHist.saveAndFlush(finReportProfit);
    else 
    return finReportProfitRepos.saveAndFlush(finReportProfit);
  }

  @Override
  public FinReportProfit update2(FinReportProfit finReportProfit, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + finReportProfit.getFinReportProfitId());
    if (!empNot.isEmpty())
      finReportProfit.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      finReportProfitReposDay.saveAndFlush(finReportProfit);	
    else if (dbName.equals(ContentName.onMon))
      finReportProfitReposMon.saveAndFlush(finReportProfit);
    else if (dbName.equals(ContentName.onHist))
        finReportProfitReposHist.saveAndFlush(finReportProfit);
    else 
      finReportProfitRepos.saveAndFlush(finReportProfit);	
    return this.findById(finReportProfit.getFinReportProfitId());
  }

  @Override
  public void delete(FinReportProfit finReportProfit, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + finReportProfit.getFinReportProfitId());
    if (dbName.equals(ContentName.onDay)) {
      finReportProfitReposDay.delete(finReportProfit);	
      finReportProfitReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportProfitReposMon.delete(finReportProfit);	
      finReportProfitReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportProfitReposHist.delete(finReportProfit);
      finReportProfitReposHist.flush();
    }
    else {
      finReportProfitRepos.delete(finReportProfit);
      finReportProfitRepos.flush();
    }
   }

  @Override
  public void insertAll(List<FinReportProfit> finReportProfit, TitaVo... titaVo) throws DBException {
    if (finReportProfit == null || finReportProfit.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (FinReportProfit t : finReportProfit){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      finReportProfit = finReportProfitReposDay.saveAll(finReportProfit);	
      finReportProfitReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportProfit = finReportProfitReposMon.saveAll(finReportProfit);	
      finReportProfitReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportProfit = finReportProfitReposHist.saveAll(finReportProfit);
      finReportProfitReposHist.flush();
    }
    else {
      finReportProfit = finReportProfitRepos.saveAll(finReportProfit);
      finReportProfitRepos.flush();
    }
    }

  @Override
  public void updateAll(List<FinReportProfit> finReportProfit, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (finReportProfit == null || finReportProfit.size() == 0)
      throw new DBException(6);

    for (FinReportProfit t : finReportProfit) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      finReportProfit = finReportProfitReposDay.saveAll(finReportProfit);	
      finReportProfitReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportProfit = finReportProfitReposMon.saveAll(finReportProfit);	
      finReportProfitReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportProfit = finReportProfitReposHist.saveAll(finReportProfit);
      finReportProfitReposHist.flush();
    }
    else {
      finReportProfit = finReportProfitRepos.saveAll(finReportProfit);
      finReportProfitRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<FinReportProfit> finReportProfit, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (finReportProfit == null || finReportProfit.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      finReportProfitReposDay.deleteAll(finReportProfit);	
      finReportProfitReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportProfitReposMon.deleteAll(finReportProfit);	
      finReportProfitReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportProfitReposHist.deleteAll(finReportProfit);
      finReportProfitReposHist.flush();
    }
    else {
      finReportProfitRepos.deleteAll(finReportProfit);
      finReportProfitRepos.flush();
    }
  }

}
