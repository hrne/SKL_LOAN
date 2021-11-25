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
import com.st1.itx.db.domain.CollRemind;
import com.st1.itx.db.domain.CollRemindId;
import com.st1.itx.db.repository.online.CollRemindRepository;
import com.st1.itx.db.repository.day.CollRemindRepositoryDay;
import com.st1.itx.db.repository.mon.CollRemindRepositoryMon;
import com.st1.itx.db.repository.hist.CollRemindRepositoryHist;
import com.st1.itx.db.service.CollRemindService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("collRemindService")
@Repository
public class CollRemindServiceImpl extends ASpringJpaParm implements CollRemindService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CollRemindRepository collRemindRepos;

  @Autowired
  private CollRemindRepositoryDay collRemindReposDay;

  @Autowired
  private CollRemindRepositoryMon collRemindReposMon;

  @Autowired
  private CollRemindRepositoryHist collRemindReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(collRemindRepos);
    org.junit.Assert.assertNotNull(collRemindReposDay);
    org.junit.Assert.assertNotNull(collRemindReposMon);
    org.junit.Assert.assertNotNull(collRemindReposHist);
  }

  @Override
  public CollRemind findById(CollRemindId collRemindId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + collRemindId);
    Optional<CollRemind> collRemind = null;
    if (dbName.equals(ContentName.onDay))
      collRemind = collRemindReposDay.findById(collRemindId);
    else if (dbName.equals(ContentName.onMon))
      collRemind = collRemindReposMon.findById(collRemindId);
    else if (dbName.equals(ContentName.onHist))
      collRemind = collRemindReposHist.findById(collRemindId);
    else 
      collRemind = collRemindRepos.findById(collRemindId);
    CollRemind obj = collRemind.isPresent() ? collRemind.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CollRemind> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollRemind> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CaseCode", "CustNo", "FacmNo", "AcDate", "TitaTlrNo", "TitaTxtNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CaseCode", "CustNo", "FacmNo", "AcDate", "TitaTlrNo", "TitaTxtNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = collRemindReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collRemindReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collRemindReposHist.findAll(pageable);
    else 
      slice = collRemindRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CollRemind> findCl(String caseCode_0, int custNo_1, int facmNo_2, String condCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollRemind> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCl " + dbName + " : " + "caseCode_0 : " + caseCode_0 + " custNo_1 : " +  custNo_1 + " facmNo_2 : " +  facmNo_2 + " condCode_3 : " +  condCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = collRemindReposDay.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsAndCondCodeIsOrderByRemindDateDesc(caseCode_0, custNo_1, facmNo_2, condCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collRemindReposMon.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsAndCondCodeIsOrderByRemindDateDesc(caseCode_0, custNo_1, facmNo_2, condCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collRemindReposHist.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsAndCondCodeIsOrderByRemindDateDesc(caseCode_0, custNo_1, facmNo_2, condCode_3, pageable);
    else 
      slice = collRemindRepos.findAllByCaseCodeIsAndCustNoIsAndFacmNoIsAndCondCodeIsOrderByRemindDateDesc(caseCode_0, custNo_1, facmNo_2, condCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CollRemind> findWithoutFacm(String caseCode_0, int custNo_1, String condCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollRemind> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findWithoutFacm " + dbName + " : " + "caseCode_0 : " + caseCode_0 + " custNo_1 : " +  custNo_1 + " condCode_2 : " +  condCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = collRemindReposDay.findAllByCaseCodeIsAndCustNoIsAndCondCodeIsOrderByRemindDateDesc(caseCode_0, custNo_1, condCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collRemindReposMon.findAllByCaseCodeIsAndCustNoIsAndCondCodeIsOrderByRemindDateDesc(caseCode_0, custNo_1, condCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collRemindReposHist.findAllByCaseCodeIsAndCustNoIsAndCondCodeIsOrderByRemindDateDesc(caseCode_0, custNo_1, condCode_2, pageable);
    else 
      slice = collRemindRepos.findAllByCaseCodeIsAndCustNoIsAndCondCodeIsOrderByRemindDateDesc(caseCode_0, custNo_1, condCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CollRemind holdById(CollRemindId collRemindId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + collRemindId);
    Optional<CollRemind> collRemind = null;
    if (dbName.equals(ContentName.onDay))
      collRemind = collRemindReposDay.findByCollRemindId(collRemindId);
    else if (dbName.equals(ContentName.onMon))
      collRemind = collRemindReposMon.findByCollRemindId(collRemindId);
    else if (dbName.equals(ContentName.onHist))
      collRemind = collRemindReposHist.findByCollRemindId(collRemindId);
    else 
      collRemind = collRemindRepos.findByCollRemindId(collRemindId);
    return collRemind.isPresent() ? collRemind.get() : null;
  }

  @Override
  public CollRemind holdById(CollRemind collRemind, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + collRemind.getCollRemindId());
    Optional<CollRemind> collRemindT = null;
    if (dbName.equals(ContentName.onDay))
      collRemindT = collRemindReposDay.findByCollRemindId(collRemind.getCollRemindId());
    else if (dbName.equals(ContentName.onMon))
      collRemindT = collRemindReposMon.findByCollRemindId(collRemind.getCollRemindId());
    else if (dbName.equals(ContentName.onHist))
      collRemindT = collRemindReposHist.findByCollRemindId(collRemind.getCollRemindId());
    else 
      collRemindT = collRemindRepos.findByCollRemindId(collRemind.getCollRemindId());
    return collRemindT.isPresent() ? collRemindT.get() : null;
  }

  @Override
  public CollRemind insert(CollRemind collRemind, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + collRemind.getCollRemindId());
    if (this.findById(collRemind.getCollRemindId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      collRemind.setCreateEmpNo(empNot);

    if(collRemind.getLastUpdateEmpNo() == null || collRemind.getLastUpdateEmpNo().isEmpty())
      collRemind.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return collRemindReposDay.saveAndFlush(collRemind);	
    else if (dbName.equals(ContentName.onMon))
      return collRemindReposMon.saveAndFlush(collRemind);
    else if (dbName.equals(ContentName.onHist))
      return collRemindReposHist.saveAndFlush(collRemind);
    else 
    return collRemindRepos.saveAndFlush(collRemind);
  }

  @Override
  public CollRemind update(CollRemind collRemind, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + collRemind.getCollRemindId());
    if (!empNot.isEmpty())
      collRemind.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return collRemindReposDay.saveAndFlush(collRemind);	
    else if (dbName.equals(ContentName.onMon))
      return collRemindReposMon.saveAndFlush(collRemind);
    else if (dbName.equals(ContentName.onHist))
      return collRemindReposHist.saveAndFlush(collRemind);
    else 
    return collRemindRepos.saveAndFlush(collRemind);
  }

  @Override
  public CollRemind update2(CollRemind collRemind, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + collRemind.getCollRemindId());
    if (!empNot.isEmpty())
      collRemind.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      collRemindReposDay.saveAndFlush(collRemind);	
    else if (dbName.equals(ContentName.onMon))
      collRemindReposMon.saveAndFlush(collRemind);
    else if (dbName.equals(ContentName.onHist))
        collRemindReposHist.saveAndFlush(collRemind);
    else 
      collRemindRepos.saveAndFlush(collRemind);	
    return this.findById(collRemind.getCollRemindId());
  }

  @Override
  public void delete(CollRemind collRemind, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + collRemind.getCollRemindId());
    if (dbName.equals(ContentName.onDay)) {
      collRemindReposDay.delete(collRemind);	
      collRemindReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collRemindReposMon.delete(collRemind);	
      collRemindReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collRemindReposHist.delete(collRemind);
      collRemindReposHist.flush();
    }
    else {
      collRemindRepos.delete(collRemind);
      collRemindRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CollRemind> collRemind, TitaVo... titaVo) throws DBException {
    if (collRemind == null || collRemind.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CollRemind t : collRemind){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      collRemind = collRemindReposDay.saveAll(collRemind);	
      collRemindReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collRemind = collRemindReposMon.saveAll(collRemind);	
      collRemindReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collRemind = collRemindReposHist.saveAll(collRemind);
      collRemindReposHist.flush();
    }
    else {
      collRemind = collRemindRepos.saveAll(collRemind);
      collRemindRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CollRemind> collRemind, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (collRemind == null || collRemind.size() == 0)
      throw new DBException(6);

    for (CollRemind t : collRemind) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      collRemind = collRemindReposDay.saveAll(collRemind);	
      collRemindReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collRemind = collRemindReposMon.saveAll(collRemind);	
      collRemindReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collRemind = collRemindReposHist.saveAll(collRemind);
      collRemindReposHist.flush();
    }
    else {
      collRemind = collRemindRepos.saveAll(collRemind);
      collRemindRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CollRemind> collRemind, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (collRemind == null || collRemind.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      collRemindReposDay.deleteAll(collRemind);	
      collRemindReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collRemindReposMon.deleteAll(collRemind);	
      collRemindReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collRemindReposHist.deleteAll(collRemind);
      collRemindReposHist.flush();
    }
    else {
      collRemindRepos.deleteAll(collRemind);
      collRemindRepos.flush();
    }
  }

}
