package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.st1.itx.db.domain.BankRelationFamily;
import com.st1.itx.db.domain.BankRelationFamilyId;
import com.st1.itx.db.repository.online.BankRelationFamilyRepository;
import com.st1.itx.db.repository.day.BankRelationFamilyRepositoryDay;
import com.st1.itx.db.repository.mon.BankRelationFamilyRepositoryMon;
import com.st1.itx.db.repository.hist.BankRelationFamilyRepositoryHist;
import com.st1.itx.db.service.BankRelationFamilyService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("bankRelationFamilyService")
@Repository
public class BankRelationFamilyServiceImpl implements BankRelationFamilyService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(BankRelationFamilyServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private BankRelationFamilyRepository bankRelationFamilyRepos;

  @Autowired
  private BankRelationFamilyRepositoryDay bankRelationFamilyReposDay;

  @Autowired
  private BankRelationFamilyRepositoryMon bankRelationFamilyReposMon;

  @Autowired
  private BankRelationFamilyRepositoryHist bankRelationFamilyReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(bankRelationFamilyRepos);
    org.junit.Assert.assertNotNull(bankRelationFamilyReposDay);
    org.junit.Assert.assertNotNull(bankRelationFamilyReposMon);
    org.junit.Assert.assertNotNull(bankRelationFamilyReposHist);
  }

  @Override
  public BankRelationFamily findById(BankRelationFamilyId bankRelationFamilyId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + bankRelationFamilyId);
    Optional<BankRelationFamily> bankRelationFamily = null;
    if (dbName.equals(ContentName.onDay))
      bankRelationFamily = bankRelationFamilyReposDay.findById(bankRelationFamilyId);
    else if (dbName.equals(ContentName.onMon))
      bankRelationFamily = bankRelationFamilyReposMon.findById(bankRelationFamilyId);
    else if (dbName.equals(ContentName.onHist))
      bankRelationFamily = bankRelationFamilyReposHist.findById(bankRelationFamilyId);
    else 
      bankRelationFamily = bankRelationFamilyRepos.findById(bankRelationFamilyId);
    BankRelationFamily obj = bankRelationFamily.isPresent() ? bankRelationFamily.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<BankRelationFamily> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankRelationFamily> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustName", "CustId", "RelationId"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = bankRelationFamilyReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankRelationFamilyReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankRelationFamilyReposHist.findAll(pageable);
    else 
      slice = bankRelationFamilyRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankRelationFamily> findRelationIdEq(String relationId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankRelationFamily> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findRelationIdEq " + dbName + " : " + "relationId_0 : " + relationId_0);
    if (dbName.equals(ContentName.onDay))
      slice = bankRelationFamilyReposDay.findAllByRelationIdIs(relationId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankRelationFamilyReposMon.findAllByRelationIdIs(relationId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankRelationFamilyReposHist.findAllByRelationIdIs(relationId_0, pageable);
    else 
      slice = bankRelationFamilyRepos.findAllByRelationIdIs(relationId_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public BankRelationFamily holdById(BankRelationFamilyId bankRelationFamilyId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + bankRelationFamilyId);
    Optional<BankRelationFamily> bankRelationFamily = null;
    if (dbName.equals(ContentName.onDay))
      bankRelationFamily = bankRelationFamilyReposDay.findByBankRelationFamilyId(bankRelationFamilyId);
    else if (dbName.equals(ContentName.onMon))
      bankRelationFamily = bankRelationFamilyReposMon.findByBankRelationFamilyId(bankRelationFamilyId);
    else if (dbName.equals(ContentName.onHist))
      bankRelationFamily = bankRelationFamilyReposHist.findByBankRelationFamilyId(bankRelationFamilyId);
    else 
      bankRelationFamily = bankRelationFamilyRepos.findByBankRelationFamilyId(bankRelationFamilyId);
    return bankRelationFamily.isPresent() ? bankRelationFamily.get() : null;
  }

  @Override
  public BankRelationFamily holdById(BankRelationFamily bankRelationFamily, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + bankRelationFamily.getBankRelationFamilyId());
    Optional<BankRelationFamily> bankRelationFamilyT = null;
    if (dbName.equals(ContentName.onDay))
      bankRelationFamilyT = bankRelationFamilyReposDay.findByBankRelationFamilyId(bankRelationFamily.getBankRelationFamilyId());
    else if (dbName.equals(ContentName.onMon))
      bankRelationFamilyT = bankRelationFamilyReposMon.findByBankRelationFamilyId(bankRelationFamily.getBankRelationFamilyId());
    else if (dbName.equals(ContentName.onHist))
      bankRelationFamilyT = bankRelationFamilyReposHist.findByBankRelationFamilyId(bankRelationFamily.getBankRelationFamilyId());
    else 
      bankRelationFamilyT = bankRelationFamilyRepos.findByBankRelationFamilyId(bankRelationFamily.getBankRelationFamilyId());
    return bankRelationFamilyT.isPresent() ? bankRelationFamilyT.get() : null;
  }

  @Override
  public BankRelationFamily insert(BankRelationFamily bankRelationFamily, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + bankRelationFamily.getBankRelationFamilyId());
    if (this.findById(bankRelationFamily.getBankRelationFamilyId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      bankRelationFamily.setCreateEmpNo(empNot);

    if(bankRelationFamily.getLastUpdateEmpNo() == null || bankRelationFamily.getLastUpdateEmpNo().isEmpty())
      bankRelationFamily.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return bankRelationFamilyReposDay.saveAndFlush(bankRelationFamily);	
    else if (dbName.equals(ContentName.onMon))
      return bankRelationFamilyReposMon.saveAndFlush(bankRelationFamily);
    else if (dbName.equals(ContentName.onHist))
      return bankRelationFamilyReposHist.saveAndFlush(bankRelationFamily);
    else 
    return bankRelationFamilyRepos.saveAndFlush(bankRelationFamily);
  }

  @Override
  public BankRelationFamily update(BankRelationFamily bankRelationFamily, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + bankRelationFamily.getBankRelationFamilyId());
    if (!empNot.isEmpty())
      bankRelationFamily.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return bankRelationFamilyReposDay.saveAndFlush(bankRelationFamily);	
    else if (dbName.equals(ContentName.onMon))
      return bankRelationFamilyReposMon.saveAndFlush(bankRelationFamily);
    else if (dbName.equals(ContentName.onHist))
      return bankRelationFamilyReposHist.saveAndFlush(bankRelationFamily);
    else 
    return bankRelationFamilyRepos.saveAndFlush(bankRelationFamily);
  }

  @Override
  public BankRelationFamily update2(BankRelationFamily bankRelationFamily, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + bankRelationFamily.getBankRelationFamilyId());
    if (!empNot.isEmpty())
      bankRelationFamily.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      bankRelationFamilyReposDay.saveAndFlush(bankRelationFamily);	
    else if (dbName.equals(ContentName.onMon))
      bankRelationFamilyReposMon.saveAndFlush(bankRelationFamily);
    else if (dbName.equals(ContentName.onHist))
        bankRelationFamilyReposHist.saveAndFlush(bankRelationFamily);
    else 
      bankRelationFamilyRepos.saveAndFlush(bankRelationFamily);	
    return this.findById(bankRelationFamily.getBankRelationFamilyId());
  }

  @Override
  public void delete(BankRelationFamily bankRelationFamily, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + bankRelationFamily.getBankRelationFamilyId());
    if (dbName.equals(ContentName.onDay)) {
      bankRelationFamilyReposDay.delete(bankRelationFamily);	
      bankRelationFamilyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRelationFamilyReposMon.delete(bankRelationFamily);	
      bankRelationFamilyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRelationFamilyReposHist.delete(bankRelationFamily);
      bankRelationFamilyReposHist.flush();
    }
    else {
      bankRelationFamilyRepos.delete(bankRelationFamily);
      bankRelationFamilyRepos.flush();
    }
   }

  @Override
  public void insertAll(List<BankRelationFamily> bankRelationFamily, TitaVo... titaVo) throws DBException {
    if (bankRelationFamily == null || bankRelationFamily.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (BankRelationFamily t : bankRelationFamily){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      bankRelationFamily = bankRelationFamilyReposDay.saveAll(bankRelationFamily);	
      bankRelationFamilyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRelationFamily = bankRelationFamilyReposMon.saveAll(bankRelationFamily);	
      bankRelationFamilyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRelationFamily = bankRelationFamilyReposHist.saveAll(bankRelationFamily);
      bankRelationFamilyReposHist.flush();
    }
    else {
      bankRelationFamily = bankRelationFamilyRepos.saveAll(bankRelationFamily);
      bankRelationFamilyRepos.flush();
    }
    }

  @Override
  public void updateAll(List<BankRelationFamily> bankRelationFamily, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (bankRelationFamily == null || bankRelationFamily.size() == 0)
      throw new DBException(6);

    for (BankRelationFamily t : bankRelationFamily) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      bankRelationFamily = bankRelationFamilyReposDay.saveAll(bankRelationFamily);	
      bankRelationFamilyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRelationFamily = bankRelationFamilyReposMon.saveAll(bankRelationFamily);	
      bankRelationFamilyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRelationFamily = bankRelationFamilyReposHist.saveAll(bankRelationFamily);
      bankRelationFamilyReposHist.flush();
    }
    else {
      bankRelationFamily = bankRelationFamilyRepos.saveAll(bankRelationFamily);
      bankRelationFamilyRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<BankRelationFamily> bankRelationFamily, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (bankRelationFamily == null || bankRelationFamily.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      bankRelationFamilyReposDay.deleteAll(bankRelationFamily);	
      bankRelationFamilyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRelationFamilyReposMon.deleteAll(bankRelationFamily);	
      bankRelationFamilyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRelationFamilyReposHist.deleteAll(bankRelationFamily);
      bankRelationFamilyReposHist.flush();
    }
    else {
      bankRelationFamilyRepos.deleteAll(bankRelationFamily);
      bankRelationFamilyRepos.flush();
    }
  }

}
