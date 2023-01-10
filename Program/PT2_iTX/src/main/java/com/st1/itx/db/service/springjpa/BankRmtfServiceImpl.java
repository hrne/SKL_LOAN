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
import com.st1.itx.db.domain.BankRmtf;
import com.st1.itx.db.domain.BankRmtfId;
import com.st1.itx.db.repository.online.BankRmtfRepository;
import com.st1.itx.db.repository.day.BankRmtfRepositoryDay;
import com.st1.itx.db.repository.mon.BankRmtfRepositoryMon;
import com.st1.itx.db.repository.hist.BankRmtfRepositoryHist;
import com.st1.itx.db.service.BankRmtfService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("bankRmtfService")
@Repository
public class BankRmtfServiceImpl extends ASpringJpaParm implements BankRmtfService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private BankRmtfRepository bankRmtfRepos;

  @Autowired
  private BankRmtfRepositoryDay bankRmtfReposDay;

  @Autowired
  private BankRmtfRepositoryMon bankRmtfReposMon;

  @Autowired
  private BankRmtfRepositoryHist bankRmtfReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(bankRmtfRepos);
    org.junit.Assert.assertNotNull(bankRmtfReposDay);
    org.junit.Assert.assertNotNull(bankRmtfReposMon);
    org.junit.Assert.assertNotNull(bankRmtfReposHist);
  }

  @Override
  public BankRmtf findById(BankRmtfId bankRmtfId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + bankRmtfId);
    Optional<BankRmtf> bankRmtf = null;
    if (dbName.equals(ContentName.onDay))
      bankRmtf = bankRmtfReposDay.findById(bankRmtfId);
    else if (dbName.equals(ContentName.onMon))
      bankRmtf = bankRmtfReposMon.findById(bankRmtfId);
    else if (dbName.equals(ContentName.onHist))
      bankRmtf = bankRmtfReposHist.findById(bankRmtfId);
    else 
      bankRmtf = bankRmtfRepos.findById(bankRmtfId);
    BankRmtf obj = bankRmtf.isPresent() ? bankRmtf.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<BankRmtf> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankRmtf> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AcDate", "BatchNo", "DetailSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcDate", "BatchNo", "DetailSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = bankRmtfReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankRmtfReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankRmtfReposHist.findAll(pageable);
    else 
      slice = bankRmtfRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public BankRmtf findTxSeqFirst(int acDate_0, String titaTlrNo_1, String titaTxtNo_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findTxSeqFirst " + dbName + " : " + "acDate_0 : " + acDate_0 + " titaTlrNo_1 : " +  titaTlrNo_1 + " titaTxtNo_2 : " +  titaTxtNo_2);
    Optional<BankRmtf> bankRmtfT = null;
    if (dbName.equals(ContentName.onDay))
      bankRmtfT = bankRmtfReposDay.findTopByAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(acDate_0, titaTlrNo_1, titaTxtNo_2);
    else if (dbName.equals(ContentName.onMon))
      bankRmtfT = bankRmtfReposMon.findTopByAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(acDate_0, titaTlrNo_1, titaTxtNo_2);
    else if (dbName.equals(ContentName.onHist))
      bankRmtfT = bankRmtfReposHist.findTopByAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(acDate_0, titaTlrNo_1, titaTxtNo_2);
    else 
      bankRmtfT = bankRmtfRepos.findTopByAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(acDate_0, titaTlrNo_1, titaTxtNo_2);

    return bankRmtfT.isPresent() ? bankRmtfT.get() : null;
  }

  @Override
  public BankRmtf holdById(BankRmtfId bankRmtfId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + bankRmtfId);
    Optional<BankRmtf> bankRmtf = null;
    if (dbName.equals(ContentName.onDay))
      bankRmtf = bankRmtfReposDay.findByBankRmtfId(bankRmtfId);
    else if (dbName.equals(ContentName.onMon))
      bankRmtf = bankRmtfReposMon.findByBankRmtfId(bankRmtfId);
    else if (dbName.equals(ContentName.onHist))
      bankRmtf = bankRmtfReposHist.findByBankRmtfId(bankRmtfId);
    else 
      bankRmtf = bankRmtfRepos.findByBankRmtfId(bankRmtfId);
    return bankRmtf.isPresent() ? bankRmtf.get() : null;
  }

  @Override
  public BankRmtf holdById(BankRmtf bankRmtf, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + bankRmtf.getBankRmtfId());
    Optional<BankRmtf> bankRmtfT = null;
    if (dbName.equals(ContentName.onDay))
      bankRmtfT = bankRmtfReposDay.findByBankRmtfId(bankRmtf.getBankRmtfId());
    else if (dbName.equals(ContentName.onMon))
      bankRmtfT = bankRmtfReposMon.findByBankRmtfId(bankRmtf.getBankRmtfId());
    else if (dbName.equals(ContentName.onHist))
      bankRmtfT = bankRmtfReposHist.findByBankRmtfId(bankRmtf.getBankRmtfId());
    else 
      bankRmtfT = bankRmtfRepos.findByBankRmtfId(bankRmtf.getBankRmtfId());
    return bankRmtfT.isPresent() ? bankRmtfT.get() : null;
  }

  @Override
  public BankRmtf insert(BankRmtf bankRmtf, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + bankRmtf.getBankRmtfId());
    if (this.findById(bankRmtf.getBankRmtfId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      bankRmtf.setCreateEmpNo(empNot);

    if(bankRmtf.getLastUpdateEmpNo() == null || bankRmtf.getLastUpdateEmpNo().isEmpty())
      bankRmtf.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return bankRmtfReposDay.saveAndFlush(bankRmtf);	
    else if (dbName.equals(ContentName.onMon))
      return bankRmtfReposMon.saveAndFlush(bankRmtf);
    else if (dbName.equals(ContentName.onHist))
      return bankRmtfReposHist.saveAndFlush(bankRmtf);
    else 
    return bankRmtfRepos.saveAndFlush(bankRmtf);
  }

  @Override
  public BankRmtf update(BankRmtf bankRmtf, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + bankRmtf.getBankRmtfId());
    if (!empNot.isEmpty())
      bankRmtf.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return bankRmtfReposDay.saveAndFlush(bankRmtf);	
    else if (dbName.equals(ContentName.onMon))
      return bankRmtfReposMon.saveAndFlush(bankRmtf);
    else if (dbName.equals(ContentName.onHist))
      return bankRmtfReposHist.saveAndFlush(bankRmtf);
    else 
    return bankRmtfRepos.saveAndFlush(bankRmtf);
  }

  @Override
  public BankRmtf update2(BankRmtf bankRmtf, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + bankRmtf.getBankRmtfId());
    if (!empNot.isEmpty())
      bankRmtf.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      bankRmtfReposDay.saveAndFlush(bankRmtf);	
    else if (dbName.equals(ContentName.onMon))
      bankRmtfReposMon.saveAndFlush(bankRmtf);
    else if (dbName.equals(ContentName.onHist))
        bankRmtfReposHist.saveAndFlush(bankRmtf);
    else 
      bankRmtfRepos.saveAndFlush(bankRmtf);	
    return this.findById(bankRmtf.getBankRmtfId());
  }

  @Override
  public void delete(BankRmtf bankRmtf, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + bankRmtf.getBankRmtfId());
    if (dbName.equals(ContentName.onDay)) {
      bankRmtfReposDay.delete(bankRmtf);	
      bankRmtfReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRmtfReposMon.delete(bankRmtf);	
      bankRmtfReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRmtfReposHist.delete(bankRmtf);
      bankRmtfReposHist.flush();
    }
    else {
      bankRmtfRepos.delete(bankRmtf);
      bankRmtfRepos.flush();
    }
   }

  @Override
  public void insertAll(List<BankRmtf> bankRmtf, TitaVo... titaVo) throws DBException {
    if (bankRmtf == null || bankRmtf.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (BankRmtf t : bankRmtf){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      bankRmtf = bankRmtfReposDay.saveAll(bankRmtf);	
      bankRmtfReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRmtf = bankRmtfReposMon.saveAll(bankRmtf);	
      bankRmtfReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRmtf = bankRmtfReposHist.saveAll(bankRmtf);
      bankRmtfReposHist.flush();
    }
    else {
      bankRmtf = bankRmtfRepos.saveAll(bankRmtf);
      bankRmtfRepos.flush();
    }
    }

  @Override
  public void updateAll(List<BankRmtf> bankRmtf, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (bankRmtf == null || bankRmtf.size() == 0)
      throw new DBException(6);

    for (BankRmtf t : bankRmtf) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      bankRmtf = bankRmtfReposDay.saveAll(bankRmtf);	
      bankRmtfReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRmtf = bankRmtfReposMon.saveAll(bankRmtf);	
      bankRmtfReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRmtf = bankRmtfReposHist.saveAll(bankRmtf);
      bankRmtfReposHist.flush();
    }
    else {
      bankRmtf = bankRmtfRepos.saveAll(bankRmtf);
      bankRmtfRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<BankRmtf> bankRmtf, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (bankRmtf == null || bankRmtf.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      bankRmtfReposDay.deleteAll(bankRmtf);	
      bankRmtfReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRmtfReposMon.deleteAll(bankRmtf);	
      bankRmtfReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRmtfReposHist.deleteAll(bankRmtf);
      bankRmtfReposHist.flush();
    }
    else {
      bankRmtfRepos.deleteAll(bankRmtf);
      bankRmtfRepos.flush();
    }
  }

}
