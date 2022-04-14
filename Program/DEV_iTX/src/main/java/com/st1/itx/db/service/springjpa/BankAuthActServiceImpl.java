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
import com.st1.itx.db.domain.BankAuthAct;
import com.st1.itx.db.domain.BankAuthActId;
import com.st1.itx.db.repository.online.BankAuthActRepository;
import com.st1.itx.db.repository.day.BankAuthActRepositoryDay;
import com.st1.itx.db.repository.mon.BankAuthActRepositoryMon;
import com.st1.itx.db.repository.hist.BankAuthActRepositoryHist;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("bankAuthActService")
@Repository
public class BankAuthActServiceImpl extends ASpringJpaParm implements BankAuthActService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private BankAuthActRepository bankAuthActRepos;

  @Autowired
  private BankAuthActRepositoryDay bankAuthActReposDay;

  @Autowired
  private BankAuthActRepositoryMon bankAuthActReposMon;

  @Autowired
  private BankAuthActRepositoryHist bankAuthActReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(bankAuthActRepos);
    org.junit.Assert.assertNotNull(bankAuthActReposDay);
    org.junit.Assert.assertNotNull(bankAuthActReposMon);
    org.junit.Assert.assertNotNull(bankAuthActReposHist);
  }

  @Override
  public BankAuthAct findById(BankAuthActId bankAuthActId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + bankAuthActId);
    Optional<BankAuthAct> bankAuthAct = null;
    if (dbName.equals(ContentName.onDay))
      bankAuthAct = bankAuthActReposDay.findById(bankAuthActId);
    else if (dbName.equals(ContentName.onMon))
      bankAuthAct = bankAuthActReposMon.findById(bankAuthActId);
    else if (dbName.equals(ContentName.onHist))
      bankAuthAct = bankAuthActReposHist.findById(bankAuthActId);
    else 
      bankAuthAct = bankAuthActRepos.findById(bankAuthActId);
    BankAuthAct obj = bankAuthAct.isPresent() ? bankAuthAct.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<BankAuthAct> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankAuthAct> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "AuthType"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "AuthType"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = bankAuthActReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankAuthActReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankAuthActReposHist.findAll(pageable);
    else 
      slice = bankAuthActRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankAuthAct> authCheck(int custNo_0, String repayAcct_1, int facmNo_2, int facmNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankAuthAct> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("authCheck " + dbName + " : " + "custNo_0 : " + custNo_0 + " repayAcct_1 : " +  repayAcct_1 + " facmNo_2 : " +  facmNo_2 + " facmNo_3 : " +  facmNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = bankAuthActReposDay.findAllByCustNoIsAndRepayAcctIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAsc(custNo_0, repayAcct_1, facmNo_2, facmNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankAuthActReposMon.findAllByCustNoIsAndRepayAcctIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAsc(custNo_0, repayAcct_1, facmNo_2, facmNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankAuthActReposHist.findAllByCustNoIsAndRepayAcctIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAsc(custNo_0, repayAcct_1, facmNo_2, facmNo_3, pageable);
    else 
      slice = bankAuthActRepos.findAllByCustNoIsAndRepayAcctIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAsc(custNo_0, repayAcct_1, facmNo_2, facmNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankAuthAct> facmNoEq(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankAuthAct> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("facmNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = bankAuthActReposDay.findAllByCustNoIsAndFacmNoIs(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankAuthActReposMon.findAllByCustNoIsAndFacmNoIs(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankAuthActReposHist.findAllByCustNoIsAndFacmNoIs(custNo_0, facmNo_1, pageable);
    else 
      slice = bankAuthActRepos.findAllByCustNoIsAndFacmNoIs(custNo_0, facmNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankAuthAct> findAcctNo(int custNo_0, String repayAcct_1, String repayBank_2, int facmNo_3, int facmNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankAuthAct> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findAcctNo " + dbName + " : " + "custNo_0 : " + custNo_0 + " repayAcct_1 : " +  repayAcct_1 + " repayBank_2 : " +  repayBank_2 + " facmNo_3 : " +  facmNo_3 + " facmNo_4 : " +  facmNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = bankAuthActReposDay.findAllByCustNoIsAndRepayAcctIsAndRepayBankIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAsc(custNo_0, repayAcct_1, repayBank_2, facmNo_3, facmNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankAuthActReposMon.findAllByCustNoIsAndRepayAcctIsAndRepayBankIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAsc(custNo_0, repayAcct_1, repayBank_2, facmNo_3, facmNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankAuthActReposHist.findAllByCustNoIsAndRepayAcctIsAndRepayBankIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAsc(custNo_0, repayAcct_1, repayBank_2, facmNo_3, facmNo_4, pageable);
    else 
      slice = bankAuthActRepos.findAllByCustNoIsAndRepayAcctIsAndRepayBankIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAsc(custNo_0, repayAcct_1, repayBank_2, facmNo_3, facmNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankAuthAct> findCustNoEq(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankAuthAct> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCustNoEq " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = bankAuthActReposDay.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankAuthActReposMon.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankAuthActReposHist.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);
    else 
      slice = bankAuthActRepos.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public BankAuthAct holdById(BankAuthActId bankAuthActId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + bankAuthActId);
    Optional<BankAuthAct> bankAuthAct = null;
    if (dbName.equals(ContentName.onDay))
      bankAuthAct = bankAuthActReposDay.findByBankAuthActId(bankAuthActId);
    else if (dbName.equals(ContentName.onMon))
      bankAuthAct = bankAuthActReposMon.findByBankAuthActId(bankAuthActId);
    else if (dbName.equals(ContentName.onHist))
      bankAuthAct = bankAuthActReposHist.findByBankAuthActId(bankAuthActId);
    else 
      bankAuthAct = bankAuthActRepos.findByBankAuthActId(bankAuthActId);
    return bankAuthAct.isPresent() ? bankAuthAct.get() : null;
  }

  @Override
  public BankAuthAct holdById(BankAuthAct bankAuthAct, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + bankAuthAct.getBankAuthActId());
    Optional<BankAuthAct> bankAuthActT = null;
    if (dbName.equals(ContentName.onDay))
      bankAuthActT = bankAuthActReposDay.findByBankAuthActId(bankAuthAct.getBankAuthActId());
    else if (dbName.equals(ContentName.onMon))
      bankAuthActT = bankAuthActReposMon.findByBankAuthActId(bankAuthAct.getBankAuthActId());
    else if (dbName.equals(ContentName.onHist))
      bankAuthActT = bankAuthActReposHist.findByBankAuthActId(bankAuthAct.getBankAuthActId());
    else 
      bankAuthActT = bankAuthActRepos.findByBankAuthActId(bankAuthAct.getBankAuthActId());
    return bankAuthActT.isPresent() ? bankAuthActT.get() : null;
  }

  @Override
  public BankAuthAct insert(BankAuthAct bankAuthAct, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + bankAuthAct.getBankAuthActId());
    if (this.findById(bankAuthAct.getBankAuthActId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      bankAuthAct.setCreateEmpNo(empNot);

    if(bankAuthAct.getLastUpdateEmpNo() == null || bankAuthAct.getLastUpdateEmpNo().isEmpty())
      bankAuthAct.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return bankAuthActReposDay.saveAndFlush(bankAuthAct);	
    else if (dbName.equals(ContentName.onMon))
      return bankAuthActReposMon.saveAndFlush(bankAuthAct);
    else if (dbName.equals(ContentName.onHist))
      return bankAuthActReposHist.saveAndFlush(bankAuthAct);
    else 
    return bankAuthActRepos.saveAndFlush(bankAuthAct);
  }

  @Override
  public BankAuthAct update(BankAuthAct bankAuthAct, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + bankAuthAct.getBankAuthActId());
    if (!empNot.isEmpty())
      bankAuthAct.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return bankAuthActReposDay.saveAndFlush(bankAuthAct);	
    else if (dbName.equals(ContentName.onMon))
      return bankAuthActReposMon.saveAndFlush(bankAuthAct);
    else if (dbName.equals(ContentName.onHist))
      return bankAuthActReposHist.saveAndFlush(bankAuthAct);
    else 
    return bankAuthActRepos.saveAndFlush(bankAuthAct);
  }

  @Override
  public BankAuthAct update2(BankAuthAct bankAuthAct, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + bankAuthAct.getBankAuthActId());
    if (!empNot.isEmpty())
      bankAuthAct.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      bankAuthActReposDay.saveAndFlush(bankAuthAct);	
    else if (dbName.equals(ContentName.onMon))
      bankAuthActReposMon.saveAndFlush(bankAuthAct);
    else if (dbName.equals(ContentName.onHist))
        bankAuthActReposHist.saveAndFlush(bankAuthAct);
    else 
      bankAuthActRepos.saveAndFlush(bankAuthAct);	
    return this.findById(bankAuthAct.getBankAuthActId());
  }

  @Override
  public void delete(BankAuthAct bankAuthAct, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + bankAuthAct.getBankAuthActId());
    if (dbName.equals(ContentName.onDay)) {
      bankAuthActReposDay.delete(bankAuthAct);	
      bankAuthActReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankAuthActReposMon.delete(bankAuthAct);	
      bankAuthActReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankAuthActReposHist.delete(bankAuthAct);
      bankAuthActReposHist.flush();
    }
    else {
      bankAuthActRepos.delete(bankAuthAct);
      bankAuthActRepos.flush();
    }
   }

  @Override
  public void insertAll(List<BankAuthAct> bankAuthAct, TitaVo... titaVo) throws DBException {
    if (bankAuthAct == null || bankAuthAct.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (BankAuthAct t : bankAuthAct){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      bankAuthAct = bankAuthActReposDay.saveAll(bankAuthAct);	
      bankAuthActReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankAuthAct = bankAuthActReposMon.saveAll(bankAuthAct);	
      bankAuthActReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankAuthAct = bankAuthActReposHist.saveAll(bankAuthAct);
      bankAuthActReposHist.flush();
    }
    else {
      bankAuthAct = bankAuthActRepos.saveAll(bankAuthAct);
      bankAuthActRepos.flush();
    }
    }

  @Override
  public void updateAll(List<BankAuthAct> bankAuthAct, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (bankAuthAct == null || bankAuthAct.size() == 0)
      throw new DBException(6);

    for (BankAuthAct t : bankAuthAct) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      bankAuthAct = bankAuthActReposDay.saveAll(bankAuthAct);	
      bankAuthActReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankAuthAct = bankAuthActReposMon.saveAll(bankAuthAct);	
      bankAuthActReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankAuthAct = bankAuthActReposHist.saveAll(bankAuthAct);
      bankAuthActReposHist.flush();
    }
    else {
      bankAuthAct = bankAuthActRepos.saveAll(bankAuthAct);
      bankAuthActRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<BankAuthAct> bankAuthAct, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (bankAuthAct == null || bankAuthAct.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      bankAuthActReposDay.deleteAll(bankAuthAct);	
      bankAuthActReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankAuthActReposMon.deleteAll(bankAuthAct);	
      bankAuthActReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankAuthActReposHist.deleteAll(bankAuthAct);
      bankAuthActReposHist.flush();
    }
    else {
      bankAuthActRepos.deleteAll(bankAuthAct);
      bankAuthActRepos.flush();
    }
  }

}
