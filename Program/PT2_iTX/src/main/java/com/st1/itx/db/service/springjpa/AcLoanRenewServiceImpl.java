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
import com.st1.itx.db.domain.AcLoanRenew;
import com.st1.itx.db.domain.AcLoanRenewId;
import com.st1.itx.db.repository.online.AcLoanRenewRepository;
import com.st1.itx.db.repository.day.AcLoanRenewRepositoryDay;
import com.st1.itx.db.repository.mon.AcLoanRenewRepositoryMon;
import com.st1.itx.db.repository.hist.AcLoanRenewRepositoryHist;
import com.st1.itx.db.service.AcLoanRenewService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("acLoanRenewService")
@Repository
public class AcLoanRenewServiceImpl extends ASpringJpaParm implements AcLoanRenewService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private AcLoanRenewRepository acLoanRenewRepos;

  @Autowired
  private AcLoanRenewRepositoryDay acLoanRenewReposDay;

  @Autowired
  private AcLoanRenewRepositoryMon acLoanRenewReposMon;

  @Autowired
  private AcLoanRenewRepositoryHist acLoanRenewReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(acLoanRenewRepos);
    org.junit.Assert.assertNotNull(acLoanRenewReposDay);
    org.junit.Assert.assertNotNull(acLoanRenewReposMon);
    org.junit.Assert.assertNotNull(acLoanRenewReposHist);
  }

  @Override
  public AcLoanRenew findById(AcLoanRenewId acLoanRenewId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + acLoanRenewId);
    Optional<AcLoanRenew> acLoanRenew = null;
    if (dbName.equals(ContentName.onDay))
      acLoanRenew = acLoanRenewReposDay.findById(acLoanRenewId);
    else if (dbName.equals(ContentName.onMon))
      acLoanRenew = acLoanRenewReposMon.findById(acLoanRenewId);
    else if (dbName.equals(ContentName.onHist))
      acLoanRenew = acLoanRenewReposHist.findById(acLoanRenewId);
    else 
      acLoanRenew = acLoanRenewRepos.findById(acLoanRenewId);
    AcLoanRenew obj = acLoanRenew.isPresent() ? acLoanRenew.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<AcLoanRenew> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcLoanRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "NewFacmNo", "NewBormNo", "OldFacmNo", "OldBormNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "NewFacmNo", "NewBormNo", "OldFacmNo", "OldBormNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = acLoanRenewReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acLoanRenewReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acLoanRenewReposHist.findAll(pageable);
    else 
      slice = acLoanRenewRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcLoanRenew> NewFacmNoNoRange(int custNo_0, int newFacmNo_1, int newFacmNo_2, int newBormNo_3, int newBormNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcLoanRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("NewFacmNoNoRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " newFacmNo_1 : " +  newFacmNo_1 + " newFacmNo_2 : " +  newFacmNo_2 + " newBormNo_3 : " +  newBormNo_3 + " newBormNo_4 : " +  newBormNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = acLoanRenewReposDay.findAllByCustNoIsAndNewFacmNoGreaterThanEqualAndNewFacmNoLessThanEqualAndNewBormNoGreaterThanEqualAndNewBormNoLessThanEqualOrderByNewFacmNoAscNewBormNoAsc(custNo_0, newFacmNo_1, newFacmNo_2, newBormNo_3, newBormNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acLoanRenewReposMon.findAllByCustNoIsAndNewFacmNoGreaterThanEqualAndNewFacmNoLessThanEqualAndNewBormNoGreaterThanEqualAndNewBormNoLessThanEqualOrderByNewFacmNoAscNewBormNoAsc(custNo_0, newFacmNo_1, newFacmNo_2, newBormNo_3, newBormNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acLoanRenewReposHist.findAllByCustNoIsAndNewFacmNoGreaterThanEqualAndNewFacmNoLessThanEqualAndNewBormNoGreaterThanEqualAndNewBormNoLessThanEqualOrderByNewFacmNoAscNewBormNoAsc(custNo_0, newFacmNo_1, newFacmNo_2, newBormNo_3, newBormNo_4, pageable);
    else 
      slice = acLoanRenewRepos.findAllByCustNoIsAndNewFacmNoGreaterThanEqualAndNewFacmNoLessThanEqualAndNewBormNoGreaterThanEqualAndNewBormNoLessThanEqualOrderByNewFacmNoAscNewBormNoAsc(custNo_0, newFacmNo_1, newFacmNo_2, newBormNo_3, newBormNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcLoanRenew> findL2079(int custNo_0, int custNo_1, int oldFacmNo_2, int oldFacmNo_3, int newFacmNo_4, int newFacmNo_5, int acDate_6, int acDate_7, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcLoanRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL2079 " + dbName + " : " + "custNo_0 : " + custNo_0 + " custNo_1 : " +  custNo_1 + " oldFacmNo_2 : " +  oldFacmNo_2 + " oldFacmNo_3 : " +  oldFacmNo_3 + " newFacmNo_4 : " +  newFacmNo_4 + " newFacmNo_5 : " +  newFacmNo_5 + " acDate_6 : " +  acDate_6 + " acDate_7 : " +  acDate_7);
    if (dbName.equals(ContentName.onDay))
      slice = acLoanRenewReposDay.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndOldFacmNoGreaterThanEqualAndOldFacmNoLessThanEqualAndNewFacmNoGreaterThanEqualAndNewFacmNoLessThanEqualAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustNoAscOldFacmNoAscOldBormNoAscNewFacmNoAscNewBormNoAscAcDateAsc(custNo_0, custNo_1, oldFacmNo_2, oldFacmNo_3, newFacmNo_4, newFacmNo_5, acDate_6, acDate_7, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acLoanRenewReposMon.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndOldFacmNoGreaterThanEqualAndOldFacmNoLessThanEqualAndNewFacmNoGreaterThanEqualAndNewFacmNoLessThanEqualAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustNoAscOldFacmNoAscOldBormNoAscNewFacmNoAscNewBormNoAscAcDateAsc(custNo_0, custNo_1, oldFacmNo_2, oldFacmNo_3, newFacmNo_4, newFacmNo_5, acDate_6, acDate_7, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acLoanRenewReposHist.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndOldFacmNoGreaterThanEqualAndOldFacmNoLessThanEqualAndNewFacmNoGreaterThanEqualAndNewFacmNoLessThanEqualAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustNoAscOldFacmNoAscOldBormNoAscNewFacmNoAscNewBormNoAscAcDateAsc(custNo_0, custNo_1, oldFacmNo_2, oldFacmNo_3, newFacmNo_4, newFacmNo_5, acDate_6, acDate_7, pageable);
    else 
      slice = acLoanRenewRepos.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndOldFacmNoGreaterThanEqualAndOldFacmNoLessThanEqualAndNewFacmNoGreaterThanEqualAndNewFacmNoLessThanEqualAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustNoAscOldFacmNoAscOldBormNoAscNewFacmNoAscNewBormNoAscAcDateAsc(custNo_0, custNo_1, oldFacmNo_2, oldFacmNo_3, newFacmNo_4, newFacmNo_5, acDate_6, acDate_7, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcLoanRenew> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcLoanRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custNoEq " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = acLoanRenewReposDay.findAllByCustNoIsOrderByCustNoAscOldFacmNoAscOldBormNoAscNewFacmNoAscNewBormNoAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acLoanRenewReposMon.findAllByCustNoIsOrderByCustNoAscOldFacmNoAscOldBormNoAscNewFacmNoAscNewBormNoAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acLoanRenewReposHist.findAllByCustNoIsOrderByCustNoAscOldFacmNoAscOldBormNoAscNewFacmNoAscNewBormNoAsc(custNo_0, pageable);
    else 
      slice = acLoanRenewRepos.findAllByCustNoIsOrderByCustNoAscOldFacmNoAscOldBormNoAscNewFacmNoAscNewBormNoAsc(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AcLoanRenew holdById(AcLoanRenewId acLoanRenewId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + acLoanRenewId);
    Optional<AcLoanRenew> acLoanRenew = null;
    if (dbName.equals(ContentName.onDay))
      acLoanRenew = acLoanRenewReposDay.findByAcLoanRenewId(acLoanRenewId);
    else if (dbName.equals(ContentName.onMon))
      acLoanRenew = acLoanRenewReposMon.findByAcLoanRenewId(acLoanRenewId);
    else if (dbName.equals(ContentName.onHist))
      acLoanRenew = acLoanRenewReposHist.findByAcLoanRenewId(acLoanRenewId);
    else 
      acLoanRenew = acLoanRenewRepos.findByAcLoanRenewId(acLoanRenewId);
    return acLoanRenew.isPresent() ? acLoanRenew.get() : null;
  }

  @Override
  public AcLoanRenew holdById(AcLoanRenew acLoanRenew, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + acLoanRenew.getAcLoanRenewId());
    Optional<AcLoanRenew> acLoanRenewT = null;
    if (dbName.equals(ContentName.onDay))
      acLoanRenewT = acLoanRenewReposDay.findByAcLoanRenewId(acLoanRenew.getAcLoanRenewId());
    else if (dbName.equals(ContentName.onMon))
      acLoanRenewT = acLoanRenewReposMon.findByAcLoanRenewId(acLoanRenew.getAcLoanRenewId());
    else if (dbName.equals(ContentName.onHist))
      acLoanRenewT = acLoanRenewReposHist.findByAcLoanRenewId(acLoanRenew.getAcLoanRenewId());
    else 
      acLoanRenewT = acLoanRenewRepos.findByAcLoanRenewId(acLoanRenew.getAcLoanRenewId());
    return acLoanRenewT.isPresent() ? acLoanRenewT.get() : null;
  }

  @Override
  public AcLoanRenew insert(AcLoanRenew acLoanRenew, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + acLoanRenew.getAcLoanRenewId());
    if (this.findById(acLoanRenew.getAcLoanRenewId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      acLoanRenew.setCreateEmpNo(empNot);

    if(acLoanRenew.getLastUpdateEmpNo() == null || acLoanRenew.getLastUpdateEmpNo().isEmpty())
      acLoanRenew.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return acLoanRenewReposDay.saveAndFlush(acLoanRenew);	
    else if (dbName.equals(ContentName.onMon))
      return acLoanRenewReposMon.saveAndFlush(acLoanRenew);
    else if (dbName.equals(ContentName.onHist))
      return acLoanRenewReposHist.saveAndFlush(acLoanRenew);
    else 
    return acLoanRenewRepos.saveAndFlush(acLoanRenew);
  }

  @Override
  public AcLoanRenew update(AcLoanRenew acLoanRenew, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + acLoanRenew.getAcLoanRenewId());
    if (!empNot.isEmpty())
      acLoanRenew.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return acLoanRenewReposDay.saveAndFlush(acLoanRenew);	
    else if (dbName.equals(ContentName.onMon))
      return acLoanRenewReposMon.saveAndFlush(acLoanRenew);
    else if (dbName.equals(ContentName.onHist))
      return acLoanRenewReposHist.saveAndFlush(acLoanRenew);
    else 
    return acLoanRenewRepos.saveAndFlush(acLoanRenew);
  }

  @Override
  public AcLoanRenew update2(AcLoanRenew acLoanRenew, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + acLoanRenew.getAcLoanRenewId());
    if (!empNot.isEmpty())
      acLoanRenew.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      acLoanRenewReposDay.saveAndFlush(acLoanRenew);	
    else if (dbName.equals(ContentName.onMon))
      acLoanRenewReposMon.saveAndFlush(acLoanRenew);
    else if (dbName.equals(ContentName.onHist))
        acLoanRenewReposHist.saveAndFlush(acLoanRenew);
    else 
      acLoanRenewRepos.saveAndFlush(acLoanRenew);	
    return this.findById(acLoanRenew.getAcLoanRenewId());
  }

  @Override
  public void delete(AcLoanRenew acLoanRenew, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + acLoanRenew.getAcLoanRenewId());
    if (dbName.equals(ContentName.onDay)) {
      acLoanRenewReposDay.delete(acLoanRenew);	
      acLoanRenewReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acLoanRenewReposMon.delete(acLoanRenew);	
      acLoanRenewReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acLoanRenewReposHist.delete(acLoanRenew);
      acLoanRenewReposHist.flush();
    }
    else {
      acLoanRenewRepos.delete(acLoanRenew);
      acLoanRenewRepos.flush();
    }
   }

  @Override
  public void insertAll(List<AcLoanRenew> acLoanRenew, TitaVo... titaVo) throws DBException {
    if (acLoanRenew == null || acLoanRenew.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (AcLoanRenew t : acLoanRenew){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      acLoanRenew = acLoanRenewReposDay.saveAll(acLoanRenew);	
      acLoanRenewReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acLoanRenew = acLoanRenewReposMon.saveAll(acLoanRenew);	
      acLoanRenewReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acLoanRenew = acLoanRenewReposHist.saveAll(acLoanRenew);
      acLoanRenewReposHist.flush();
    }
    else {
      acLoanRenew = acLoanRenewRepos.saveAll(acLoanRenew);
      acLoanRenewRepos.flush();
    }
    }

  @Override
  public void updateAll(List<AcLoanRenew> acLoanRenew, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (acLoanRenew == null || acLoanRenew.size() == 0)
      throw new DBException(6);

    for (AcLoanRenew t : acLoanRenew) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      acLoanRenew = acLoanRenewReposDay.saveAll(acLoanRenew);	
      acLoanRenewReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acLoanRenew = acLoanRenewReposMon.saveAll(acLoanRenew);	
      acLoanRenewReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acLoanRenew = acLoanRenewReposHist.saveAll(acLoanRenew);
      acLoanRenewReposHist.flush();
    }
    else {
      acLoanRenew = acLoanRenewRepos.saveAll(acLoanRenew);
      acLoanRenewRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<AcLoanRenew> acLoanRenew, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (acLoanRenew == null || acLoanRenew.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      acLoanRenewReposDay.deleteAll(acLoanRenew);	
      acLoanRenewReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acLoanRenewReposMon.deleteAll(acLoanRenew);	
      acLoanRenewReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acLoanRenewReposHist.deleteAll(acLoanRenew);
      acLoanRenewReposHist.flush();
    }
    else {
      acLoanRenewRepos.deleteAll(acLoanRenew);
      acLoanRenewRepos.flush();
    }
  }

}
