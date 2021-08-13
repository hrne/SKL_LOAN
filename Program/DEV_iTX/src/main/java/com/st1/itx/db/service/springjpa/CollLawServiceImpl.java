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
import com.st1.itx.db.domain.CollLaw;
import com.st1.itx.db.domain.CollLawId;
import com.st1.itx.db.repository.online.CollLawRepository;
import com.st1.itx.db.repository.day.CollLawRepositoryDay;
import com.st1.itx.db.repository.mon.CollLawRepositoryMon;
import com.st1.itx.db.repository.hist.CollLawRepositoryHist;
import com.st1.itx.db.service.CollLawService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("collLawService")
@Repository
public class CollLawServiceImpl extends ASpringJpaParm implements CollLawService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CollLawRepository collLawRepos;

  @Autowired
  private CollLawRepositoryDay collLawReposDay;

  @Autowired
  private CollLawRepositoryMon collLawReposMon;

  @Autowired
  private CollLawRepositoryHist collLawReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(collLawRepos);
    org.junit.Assert.assertNotNull(collLawReposDay);
    org.junit.Assert.assertNotNull(collLawReposMon);
    org.junit.Assert.assertNotNull(collLawReposHist);
  }

  @Override
  public CollLaw findById(CollLawId collLawId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + collLawId);
    Optional<CollLaw> collLaw = null;
    if (dbName.equals(ContentName.onDay))
      collLaw = collLawReposDay.findById(collLawId);
    else if (dbName.equals(ContentName.onMon))
      collLaw = collLawReposMon.findById(collLawId);
    else if (dbName.equals(ContentName.onHist))
      collLaw = collLawReposHist.findById(collLawId);
    else 
      collLaw = collLawRepos.findById(collLawId);
    CollLaw obj = collLaw.isPresent() ? collLaw.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CollLaw> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollLaw> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CaseCode", "CustNo", "FacmNo", "AcDate", "TitaTlrNo", "TitaTxtNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = collLawReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collLawReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collLawReposHist.findAll(pageable);
    else 
      slice = collLawRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CollLaw> telTimeBetween(int acDate_0, int acDate_1, String caseCode_2, int custNo_3, int facmNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollLaw> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("telTimeBetween " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " caseCode_2 : " +  caseCode_2 + " custNo_3 : " +  custNo_3 + " facmNo_4 : " +  facmNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = collLawReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByAcDateDesc(acDate_0, acDate_1, caseCode_2, custNo_3, facmNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collLawReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByAcDateDesc(acDate_0, acDate_1, caseCode_2, custNo_3, facmNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collLawReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByAcDateDesc(acDate_0, acDate_1, caseCode_2, custNo_3, facmNo_4, pageable);
    else 
      slice = collLawRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByAcDateDesc(acDate_0, acDate_1, caseCode_2, custNo_3, facmNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CollLaw> findSameCust(String caseCode_0, int custNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollLaw> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findSameCust " + dbName + " : " + "caseCode_0 : " + caseCode_0 + " custNo_1 : " +  custNo_1 + " facmNo_2 : " +  facmNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = collLawReposDay.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByAcDateDesc(caseCode_0, custNo_1, facmNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collLawReposMon.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByAcDateDesc(caseCode_0, custNo_1, facmNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collLawReposHist.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByAcDateDesc(caseCode_0, custNo_1, facmNo_2, pageable);
    else 
      slice = collLawRepos.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByAcDateDesc(caseCode_0, custNo_1, facmNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CollLaw> withoutFacmNo(int acDate_0, int acDate_1, String caseCode_2, int custNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollLaw> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("withoutFacmNo " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " caseCode_2 : " +  caseCode_2 + " custNo_3 : " +  custNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = collLawReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByAcDateDesc(acDate_0, acDate_1, caseCode_2, custNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collLawReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByAcDateDesc(acDate_0, acDate_1, caseCode_2, custNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collLawReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByAcDateDesc(acDate_0, acDate_1, caseCode_2, custNo_3, pageable);
    else 
      slice = collLawRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByAcDateDesc(acDate_0, acDate_1, caseCode_2, custNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CollLaw> withoutFacmNoAll(String caseCode_0, int custNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollLaw> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("withoutFacmNoAll " + dbName + " : " + "caseCode_0 : " + caseCode_0 + " custNo_1 : " +  custNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = collLawReposDay.findAllByCaseCodeIsAndCustNoIsOrderByAcDateDesc(caseCode_0, custNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collLawReposMon.findAllByCaseCodeIsAndCustNoIsOrderByAcDateDesc(caseCode_0, custNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collLawReposHist.findAllByCaseCodeIsAndCustNoIsOrderByAcDateDesc(caseCode_0, custNo_1, pageable);
    else 
      slice = collLawRepos.findAllByCaseCodeIsAndCustNoIsOrderByAcDateDesc(caseCode_0, custNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CollLaw findFacmNoFirst(String caseCode_0, int custNo_1, int facmNo_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findFacmNoFirst " + dbName + " : " + "caseCode_0 : " + caseCode_0 + " custNo_1 : " +  custNo_1 + " facmNo_2 : " +  facmNo_2);
    Optional<CollLaw> collLawT = null;
    if (dbName.equals(ContentName.onDay))
      collLawT = collLawReposDay.findTopByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByRecordDateDescAcDateDesc(caseCode_0, custNo_1, facmNo_2);
    else if (dbName.equals(ContentName.onMon))
      collLawT = collLawReposMon.findTopByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByRecordDateDescAcDateDesc(caseCode_0, custNo_1, facmNo_2);
    else if (dbName.equals(ContentName.onHist))
      collLawT = collLawReposHist.findTopByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByRecordDateDescAcDateDesc(caseCode_0, custNo_1, facmNo_2);
    else 
      collLawT = collLawRepos.findTopByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByRecordDateDescAcDateDesc(caseCode_0, custNo_1, facmNo_2);

    return collLawT.isPresent() ? collLawT.get() : null;
  }

  @Override
  public CollLaw holdById(CollLawId collLawId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + collLawId);
    Optional<CollLaw> collLaw = null;
    if (dbName.equals(ContentName.onDay))
      collLaw = collLawReposDay.findByCollLawId(collLawId);
    else if (dbName.equals(ContentName.onMon))
      collLaw = collLawReposMon.findByCollLawId(collLawId);
    else if (dbName.equals(ContentName.onHist))
      collLaw = collLawReposHist.findByCollLawId(collLawId);
    else 
      collLaw = collLawRepos.findByCollLawId(collLawId);
    return collLaw.isPresent() ? collLaw.get() : null;
  }

  @Override
  public CollLaw holdById(CollLaw collLaw, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + collLaw.getCollLawId());
    Optional<CollLaw> collLawT = null;
    if (dbName.equals(ContentName.onDay))
      collLawT = collLawReposDay.findByCollLawId(collLaw.getCollLawId());
    else if (dbName.equals(ContentName.onMon))
      collLawT = collLawReposMon.findByCollLawId(collLaw.getCollLawId());
    else if (dbName.equals(ContentName.onHist))
      collLawT = collLawReposHist.findByCollLawId(collLaw.getCollLawId());
    else 
      collLawT = collLawRepos.findByCollLawId(collLaw.getCollLawId());
    return collLawT.isPresent() ? collLawT.get() : null;
  }

  @Override
  public CollLaw insert(CollLaw collLaw, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + collLaw.getCollLawId());
    if (this.findById(collLaw.getCollLawId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      collLaw.setCreateEmpNo(empNot);

    if(collLaw.getLastUpdateEmpNo() == null || collLaw.getLastUpdateEmpNo().isEmpty())
      collLaw.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return collLawReposDay.saveAndFlush(collLaw);	
    else if (dbName.equals(ContentName.onMon))
      return collLawReposMon.saveAndFlush(collLaw);
    else if (dbName.equals(ContentName.onHist))
      return collLawReposHist.saveAndFlush(collLaw);
    else 
    return collLawRepos.saveAndFlush(collLaw);
  }

  @Override
  public CollLaw update(CollLaw collLaw, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + collLaw.getCollLawId());
    if (!empNot.isEmpty())
      collLaw.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return collLawReposDay.saveAndFlush(collLaw);	
    else if (dbName.equals(ContentName.onMon))
      return collLawReposMon.saveAndFlush(collLaw);
    else if (dbName.equals(ContentName.onHist))
      return collLawReposHist.saveAndFlush(collLaw);
    else 
    return collLawRepos.saveAndFlush(collLaw);
  }

  @Override
  public CollLaw update2(CollLaw collLaw, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + collLaw.getCollLawId());
    if (!empNot.isEmpty())
      collLaw.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      collLawReposDay.saveAndFlush(collLaw);	
    else if (dbName.equals(ContentName.onMon))
      collLawReposMon.saveAndFlush(collLaw);
    else if (dbName.equals(ContentName.onHist))
        collLawReposHist.saveAndFlush(collLaw);
    else 
      collLawRepos.saveAndFlush(collLaw);	
    return this.findById(collLaw.getCollLawId());
  }

  @Override
  public void delete(CollLaw collLaw, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + collLaw.getCollLawId());
    if (dbName.equals(ContentName.onDay)) {
      collLawReposDay.delete(collLaw);	
      collLawReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collLawReposMon.delete(collLaw);	
      collLawReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collLawReposHist.delete(collLaw);
      collLawReposHist.flush();
    }
    else {
      collLawRepos.delete(collLaw);
      collLawRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CollLaw> collLaw, TitaVo... titaVo) throws DBException {
    if (collLaw == null || collLaw.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (CollLaw t : collLaw){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      collLaw = collLawReposDay.saveAll(collLaw);	
      collLawReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collLaw = collLawReposMon.saveAll(collLaw);	
      collLawReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collLaw = collLawReposHist.saveAll(collLaw);
      collLawReposHist.flush();
    }
    else {
      collLaw = collLawRepos.saveAll(collLaw);
      collLawRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CollLaw> collLaw, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (collLaw == null || collLaw.size() == 0)
      throw new DBException(6);

    for (CollLaw t : collLaw) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      collLaw = collLawReposDay.saveAll(collLaw);	
      collLawReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collLaw = collLawReposMon.saveAll(collLaw);	
      collLawReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collLaw = collLawReposHist.saveAll(collLaw);
      collLawReposHist.flush();
    }
    else {
      collLaw = collLawRepos.saveAll(collLaw);
      collLawRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CollLaw> collLaw, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (collLaw == null || collLaw.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      collLawReposDay.deleteAll(collLaw);	
      collLawReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collLawReposMon.deleteAll(collLaw);	
      collLawReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collLawReposHist.deleteAll(collLaw);
      collLawReposHist.flush();
    }
    else {
      collLawRepos.deleteAll(collLaw);
      collLawRepos.flush();
    }
  }

}
