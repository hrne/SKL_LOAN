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
import com.st1.itx.db.domain.CreditRating;
import com.st1.itx.db.domain.CreditRatingId;
import com.st1.itx.db.repository.online.CreditRatingRepository;
import com.st1.itx.db.repository.day.CreditRatingRepositoryDay;
import com.st1.itx.db.repository.mon.CreditRatingRepositoryMon;
import com.st1.itx.db.repository.hist.CreditRatingRepositoryHist;
import com.st1.itx.db.service.CreditRatingService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("creditRatingService")
@Repository
public class CreditRatingServiceImpl extends ASpringJpaParm implements CreditRatingService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CreditRatingRepository creditRatingRepos;

  @Autowired
  private CreditRatingRepositoryDay creditRatingReposDay;

  @Autowired
  private CreditRatingRepositoryMon creditRatingReposMon;

  @Autowired
  private CreditRatingRepositoryHist creditRatingReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(creditRatingRepos);
    org.junit.Assert.assertNotNull(creditRatingReposDay);
    org.junit.Assert.assertNotNull(creditRatingReposMon);
    org.junit.Assert.assertNotNull(creditRatingReposHist);
  }

  @Override
  public CreditRating findById(CreditRatingId creditRatingId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + creditRatingId);
    Optional<CreditRating> creditRating = null;
    if (dbName.equals(ContentName.onDay))
      creditRating = creditRatingReposDay.findById(creditRatingId);
    else if (dbName.equals(ContentName.onMon))
      creditRating = creditRatingReposMon.findById(creditRatingId);
    else if (dbName.equals(ContentName.onHist))
      creditRating = creditRatingReposHist.findById(creditRatingId);
    else 
      creditRating = creditRatingRepos.findById(creditRatingId);
    CreditRating obj = creditRating.isPresent() ? creditRating.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CreditRating> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CreditRating> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = creditRatingReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = creditRatingReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = creditRatingReposHist.findAll(pageable);
    else 
      slice = creditRatingRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CreditRating holdById(CreditRatingId creditRatingId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + creditRatingId);
    Optional<CreditRating> creditRating = null;
    if (dbName.equals(ContentName.onDay))
      creditRating = creditRatingReposDay.findByCreditRatingId(creditRatingId);
    else if (dbName.equals(ContentName.onMon))
      creditRating = creditRatingReposMon.findByCreditRatingId(creditRatingId);
    else if (dbName.equals(ContentName.onHist))
      creditRating = creditRatingReposHist.findByCreditRatingId(creditRatingId);
    else 
      creditRating = creditRatingRepos.findByCreditRatingId(creditRatingId);
    return creditRating.isPresent() ? creditRating.get() : null;
  }

  @Override
  public CreditRating holdById(CreditRating creditRating, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + creditRating.getCreditRatingId());
    Optional<CreditRating> creditRatingT = null;
    if (dbName.equals(ContentName.onDay))
      creditRatingT = creditRatingReposDay.findByCreditRatingId(creditRating.getCreditRatingId());
    else if (dbName.equals(ContentName.onMon))
      creditRatingT = creditRatingReposMon.findByCreditRatingId(creditRating.getCreditRatingId());
    else if (dbName.equals(ContentName.onHist))
      creditRatingT = creditRatingReposHist.findByCreditRatingId(creditRating.getCreditRatingId());
    else 
      creditRatingT = creditRatingRepos.findByCreditRatingId(creditRating.getCreditRatingId());
    return creditRatingT.isPresent() ? creditRatingT.get() : null;
  }

  @Override
  public CreditRating insert(CreditRating creditRating, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + creditRating.getCreditRatingId());
    if (this.findById(creditRating.getCreditRatingId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      creditRating.setCreateEmpNo(empNot);

    if(creditRating.getLastUpdateEmpNo() == null || creditRating.getLastUpdateEmpNo().isEmpty())
      creditRating.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return creditRatingReposDay.saveAndFlush(creditRating);	
    else if (dbName.equals(ContentName.onMon))
      return creditRatingReposMon.saveAndFlush(creditRating);
    else if (dbName.equals(ContentName.onHist))
      return creditRatingReposHist.saveAndFlush(creditRating);
    else 
    return creditRatingRepos.saveAndFlush(creditRating);
  }

  @Override
  public CreditRating update(CreditRating creditRating, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + creditRating.getCreditRatingId());
    if (!empNot.isEmpty())
      creditRating.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return creditRatingReposDay.saveAndFlush(creditRating);	
    else if (dbName.equals(ContentName.onMon))
      return creditRatingReposMon.saveAndFlush(creditRating);
    else if (dbName.equals(ContentName.onHist))
      return creditRatingReposHist.saveAndFlush(creditRating);
    else 
    return creditRatingRepos.saveAndFlush(creditRating);
  }

  @Override
  public CreditRating update2(CreditRating creditRating, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + creditRating.getCreditRatingId());
    if (!empNot.isEmpty())
      creditRating.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      creditRatingReposDay.saveAndFlush(creditRating);	
    else if (dbName.equals(ContentName.onMon))
      creditRatingReposMon.saveAndFlush(creditRating);
    else if (dbName.equals(ContentName.onHist))
        creditRatingReposHist.saveAndFlush(creditRating);
    else 
      creditRatingRepos.saveAndFlush(creditRating);	
    return this.findById(creditRating.getCreditRatingId());
  }

  @Override
  public void delete(CreditRating creditRating, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + creditRating.getCreditRatingId());
    if (dbName.equals(ContentName.onDay)) {
      creditRatingReposDay.delete(creditRating);	
      creditRatingReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      creditRatingReposMon.delete(creditRating);	
      creditRatingReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      creditRatingReposHist.delete(creditRating);
      creditRatingReposHist.flush();
    }
    else {
      creditRatingRepos.delete(creditRating);
      creditRatingRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CreditRating> creditRating, TitaVo... titaVo) throws DBException {
    if (creditRating == null || creditRating.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CreditRating t : creditRating){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      creditRating = creditRatingReposDay.saveAll(creditRating);	
      creditRatingReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      creditRating = creditRatingReposMon.saveAll(creditRating);	
      creditRatingReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      creditRating = creditRatingReposHist.saveAll(creditRating);
      creditRatingReposHist.flush();
    }
    else {
      creditRating = creditRatingRepos.saveAll(creditRating);
      creditRatingRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CreditRating> creditRating, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (creditRating == null || creditRating.size() == 0)
      throw new DBException(6);

    for (CreditRating t : creditRating) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      creditRating = creditRatingReposDay.saveAll(creditRating);	
      creditRatingReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      creditRating = creditRatingReposMon.saveAll(creditRating);	
      creditRatingReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      creditRating = creditRatingReposHist.saveAll(creditRating);
      creditRatingReposHist.flush();
    }
    else {
      creditRating = creditRatingRepos.saveAll(creditRating);
      creditRatingRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CreditRating> creditRating, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (creditRating == null || creditRating.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      creditRatingReposDay.deleteAll(creditRating);	
      creditRatingReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      creditRatingReposMon.deleteAll(creditRating);	
      creditRatingReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      creditRatingReposHist.deleteAll(creditRating);
      creditRatingReposHist.flush();
    }
    else {
      creditRatingRepos.deleteAll(creditRating);
      creditRatingRepos.flush();
    }
  }

}
