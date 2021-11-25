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
import com.st1.itx.db.domain.BankRelationCompany;
import com.st1.itx.db.domain.BankRelationCompanyId;
import com.st1.itx.db.repository.online.BankRelationCompanyRepository;
import com.st1.itx.db.repository.day.BankRelationCompanyRepositoryDay;
import com.st1.itx.db.repository.mon.BankRelationCompanyRepositoryMon;
import com.st1.itx.db.repository.hist.BankRelationCompanyRepositoryHist;
import com.st1.itx.db.service.BankRelationCompanyService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("bankRelationCompanyService")
@Repository
public class BankRelationCompanyServiceImpl extends ASpringJpaParm implements BankRelationCompanyService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private BankRelationCompanyRepository bankRelationCompanyRepos;

  @Autowired
  private BankRelationCompanyRepositoryDay bankRelationCompanyReposDay;

  @Autowired
  private BankRelationCompanyRepositoryMon bankRelationCompanyReposMon;

  @Autowired
  private BankRelationCompanyRepositoryHist bankRelationCompanyReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(bankRelationCompanyRepos);
    org.junit.Assert.assertNotNull(bankRelationCompanyReposDay);
    org.junit.Assert.assertNotNull(bankRelationCompanyReposMon);
    org.junit.Assert.assertNotNull(bankRelationCompanyReposHist);
  }

  @Override
  public BankRelationCompany findById(BankRelationCompanyId bankRelationCompanyId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + bankRelationCompanyId);
    Optional<BankRelationCompany> bankRelationCompany = null;
    if (dbName.equals(ContentName.onDay))
      bankRelationCompany = bankRelationCompanyReposDay.findById(bankRelationCompanyId);
    else if (dbName.equals(ContentName.onMon))
      bankRelationCompany = bankRelationCompanyReposMon.findById(bankRelationCompanyId);
    else if (dbName.equals(ContentName.onHist))
      bankRelationCompany = bankRelationCompanyReposHist.findById(bankRelationCompanyId);
    else 
      bankRelationCompany = bankRelationCompanyRepos.findById(bankRelationCompanyId);
    BankRelationCompany obj = bankRelationCompany.isPresent() ? bankRelationCompany.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<BankRelationCompany> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankRelationCompany> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustName", "CustId", "CompanyId"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustName", "CustId", "CompanyId"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = bankRelationCompanyReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankRelationCompanyReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankRelationCompanyReposHist.findAll(pageable);
    else 
      slice = bankRelationCompanyRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankRelationCompany> findCompanyIdEq(String companyId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankRelationCompany> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCompanyIdEq " + dbName + " : " + "companyId_0 : " + companyId_0);
    if (dbName.equals(ContentName.onDay))
      slice = bankRelationCompanyReposDay.findAllByCompanyIdIs(companyId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankRelationCompanyReposMon.findAllByCompanyIdIs(companyId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankRelationCompanyReposHist.findAllByCompanyIdIs(companyId_0, pageable);
    else 
      slice = bankRelationCompanyRepos.findAllByCompanyIdIs(companyId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public BankRelationCompany custIdFirst(String custId_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("custIdFirst " + dbName + " : " + "custId_0 : " + custId_0);
    Optional<BankRelationCompany> bankRelationCompanyT = null;
    if (dbName.equals(ContentName.onDay))
      bankRelationCompanyT = bankRelationCompanyReposDay.findTopByCustIdIs(custId_0);
    else if (dbName.equals(ContentName.onMon))
      bankRelationCompanyT = bankRelationCompanyReposMon.findTopByCustIdIs(custId_0);
    else if (dbName.equals(ContentName.onHist))
      bankRelationCompanyT = bankRelationCompanyReposHist.findTopByCustIdIs(custId_0);
    else 
      bankRelationCompanyT = bankRelationCompanyRepos.findTopByCustIdIs(custId_0);

    return bankRelationCompanyT.isPresent() ? bankRelationCompanyT.get() : null;
  }

  @Override
  public BankRelationCompany holdById(BankRelationCompanyId bankRelationCompanyId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + bankRelationCompanyId);
    Optional<BankRelationCompany> bankRelationCompany = null;
    if (dbName.equals(ContentName.onDay))
      bankRelationCompany = bankRelationCompanyReposDay.findByBankRelationCompanyId(bankRelationCompanyId);
    else if (dbName.equals(ContentName.onMon))
      bankRelationCompany = bankRelationCompanyReposMon.findByBankRelationCompanyId(bankRelationCompanyId);
    else if (dbName.equals(ContentName.onHist))
      bankRelationCompany = bankRelationCompanyReposHist.findByBankRelationCompanyId(bankRelationCompanyId);
    else 
      bankRelationCompany = bankRelationCompanyRepos.findByBankRelationCompanyId(bankRelationCompanyId);
    return bankRelationCompany.isPresent() ? bankRelationCompany.get() : null;
  }

  @Override
  public BankRelationCompany holdById(BankRelationCompany bankRelationCompany, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + bankRelationCompany.getBankRelationCompanyId());
    Optional<BankRelationCompany> bankRelationCompanyT = null;
    if (dbName.equals(ContentName.onDay))
      bankRelationCompanyT = bankRelationCompanyReposDay.findByBankRelationCompanyId(bankRelationCompany.getBankRelationCompanyId());
    else if (dbName.equals(ContentName.onMon))
      bankRelationCompanyT = bankRelationCompanyReposMon.findByBankRelationCompanyId(bankRelationCompany.getBankRelationCompanyId());
    else if (dbName.equals(ContentName.onHist))
      bankRelationCompanyT = bankRelationCompanyReposHist.findByBankRelationCompanyId(bankRelationCompany.getBankRelationCompanyId());
    else 
      bankRelationCompanyT = bankRelationCompanyRepos.findByBankRelationCompanyId(bankRelationCompany.getBankRelationCompanyId());
    return bankRelationCompanyT.isPresent() ? bankRelationCompanyT.get() : null;
  }

  @Override
  public BankRelationCompany insert(BankRelationCompany bankRelationCompany, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + bankRelationCompany.getBankRelationCompanyId());
    if (this.findById(bankRelationCompany.getBankRelationCompanyId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      bankRelationCompany.setCreateEmpNo(empNot);

    if(bankRelationCompany.getLastUpdateEmpNo() == null || bankRelationCompany.getLastUpdateEmpNo().isEmpty())
      bankRelationCompany.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return bankRelationCompanyReposDay.saveAndFlush(bankRelationCompany);	
    else if (dbName.equals(ContentName.onMon))
      return bankRelationCompanyReposMon.saveAndFlush(bankRelationCompany);
    else if (dbName.equals(ContentName.onHist))
      return bankRelationCompanyReposHist.saveAndFlush(bankRelationCompany);
    else 
    return bankRelationCompanyRepos.saveAndFlush(bankRelationCompany);
  }

  @Override
  public BankRelationCompany update(BankRelationCompany bankRelationCompany, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + bankRelationCompany.getBankRelationCompanyId());
    if (!empNot.isEmpty())
      bankRelationCompany.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return bankRelationCompanyReposDay.saveAndFlush(bankRelationCompany);	
    else if (dbName.equals(ContentName.onMon))
      return bankRelationCompanyReposMon.saveAndFlush(bankRelationCompany);
    else if (dbName.equals(ContentName.onHist))
      return bankRelationCompanyReposHist.saveAndFlush(bankRelationCompany);
    else 
    return bankRelationCompanyRepos.saveAndFlush(bankRelationCompany);
  }

  @Override
  public BankRelationCompany update2(BankRelationCompany bankRelationCompany, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + bankRelationCompany.getBankRelationCompanyId());
    if (!empNot.isEmpty())
      bankRelationCompany.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      bankRelationCompanyReposDay.saveAndFlush(bankRelationCompany);	
    else if (dbName.equals(ContentName.onMon))
      bankRelationCompanyReposMon.saveAndFlush(bankRelationCompany);
    else if (dbName.equals(ContentName.onHist))
        bankRelationCompanyReposHist.saveAndFlush(bankRelationCompany);
    else 
      bankRelationCompanyRepos.saveAndFlush(bankRelationCompany);	
    return this.findById(bankRelationCompany.getBankRelationCompanyId());
  }

  @Override
  public void delete(BankRelationCompany bankRelationCompany, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + bankRelationCompany.getBankRelationCompanyId());
    if (dbName.equals(ContentName.onDay)) {
      bankRelationCompanyReposDay.delete(bankRelationCompany);	
      bankRelationCompanyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRelationCompanyReposMon.delete(bankRelationCompany);	
      bankRelationCompanyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRelationCompanyReposHist.delete(bankRelationCompany);
      bankRelationCompanyReposHist.flush();
    }
    else {
      bankRelationCompanyRepos.delete(bankRelationCompany);
      bankRelationCompanyRepos.flush();
    }
   }

  @Override
  public void insertAll(List<BankRelationCompany> bankRelationCompany, TitaVo... titaVo) throws DBException {
    if (bankRelationCompany == null || bankRelationCompany.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (BankRelationCompany t : bankRelationCompany){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      bankRelationCompany = bankRelationCompanyReposDay.saveAll(bankRelationCompany);	
      bankRelationCompanyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRelationCompany = bankRelationCompanyReposMon.saveAll(bankRelationCompany);	
      bankRelationCompanyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRelationCompany = bankRelationCompanyReposHist.saveAll(bankRelationCompany);
      bankRelationCompanyReposHist.flush();
    }
    else {
      bankRelationCompany = bankRelationCompanyRepos.saveAll(bankRelationCompany);
      bankRelationCompanyRepos.flush();
    }
    }

  @Override
  public void updateAll(List<BankRelationCompany> bankRelationCompany, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (bankRelationCompany == null || bankRelationCompany.size() == 0)
      throw new DBException(6);

    for (BankRelationCompany t : bankRelationCompany) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      bankRelationCompany = bankRelationCompanyReposDay.saveAll(bankRelationCompany);	
      bankRelationCompanyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRelationCompany = bankRelationCompanyReposMon.saveAll(bankRelationCompany);	
      bankRelationCompanyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRelationCompany = bankRelationCompanyReposHist.saveAll(bankRelationCompany);
      bankRelationCompanyReposHist.flush();
    }
    else {
      bankRelationCompany = bankRelationCompanyRepos.saveAll(bankRelationCompany);
      bankRelationCompanyRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<BankRelationCompany> bankRelationCompany, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (bankRelationCompany == null || bankRelationCompany.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      bankRelationCompanyReposDay.deleteAll(bankRelationCompany);	
      bankRelationCompanyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRelationCompanyReposMon.deleteAll(bankRelationCompany);	
      bankRelationCompanyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRelationCompanyReposHist.deleteAll(bankRelationCompany);
      bankRelationCompanyReposHist.flush();
    }
    else {
      bankRelationCompanyRepos.deleteAll(bankRelationCompany);
      bankRelationCompanyRepos.flush();
    }
  }

}
