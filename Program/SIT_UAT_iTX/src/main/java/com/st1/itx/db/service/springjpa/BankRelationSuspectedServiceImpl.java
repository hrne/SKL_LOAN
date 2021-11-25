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
import com.st1.itx.db.domain.BankRelationSuspected;
import com.st1.itx.db.domain.BankRelationSuspectedId;
import com.st1.itx.db.repository.online.BankRelationSuspectedRepository;
import com.st1.itx.db.repository.day.BankRelationSuspectedRepositoryDay;
import com.st1.itx.db.repository.mon.BankRelationSuspectedRepositoryMon;
import com.st1.itx.db.repository.hist.BankRelationSuspectedRepositoryHist;
import com.st1.itx.db.service.BankRelationSuspectedService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("bankRelationSuspectedService")
@Repository
public class BankRelationSuspectedServiceImpl extends ASpringJpaParm implements BankRelationSuspectedService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private BankRelationSuspectedRepository bankRelationSuspectedRepos;

  @Autowired
  private BankRelationSuspectedRepositoryDay bankRelationSuspectedReposDay;

  @Autowired
  private BankRelationSuspectedRepositoryMon bankRelationSuspectedReposMon;

  @Autowired
  private BankRelationSuspectedRepositoryHist bankRelationSuspectedReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(bankRelationSuspectedRepos);
    org.junit.Assert.assertNotNull(bankRelationSuspectedReposDay);
    org.junit.Assert.assertNotNull(bankRelationSuspectedReposMon);
    org.junit.Assert.assertNotNull(bankRelationSuspectedReposHist);
  }

  @Override
  public BankRelationSuspected findById(BankRelationSuspectedId bankRelationSuspectedId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + bankRelationSuspectedId);
    Optional<BankRelationSuspected> bankRelationSuspected = null;
    if (dbName.equals(ContentName.onDay))
      bankRelationSuspected = bankRelationSuspectedReposDay.findById(bankRelationSuspectedId);
    else if (dbName.equals(ContentName.onMon))
      bankRelationSuspected = bankRelationSuspectedReposMon.findById(bankRelationSuspectedId);
    else if (dbName.equals(ContentName.onHist))
      bankRelationSuspected = bankRelationSuspectedReposHist.findById(bankRelationSuspectedId);
    else 
      bankRelationSuspected = bankRelationSuspectedRepos.findById(bankRelationSuspectedId);
    BankRelationSuspected obj = bankRelationSuspected.isPresent() ? bankRelationSuspected.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<BankRelationSuspected> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankRelationSuspected> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "RepCusName", "CustId"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "RepCusName", "CustId"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = bankRelationSuspectedReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankRelationSuspectedReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankRelationSuspectedReposHist.findAll(pageable);
    else 
      slice = bankRelationSuspectedRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankRelationSuspected> RepCusNameEq(String repCusName_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankRelationSuspected> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("RepCusNameEq " + dbName + " : " + "repCusName_0 : " + repCusName_0);
    if (dbName.equals(ContentName.onDay))
      slice = bankRelationSuspectedReposDay.findAllByRepCusNameIs(repCusName_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankRelationSuspectedReposMon.findAllByRepCusNameIs(repCusName_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankRelationSuspectedReposHist.findAllByRepCusNameIs(repCusName_0, pageable);
    else 
      slice = bankRelationSuspectedRepos.findAllByRepCusNameIs(repCusName_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public BankRelationSuspected holdById(BankRelationSuspectedId bankRelationSuspectedId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + bankRelationSuspectedId);
    Optional<BankRelationSuspected> bankRelationSuspected = null;
    if (dbName.equals(ContentName.onDay))
      bankRelationSuspected = bankRelationSuspectedReposDay.findByBankRelationSuspectedId(bankRelationSuspectedId);
    else if (dbName.equals(ContentName.onMon))
      bankRelationSuspected = bankRelationSuspectedReposMon.findByBankRelationSuspectedId(bankRelationSuspectedId);
    else if (dbName.equals(ContentName.onHist))
      bankRelationSuspected = bankRelationSuspectedReposHist.findByBankRelationSuspectedId(bankRelationSuspectedId);
    else 
      bankRelationSuspected = bankRelationSuspectedRepos.findByBankRelationSuspectedId(bankRelationSuspectedId);
    return bankRelationSuspected.isPresent() ? bankRelationSuspected.get() : null;
  }

  @Override
  public BankRelationSuspected holdById(BankRelationSuspected bankRelationSuspected, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + bankRelationSuspected.getBankRelationSuspectedId());
    Optional<BankRelationSuspected> bankRelationSuspectedT = null;
    if (dbName.equals(ContentName.onDay))
      bankRelationSuspectedT = bankRelationSuspectedReposDay.findByBankRelationSuspectedId(bankRelationSuspected.getBankRelationSuspectedId());
    else if (dbName.equals(ContentName.onMon))
      bankRelationSuspectedT = bankRelationSuspectedReposMon.findByBankRelationSuspectedId(bankRelationSuspected.getBankRelationSuspectedId());
    else if (dbName.equals(ContentName.onHist))
      bankRelationSuspectedT = bankRelationSuspectedReposHist.findByBankRelationSuspectedId(bankRelationSuspected.getBankRelationSuspectedId());
    else 
      bankRelationSuspectedT = bankRelationSuspectedRepos.findByBankRelationSuspectedId(bankRelationSuspected.getBankRelationSuspectedId());
    return bankRelationSuspectedT.isPresent() ? bankRelationSuspectedT.get() : null;
  }

  @Override
  public BankRelationSuspected insert(BankRelationSuspected bankRelationSuspected, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + bankRelationSuspected.getBankRelationSuspectedId());
    if (this.findById(bankRelationSuspected.getBankRelationSuspectedId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      bankRelationSuspected.setCreateEmpNo(empNot);

    if(bankRelationSuspected.getLastUpdateEmpNo() == null || bankRelationSuspected.getLastUpdateEmpNo().isEmpty())
      bankRelationSuspected.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return bankRelationSuspectedReposDay.saveAndFlush(bankRelationSuspected);	
    else if (dbName.equals(ContentName.onMon))
      return bankRelationSuspectedReposMon.saveAndFlush(bankRelationSuspected);
    else if (dbName.equals(ContentName.onHist))
      return bankRelationSuspectedReposHist.saveAndFlush(bankRelationSuspected);
    else 
    return bankRelationSuspectedRepos.saveAndFlush(bankRelationSuspected);
  }

  @Override
  public BankRelationSuspected update(BankRelationSuspected bankRelationSuspected, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + bankRelationSuspected.getBankRelationSuspectedId());
    if (!empNot.isEmpty())
      bankRelationSuspected.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return bankRelationSuspectedReposDay.saveAndFlush(bankRelationSuspected);	
    else if (dbName.equals(ContentName.onMon))
      return bankRelationSuspectedReposMon.saveAndFlush(bankRelationSuspected);
    else if (dbName.equals(ContentName.onHist))
      return bankRelationSuspectedReposHist.saveAndFlush(bankRelationSuspected);
    else 
    return bankRelationSuspectedRepos.saveAndFlush(bankRelationSuspected);
  }

  @Override
  public BankRelationSuspected update2(BankRelationSuspected bankRelationSuspected, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + bankRelationSuspected.getBankRelationSuspectedId());
    if (!empNot.isEmpty())
      bankRelationSuspected.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      bankRelationSuspectedReposDay.saveAndFlush(bankRelationSuspected);	
    else if (dbName.equals(ContentName.onMon))
      bankRelationSuspectedReposMon.saveAndFlush(bankRelationSuspected);
    else if (dbName.equals(ContentName.onHist))
        bankRelationSuspectedReposHist.saveAndFlush(bankRelationSuspected);
    else 
      bankRelationSuspectedRepos.saveAndFlush(bankRelationSuspected);	
    return this.findById(bankRelationSuspected.getBankRelationSuspectedId());
  }

  @Override
  public void delete(BankRelationSuspected bankRelationSuspected, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + bankRelationSuspected.getBankRelationSuspectedId());
    if (dbName.equals(ContentName.onDay)) {
      bankRelationSuspectedReposDay.delete(bankRelationSuspected);	
      bankRelationSuspectedReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRelationSuspectedReposMon.delete(bankRelationSuspected);	
      bankRelationSuspectedReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRelationSuspectedReposHist.delete(bankRelationSuspected);
      bankRelationSuspectedReposHist.flush();
    }
    else {
      bankRelationSuspectedRepos.delete(bankRelationSuspected);
      bankRelationSuspectedRepos.flush();
    }
   }

  @Override
  public void insertAll(List<BankRelationSuspected> bankRelationSuspected, TitaVo... titaVo) throws DBException {
    if (bankRelationSuspected == null || bankRelationSuspected.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (BankRelationSuspected t : bankRelationSuspected){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      bankRelationSuspected = bankRelationSuspectedReposDay.saveAll(bankRelationSuspected);	
      bankRelationSuspectedReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRelationSuspected = bankRelationSuspectedReposMon.saveAll(bankRelationSuspected);	
      bankRelationSuspectedReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRelationSuspected = bankRelationSuspectedReposHist.saveAll(bankRelationSuspected);
      bankRelationSuspectedReposHist.flush();
    }
    else {
      bankRelationSuspected = bankRelationSuspectedRepos.saveAll(bankRelationSuspected);
      bankRelationSuspectedRepos.flush();
    }
    }

  @Override
  public void updateAll(List<BankRelationSuspected> bankRelationSuspected, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (bankRelationSuspected == null || bankRelationSuspected.size() == 0)
      throw new DBException(6);

    for (BankRelationSuspected t : bankRelationSuspected) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      bankRelationSuspected = bankRelationSuspectedReposDay.saveAll(bankRelationSuspected);	
      bankRelationSuspectedReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRelationSuspected = bankRelationSuspectedReposMon.saveAll(bankRelationSuspected);	
      bankRelationSuspectedReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRelationSuspected = bankRelationSuspectedReposHist.saveAll(bankRelationSuspected);
      bankRelationSuspectedReposHist.flush();
    }
    else {
      bankRelationSuspected = bankRelationSuspectedRepos.saveAll(bankRelationSuspected);
      bankRelationSuspectedRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<BankRelationSuspected> bankRelationSuspected, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (bankRelationSuspected == null || bankRelationSuspected.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      bankRelationSuspectedReposDay.deleteAll(bankRelationSuspected);	
      bankRelationSuspectedReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRelationSuspectedReposMon.deleteAll(bankRelationSuspected);	
      bankRelationSuspectedReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRelationSuspectedReposHist.deleteAll(bankRelationSuspected);
      bankRelationSuspectedReposHist.flush();
    }
    else {
      bankRelationSuspectedRepos.deleteAll(bankRelationSuspected);
      bankRelationSuspectedRepos.flush();
    }
  }

}
