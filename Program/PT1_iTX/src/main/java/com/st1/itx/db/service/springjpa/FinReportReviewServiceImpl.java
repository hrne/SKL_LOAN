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
import com.st1.itx.db.domain.FinReportReview;
import com.st1.itx.db.domain.FinReportReviewId;
import com.st1.itx.db.repository.online.FinReportReviewRepository;
import com.st1.itx.db.repository.day.FinReportReviewRepositoryDay;
import com.st1.itx.db.repository.mon.FinReportReviewRepositoryMon;
import com.st1.itx.db.repository.hist.FinReportReviewRepositoryHist;
import com.st1.itx.db.service.FinReportReviewService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("finReportReviewService")
@Repository
public class FinReportReviewServiceImpl extends ASpringJpaParm implements FinReportReviewService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private FinReportReviewRepository finReportReviewRepos;

  @Autowired
  private FinReportReviewRepositoryDay finReportReviewReposDay;

  @Autowired
  private FinReportReviewRepositoryMon finReportReviewReposMon;

  @Autowired
  private FinReportReviewRepositoryHist finReportReviewReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(finReportReviewRepos);
    org.junit.Assert.assertNotNull(finReportReviewReposDay);
    org.junit.Assert.assertNotNull(finReportReviewReposMon);
    org.junit.Assert.assertNotNull(finReportReviewReposHist);
  }

  @Override
  public FinReportReview findById(FinReportReviewId finReportReviewId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + finReportReviewId);
    Optional<FinReportReview> finReportReview = null;
    if (dbName.equals(ContentName.onDay))
      finReportReview = finReportReviewReposDay.findById(finReportReviewId);
    else if (dbName.equals(ContentName.onMon))
      finReportReview = finReportReviewReposMon.findById(finReportReviewId);
    else if (dbName.equals(ContentName.onHist))
      finReportReview = finReportReviewReposHist.findById(finReportReviewId);
    else 
      finReportReview = finReportReviewRepos.findById(finReportReviewId);
    FinReportReview obj = finReportReview.isPresent() ? finReportReview.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<FinReportReview> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FinReportReview> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustUKey", "Ukey"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustUKey", "Ukey"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = finReportReviewReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = finReportReviewReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = finReportReviewReposHist.findAll(pageable);
    else 
      slice = finReportReviewRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<FinReportReview> findCustUKey(String custUKey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FinReportReview> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCustUKey " + dbName + " : " + "custUKey_0 : " + custUKey_0);
    if (dbName.equals(ContentName.onDay))
      slice = finReportReviewReposDay.findAllByCustUKeyIs(custUKey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = finReportReviewReposMon.findAllByCustUKeyIs(custUKey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = finReportReviewReposHist.findAllByCustUKeyIs(custUKey_0, pageable);
    else 
      slice = finReportReviewRepos.findAllByCustUKeyIs(custUKey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public FinReportReview holdById(FinReportReviewId finReportReviewId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + finReportReviewId);
    Optional<FinReportReview> finReportReview = null;
    if (dbName.equals(ContentName.onDay))
      finReportReview = finReportReviewReposDay.findByFinReportReviewId(finReportReviewId);
    else if (dbName.equals(ContentName.onMon))
      finReportReview = finReportReviewReposMon.findByFinReportReviewId(finReportReviewId);
    else if (dbName.equals(ContentName.onHist))
      finReportReview = finReportReviewReposHist.findByFinReportReviewId(finReportReviewId);
    else 
      finReportReview = finReportReviewRepos.findByFinReportReviewId(finReportReviewId);
    return finReportReview.isPresent() ? finReportReview.get() : null;
  }

  @Override
  public FinReportReview holdById(FinReportReview finReportReview, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + finReportReview.getFinReportReviewId());
    Optional<FinReportReview> finReportReviewT = null;
    if (dbName.equals(ContentName.onDay))
      finReportReviewT = finReportReviewReposDay.findByFinReportReviewId(finReportReview.getFinReportReviewId());
    else if (dbName.equals(ContentName.onMon))
      finReportReviewT = finReportReviewReposMon.findByFinReportReviewId(finReportReview.getFinReportReviewId());
    else if (dbName.equals(ContentName.onHist))
      finReportReviewT = finReportReviewReposHist.findByFinReportReviewId(finReportReview.getFinReportReviewId());
    else 
      finReportReviewT = finReportReviewRepos.findByFinReportReviewId(finReportReview.getFinReportReviewId());
    return finReportReviewT.isPresent() ? finReportReviewT.get() : null;
  }

  @Override
  public FinReportReview insert(FinReportReview finReportReview, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + finReportReview.getFinReportReviewId());
    if (this.findById(finReportReview.getFinReportReviewId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      finReportReview.setCreateEmpNo(empNot);

    if(finReportReview.getLastUpdateEmpNo() == null || finReportReview.getLastUpdateEmpNo().isEmpty())
      finReportReview.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return finReportReviewReposDay.saveAndFlush(finReportReview);	
    else if (dbName.equals(ContentName.onMon))
      return finReportReviewReposMon.saveAndFlush(finReportReview);
    else if (dbName.equals(ContentName.onHist))
      return finReportReviewReposHist.saveAndFlush(finReportReview);
    else 
    return finReportReviewRepos.saveAndFlush(finReportReview);
  }

  @Override
  public FinReportReview update(FinReportReview finReportReview, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + finReportReview.getFinReportReviewId());
    if (!empNot.isEmpty())
      finReportReview.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return finReportReviewReposDay.saveAndFlush(finReportReview);	
    else if (dbName.equals(ContentName.onMon))
      return finReportReviewReposMon.saveAndFlush(finReportReview);
    else if (dbName.equals(ContentName.onHist))
      return finReportReviewReposHist.saveAndFlush(finReportReview);
    else 
    return finReportReviewRepos.saveAndFlush(finReportReview);
  }

  @Override
  public FinReportReview update2(FinReportReview finReportReview, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + finReportReview.getFinReportReviewId());
    if (!empNot.isEmpty())
      finReportReview.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      finReportReviewReposDay.saveAndFlush(finReportReview);	
    else if (dbName.equals(ContentName.onMon))
      finReportReviewReposMon.saveAndFlush(finReportReview);
    else if (dbName.equals(ContentName.onHist))
        finReportReviewReposHist.saveAndFlush(finReportReview);
    else 
      finReportReviewRepos.saveAndFlush(finReportReview);	
    return this.findById(finReportReview.getFinReportReviewId());
  }

  @Override
  public void delete(FinReportReview finReportReview, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + finReportReview.getFinReportReviewId());
    if (dbName.equals(ContentName.onDay)) {
      finReportReviewReposDay.delete(finReportReview);	
      finReportReviewReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportReviewReposMon.delete(finReportReview);	
      finReportReviewReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportReviewReposHist.delete(finReportReview);
      finReportReviewReposHist.flush();
    }
    else {
      finReportReviewRepos.delete(finReportReview);
      finReportReviewRepos.flush();
    }
   }

  @Override
  public void insertAll(List<FinReportReview> finReportReview, TitaVo... titaVo) throws DBException {
    if (finReportReview == null || finReportReview.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (FinReportReview t : finReportReview){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      finReportReview = finReportReviewReposDay.saveAll(finReportReview);	
      finReportReviewReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportReview = finReportReviewReposMon.saveAll(finReportReview);	
      finReportReviewReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportReview = finReportReviewReposHist.saveAll(finReportReview);
      finReportReviewReposHist.flush();
    }
    else {
      finReportReview = finReportReviewRepos.saveAll(finReportReview);
      finReportReviewRepos.flush();
    }
    }

  @Override
  public void updateAll(List<FinReportReview> finReportReview, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (finReportReview == null || finReportReview.size() == 0)
      throw new DBException(6);

    for (FinReportReview t : finReportReview) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      finReportReview = finReportReviewReposDay.saveAll(finReportReview);	
      finReportReviewReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportReview = finReportReviewReposMon.saveAll(finReportReview);	
      finReportReviewReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportReview = finReportReviewReposHist.saveAll(finReportReview);
      finReportReviewReposHist.flush();
    }
    else {
      finReportReview = finReportReviewRepos.saveAll(finReportReview);
      finReportReviewRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<FinReportReview> finReportReview, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (finReportReview == null || finReportReview.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      finReportReviewReposDay.deleteAll(finReportReview);	
      finReportReviewReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportReviewReposMon.deleteAll(finReportReview);	
      finReportReviewReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportReviewReposHist.deleteAll(finReportReview);
      finReportReviewReposHist.flush();
    }
    else {
      finReportReviewRepos.deleteAll(finReportReview);
      finReportReviewRepos.flush();
    }
  }

}
