package com.st1.itx.db.service.springjpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

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
import com.st1.itx.db.domain.CollMeet;
import com.st1.itx.db.domain.CollMeetId;
import com.st1.itx.db.repository.online.CollMeetRepository;
import com.st1.itx.db.repository.day.CollMeetRepositoryDay;
import com.st1.itx.db.repository.mon.CollMeetRepositoryMon;
import com.st1.itx.db.repository.hist.CollMeetRepositoryHist;
import com.st1.itx.db.service.CollMeetService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("collMeetService")
@Repository
public class CollMeetServiceImpl extends ASpringJpaParm implements CollMeetService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CollMeetRepository collMeetRepos;

  @Autowired
  private CollMeetRepositoryDay collMeetReposDay;

  @Autowired
  private CollMeetRepositoryMon collMeetReposMon;

  @Autowired
  private CollMeetRepositoryHist collMeetReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(collMeetRepos);
    org.junit.Assert.assertNotNull(collMeetReposDay);
    org.junit.Assert.assertNotNull(collMeetReposMon);
    org.junit.Assert.assertNotNull(collMeetReposHist);
  }

  @Override
  public CollMeet findById(CollMeetId collMeetId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + collMeetId);
    Optional<CollMeet> collMeet = null;
    if (dbName.equals(ContentName.onDay))
      collMeet = collMeetReposDay.findById(collMeetId);
    else if (dbName.equals(ContentName.onMon))
      collMeet = collMeetReposMon.findById(collMeetId);
    else if (dbName.equals(ContentName.onHist))
      collMeet = collMeetReposHist.findById(collMeetId);
    else 
      collMeet = collMeetRepos.findById(collMeetId);
    CollMeet obj = collMeet.isPresent() ? collMeet.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CollMeet> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollMeet> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CaseCode", "CustNo", "FacmNo", "AcDate", "TitaTlrNo", "TitaTxtNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CaseCode", "CustNo", "FacmNo", "AcDate", "TitaTlrNo", "TitaTxtNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = collMeetReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collMeetReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collMeetReposHist.findAll(pageable);
    else 
      slice = collMeetRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CollMeet> telTimeBetween(int meetDate_0, int meetDate_1, String caseCode_2, int custNo_3, int facmNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollMeet> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("telTimeBetween " + dbName + " : " + "meetDate_0 : " + meetDate_0 + " meetDate_1 : " +  meetDate_1 + " caseCode_2 : " +  caseCode_2 + " custNo_3 : " +  custNo_3 + " facmNo_4 : " +  facmNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = collMeetReposDay.findAllByMeetDateGreaterThanEqualAndMeetDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMeetDateDesc(meetDate_0, meetDate_1, caseCode_2, custNo_3, facmNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collMeetReposMon.findAllByMeetDateGreaterThanEqualAndMeetDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMeetDateDesc(meetDate_0, meetDate_1, caseCode_2, custNo_3, facmNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collMeetReposHist.findAllByMeetDateGreaterThanEqualAndMeetDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMeetDateDesc(meetDate_0, meetDate_1, caseCode_2, custNo_3, facmNo_4, pageable);
    else 
      slice = collMeetRepos.findAllByMeetDateGreaterThanEqualAndMeetDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMeetDateDesc(meetDate_0, meetDate_1, caseCode_2, custNo_3, facmNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CollMeet> findSameCust(String caseCode_0, int custNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollMeet> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findSameCust " + dbName + " : " + "caseCode_0 : " + caseCode_0 + " custNo_1 : " +  custNo_1 + " facmNo_2 : " +  facmNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = collMeetReposDay.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMeetDateDesc(caseCode_0, custNo_1, facmNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collMeetReposMon.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMeetDateDesc(caseCode_0, custNo_1, facmNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collMeetReposHist.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMeetDateDesc(caseCode_0, custNo_1, facmNo_2, pageable);
    else 
      slice = collMeetRepos.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMeetDateDesc(caseCode_0, custNo_1, facmNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CollMeet> withoutFacmNo(int meetDate_0, int meetDate_1, String caseCode_2, int custNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollMeet> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("withoutFacmNo " + dbName + " : " + "meetDate_0 : " + meetDate_0 + " meetDate_1 : " +  meetDate_1 + " caseCode_2 : " +  caseCode_2 + " custNo_3 : " +  custNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = collMeetReposDay.findAllByMeetDateGreaterThanEqualAndMeetDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByMeetDateDesc(meetDate_0, meetDate_1, caseCode_2, custNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collMeetReposMon.findAllByMeetDateGreaterThanEqualAndMeetDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByMeetDateDesc(meetDate_0, meetDate_1, caseCode_2, custNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collMeetReposHist.findAllByMeetDateGreaterThanEqualAndMeetDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByMeetDateDesc(meetDate_0, meetDate_1, caseCode_2, custNo_3, pageable);
    else 
      slice = collMeetRepos.findAllByMeetDateGreaterThanEqualAndMeetDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByMeetDateDesc(meetDate_0, meetDate_1, caseCode_2, custNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CollMeet> withoutFacmNoAll(String caseCode_0, int custNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollMeet> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("withoutFacmNoAll " + dbName + " : " + "caseCode_0 : " + caseCode_0 + " custNo_1 : " +  custNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = collMeetReposDay.findAllByCaseCodeIsAndCustNoIsOrderByMeetDateDesc(caseCode_0, custNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collMeetReposMon.findAllByCaseCodeIsAndCustNoIsOrderByMeetDateDesc(caseCode_0, custNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collMeetReposHist.findAllByCaseCodeIsAndCustNoIsOrderByMeetDateDesc(caseCode_0, custNo_1, pageable);
    else 
      slice = collMeetRepos.findAllByCaseCodeIsAndCustNoIsOrderByMeetDateDesc(caseCode_0, custNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CollMeet holdById(CollMeetId collMeetId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + collMeetId);
    Optional<CollMeet> collMeet = null;
    if (dbName.equals(ContentName.onDay))
      collMeet = collMeetReposDay.findByCollMeetId(collMeetId);
    else if (dbName.equals(ContentName.onMon))
      collMeet = collMeetReposMon.findByCollMeetId(collMeetId);
    else if (dbName.equals(ContentName.onHist))
      collMeet = collMeetReposHist.findByCollMeetId(collMeetId);
    else 
      collMeet = collMeetRepos.findByCollMeetId(collMeetId);
    return collMeet.isPresent() ? collMeet.get() : null;
  }

  @Override
  public CollMeet holdById(CollMeet collMeet, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + collMeet.getCollMeetId());
    Optional<CollMeet> collMeetT = null;
    if (dbName.equals(ContentName.onDay))
      collMeetT = collMeetReposDay.findByCollMeetId(collMeet.getCollMeetId());
    else if (dbName.equals(ContentName.onMon))
      collMeetT = collMeetReposMon.findByCollMeetId(collMeet.getCollMeetId());
    else if (dbName.equals(ContentName.onHist))
      collMeetT = collMeetReposHist.findByCollMeetId(collMeet.getCollMeetId());
    else 
      collMeetT = collMeetRepos.findByCollMeetId(collMeet.getCollMeetId());
    return collMeetT.isPresent() ? collMeetT.get() : null;
  }

  @Override
  public CollMeet insert(CollMeet collMeet, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + collMeet.getCollMeetId());
    if (this.findById(collMeet.getCollMeetId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      collMeet.setCreateEmpNo(empNot);

    if(collMeet.getLastUpdateEmpNo() == null || collMeet.getLastUpdateEmpNo().isEmpty())
      collMeet.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return collMeetReposDay.saveAndFlush(collMeet);	
    else if (dbName.equals(ContentName.onMon))
      return collMeetReposMon.saveAndFlush(collMeet);
    else if (dbName.equals(ContentName.onHist))
      return collMeetReposHist.saveAndFlush(collMeet);
    else 
    return collMeetRepos.saveAndFlush(collMeet);
  }

  @Override
  public CollMeet update(CollMeet collMeet, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + collMeet.getCollMeetId());
    if (!empNot.isEmpty())
      collMeet.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return collMeetReposDay.saveAndFlush(collMeet);	
    else if (dbName.equals(ContentName.onMon))
      return collMeetReposMon.saveAndFlush(collMeet);
    else if (dbName.equals(ContentName.onHist))
      return collMeetReposHist.saveAndFlush(collMeet);
    else 
    return collMeetRepos.saveAndFlush(collMeet);
  }

  @Override
  public CollMeet update2(CollMeet collMeet, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + collMeet.getCollMeetId());
    if (!empNot.isEmpty())
      collMeet.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      collMeetReposDay.saveAndFlush(collMeet);	
    else if (dbName.equals(ContentName.onMon))
      collMeetReposMon.saveAndFlush(collMeet);
    else if (dbName.equals(ContentName.onHist))
        collMeetReposHist.saveAndFlush(collMeet);
    else 
      collMeetRepos.saveAndFlush(collMeet);	
    return this.findById(collMeet.getCollMeetId());
  }

  @Override
  public void delete(CollMeet collMeet, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + collMeet.getCollMeetId());
    if (dbName.equals(ContentName.onDay)) {
      collMeetReposDay.delete(collMeet);	
      collMeetReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collMeetReposMon.delete(collMeet);	
      collMeetReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collMeetReposHist.delete(collMeet);
      collMeetReposHist.flush();
    }
    else {
      collMeetRepos.delete(collMeet);
      collMeetRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CollMeet> collMeet, TitaVo... titaVo) throws DBException {
    if (collMeet == null || collMeet.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CollMeet t : collMeet){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      collMeet = collMeetReposDay.saveAll(collMeet);	
      collMeetReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collMeet = collMeetReposMon.saveAll(collMeet);	
      collMeetReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collMeet = collMeetReposHist.saveAll(collMeet);
      collMeetReposHist.flush();
    }
    else {
      collMeet = collMeetRepos.saveAll(collMeet);
      collMeetRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CollMeet> collMeet, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (collMeet == null || collMeet.size() == 0)
      throw new DBException(6);

    for (CollMeet t : collMeet) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      collMeet = collMeetReposDay.saveAll(collMeet);	
      collMeetReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collMeet = collMeetReposMon.saveAll(collMeet);	
      collMeetReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collMeet = collMeetReposHist.saveAll(collMeet);
      collMeetReposHist.flush();
    }
    else {
      collMeet = collMeetRepos.saveAll(collMeet);
      collMeetRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CollMeet> collMeet, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (collMeet == null || collMeet.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      collMeetReposDay.deleteAll(collMeet);	
      collMeetReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collMeetReposMon.deleteAll(collMeet);	
      collMeetReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collMeetReposHist.deleteAll(collMeet);
      collMeetReposHist.flush();
    }
    else {
      collMeetRepos.deleteAll(collMeet);
      collMeetRepos.flush();
    }
  }

}
