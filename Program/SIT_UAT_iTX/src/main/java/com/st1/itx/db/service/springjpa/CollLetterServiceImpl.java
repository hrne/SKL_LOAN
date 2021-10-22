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
import com.st1.itx.db.domain.CollLetter;
import com.st1.itx.db.domain.CollLetterId;
import com.st1.itx.db.repository.online.CollLetterRepository;
import com.st1.itx.db.repository.day.CollLetterRepositoryDay;
import com.st1.itx.db.repository.mon.CollLetterRepositoryMon;
import com.st1.itx.db.repository.hist.CollLetterRepositoryHist;
import com.st1.itx.db.service.CollLetterService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("collLetterService")
@Repository
public class CollLetterServiceImpl extends ASpringJpaParm implements CollLetterService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CollLetterRepository collLetterRepos;

  @Autowired
  private CollLetterRepositoryDay collLetterReposDay;

  @Autowired
  private CollLetterRepositoryMon collLetterReposMon;

  @Autowired
  private CollLetterRepositoryHist collLetterReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(collLetterRepos);
    org.junit.Assert.assertNotNull(collLetterReposDay);
    org.junit.Assert.assertNotNull(collLetterReposMon);
    org.junit.Assert.assertNotNull(collLetterReposHist);
  }

  @Override
  public CollLetter findById(CollLetterId collLetterId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + collLetterId);
    Optional<CollLetter> collLetter = null;
    if (dbName.equals(ContentName.onDay))
      collLetter = collLetterReposDay.findById(collLetterId);
    else if (dbName.equals(ContentName.onMon))
      collLetter = collLetterReposMon.findById(collLetterId);
    else if (dbName.equals(ContentName.onHist))
      collLetter = collLetterReposHist.findById(collLetterId);
    else 
      collLetter = collLetterRepos.findById(collLetterId);
    CollLetter obj = collLetter.isPresent() ? collLetter.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CollLetter> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollLetter> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CaseCode", "CustNo", "FacmNo", "AcDate", "TitaTlrNo", "TitaTxtNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CaseCode", "CustNo", "FacmNo", "AcDate", "TitaTlrNo", "TitaTxtNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = collLetterReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collLetterReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collLetterReposHist.findAll(pageable);
    else 
      slice = collLetterRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CollLetter> telTimeBetween(int mailDate_0, int mailDate_1, String caseCode_2, int custNo_3, int facmNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollLetter> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("telTimeBetween " + dbName + " : " + "mailDate_0 : " + mailDate_0 + " mailDate_1 : " +  mailDate_1 + " caseCode_2 : " +  caseCode_2 + " custNo_3 : " +  custNo_3 + " facmNo_4 : " +  facmNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = collLetterReposDay.findAllByMailDateGreaterThanEqualAndMailDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMailDateDesc(mailDate_0, mailDate_1, caseCode_2, custNo_3, facmNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collLetterReposMon.findAllByMailDateGreaterThanEqualAndMailDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMailDateDesc(mailDate_0, mailDate_1, caseCode_2, custNo_3, facmNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collLetterReposHist.findAllByMailDateGreaterThanEqualAndMailDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMailDateDesc(mailDate_0, mailDate_1, caseCode_2, custNo_3, facmNo_4, pageable);
    else 
      slice = collLetterRepos.findAllByMailDateGreaterThanEqualAndMailDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMailDateDesc(mailDate_0, mailDate_1, caseCode_2, custNo_3, facmNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CollLetter> findSameCust(String caseCode_0, int custNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollLetter> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findSameCust " + dbName + " : " + "caseCode_0 : " + caseCode_0 + " custNo_1 : " +  custNo_1 + " facmNo_2 : " +  facmNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = collLetterReposDay.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMailDateDesc(caseCode_0, custNo_1, facmNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collLetterReposMon.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMailDateDesc(caseCode_0, custNo_1, facmNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collLetterReposHist.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMailDateDesc(caseCode_0, custNo_1, facmNo_2, pageable);
    else 
      slice = collLetterRepos.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMailDateDesc(caseCode_0, custNo_1, facmNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CollLetter> withoutFacmNo(int mailDate_0, int mailDate_1, String caseCode_2, int custNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollLetter> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("withoutFacmNo " + dbName + " : " + "mailDate_0 : " + mailDate_0 + " mailDate_1 : " +  mailDate_1 + " caseCode_2 : " +  caseCode_2 + " custNo_3 : " +  custNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = collLetterReposDay.findAllByMailDateGreaterThanEqualAndMailDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByMailDateDesc(mailDate_0, mailDate_1, caseCode_2, custNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collLetterReposMon.findAllByMailDateGreaterThanEqualAndMailDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByMailDateDesc(mailDate_0, mailDate_1, caseCode_2, custNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collLetterReposHist.findAllByMailDateGreaterThanEqualAndMailDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByMailDateDesc(mailDate_0, mailDate_1, caseCode_2, custNo_3, pageable);
    else 
      slice = collLetterRepos.findAllByMailDateGreaterThanEqualAndMailDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByMailDateDesc(mailDate_0, mailDate_1, caseCode_2, custNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CollLetter> withoutFacmNoAll(String caseCode_0, int custNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollLetter> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("withoutFacmNoAll " + dbName + " : " + "caseCode_0 : " + caseCode_0 + " custNo_1 : " +  custNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = collLetterReposDay.findAllByCaseCodeIsAndCustNoIsOrderByMailDateDesc(caseCode_0, custNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collLetterReposMon.findAllByCaseCodeIsAndCustNoIsOrderByMailDateDesc(caseCode_0, custNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collLetterReposHist.findAllByCaseCodeIsAndCustNoIsOrderByMailDateDesc(caseCode_0, custNo_1, pageable);
    else 
      slice = collLetterRepos.findAllByCaseCodeIsAndCustNoIsOrderByMailDateDesc(caseCode_0, custNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CollLetter holdById(CollLetterId collLetterId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + collLetterId);
    Optional<CollLetter> collLetter = null;
    if (dbName.equals(ContentName.onDay))
      collLetter = collLetterReposDay.findByCollLetterId(collLetterId);
    else if (dbName.equals(ContentName.onMon))
      collLetter = collLetterReposMon.findByCollLetterId(collLetterId);
    else if (dbName.equals(ContentName.onHist))
      collLetter = collLetterReposHist.findByCollLetterId(collLetterId);
    else 
      collLetter = collLetterRepos.findByCollLetterId(collLetterId);
    return collLetter.isPresent() ? collLetter.get() : null;
  }

  @Override
  public CollLetter holdById(CollLetter collLetter, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + collLetter.getCollLetterId());
    Optional<CollLetter> collLetterT = null;
    if (dbName.equals(ContentName.onDay))
      collLetterT = collLetterReposDay.findByCollLetterId(collLetter.getCollLetterId());
    else if (dbName.equals(ContentName.onMon))
      collLetterT = collLetterReposMon.findByCollLetterId(collLetter.getCollLetterId());
    else if (dbName.equals(ContentName.onHist))
      collLetterT = collLetterReposHist.findByCollLetterId(collLetter.getCollLetterId());
    else 
      collLetterT = collLetterRepos.findByCollLetterId(collLetter.getCollLetterId());
    return collLetterT.isPresent() ? collLetterT.get() : null;
  }

  @Override
  public CollLetter insert(CollLetter collLetter, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + collLetter.getCollLetterId());
    if (this.findById(collLetter.getCollLetterId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      collLetter.setCreateEmpNo(empNot);

    if(collLetter.getLastUpdateEmpNo() == null || collLetter.getLastUpdateEmpNo().isEmpty())
      collLetter.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return collLetterReposDay.saveAndFlush(collLetter);	
    else if (dbName.equals(ContentName.onMon))
      return collLetterReposMon.saveAndFlush(collLetter);
    else if (dbName.equals(ContentName.onHist))
      return collLetterReposHist.saveAndFlush(collLetter);
    else 
    return collLetterRepos.saveAndFlush(collLetter);
  }

  @Override
  public CollLetter update(CollLetter collLetter, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + collLetter.getCollLetterId());
    if (!empNot.isEmpty())
      collLetter.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return collLetterReposDay.saveAndFlush(collLetter);	
    else if (dbName.equals(ContentName.onMon))
      return collLetterReposMon.saveAndFlush(collLetter);
    else if (dbName.equals(ContentName.onHist))
      return collLetterReposHist.saveAndFlush(collLetter);
    else 
    return collLetterRepos.saveAndFlush(collLetter);
  }

  @Override
  public CollLetter update2(CollLetter collLetter, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + collLetter.getCollLetterId());
    if (!empNot.isEmpty())
      collLetter.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      collLetterReposDay.saveAndFlush(collLetter);	
    else if (dbName.equals(ContentName.onMon))
      collLetterReposMon.saveAndFlush(collLetter);
    else if (dbName.equals(ContentName.onHist))
        collLetterReposHist.saveAndFlush(collLetter);
    else 
      collLetterRepos.saveAndFlush(collLetter);	
    return this.findById(collLetter.getCollLetterId());
  }

  @Override
  public void delete(CollLetter collLetter, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + collLetter.getCollLetterId());
    if (dbName.equals(ContentName.onDay)) {
      collLetterReposDay.delete(collLetter);	
      collLetterReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collLetterReposMon.delete(collLetter);	
      collLetterReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collLetterReposHist.delete(collLetter);
      collLetterReposHist.flush();
    }
    else {
      collLetterRepos.delete(collLetter);
      collLetterRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CollLetter> collLetter, TitaVo... titaVo) throws DBException {
    if (collLetter == null || collLetter.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (CollLetter t : collLetter){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      collLetter = collLetterReposDay.saveAll(collLetter);	
      collLetterReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collLetter = collLetterReposMon.saveAll(collLetter);	
      collLetterReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collLetter = collLetterReposHist.saveAll(collLetter);
      collLetterReposHist.flush();
    }
    else {
      collLetter = collLetterRepos.saveAll(collLetter);
      collLetterRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CollLetter> collLetter, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (collLetter == null || collLetter.size() == 0)
      throw new DBException(6);

    for (CollLetter t : collLetter) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      collLetter = collLetterReposDay.saveAll(collLetter);	
      collLetterReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collLetter = collLetterReposMon.saveAll(collLetter);	
      collLetterReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collLetter = collLetterReposHist.saveAll(collLetter);
      collLetterReposHist.flush();
    }
    else {
      collLetter = collLetterRepos.saveAll(collLetter);
      collLetterRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CollLetter> collLetter, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (collLetter == null || collLetter.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      collLetterReposDay.deleteAll(collLetter);	
      collLetterReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collLetterReposMon.deleteAll(collLetter);	
      collLetterReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collLetterReposHist.deleteAll(collLetter);
      collLetterReposHist.flush();
    }
    else {
      collLetterRepos.deleteAll(collLetter);
      collLetterRepos.flush();
    }
  }

}
