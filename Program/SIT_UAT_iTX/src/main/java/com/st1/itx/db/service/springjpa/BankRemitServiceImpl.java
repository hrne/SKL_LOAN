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
import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.domain.BankRemitId;
import com.st1.itx.db.repository.online.BankRemitRepository;
import com.st1.itx.db.repository.day.BankRemitRepositoryDay;
import com.st1.itx.db.repository.mon.BankRemitRepositoryMon;
import com.st1.itx.db.repository.hist.BankRemitRepositoryHist;
import com.st1.itx.db.service.BankRemitService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("bankRemitService")
@Repository
public class BankRemitServiceImpl extends ASpringJpaParm implements BankRemitService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private BankRemitRepository bankRemitRepos;

  @Autowired
  private BankRemitRepositoryDay bankRemitReposDay;

  @Autowired
  private BankRemitRepositoryMon bankRemitReposMon;

  @Autowired
  private BankRemitRepositoryHist bankRemitReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(bankRemitRepos);
    org.junit.Assert.assertNotNull(bankRemitReposDay);
    org.junit.Assert.assertNotNull(bankRemitReposMon);
    org.junit.Assert.assertNotNull(bankRemitReposHist);
  }

  @Override
  public BankRemit findById(BankRemitId bankRemitId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + bankRemitId);
    Optional<BankRemit> bankRemit = null;
    if (dbName.equals(ContentName.onDay))
      bankRemit = bankRemitReposDay.findById(bankRemitId);
    else if (dbName.equals(ContentName.onMon))
      bankRemit = bankRemitReposMon.findById(bankRemitId);
    else if (dbName.equals(ContentName.onHist))
      bankRemit = bankRemitReposHist.findById(bankRemitId);
    else 
      bankRemit = bankRemitRepos.findById(bankRemitId);
    BankRemit obj = bankRemit.isPresent() ? bankRemit.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<BankRemit> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankRemit> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AcDate", "TitaTlrNo", "TitaTxtNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcDate", "TitaTlrNo", "TitaTxtNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = bankRemitReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankRemitReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankRemitReposHist.findAll(pageable);
    else 
      slice = bankRemitRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankRemit> findL4001A(int acDate_0, int acDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankRemit> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4001A " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = bankRemitReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqual(acDate_0, acDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankRemitReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqual(acDate_0, acDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankRemitReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqual(acDate_0, acDate_1, pageable);
    else 
      slice = bankRemitRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqual(acDate_0, acDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankRemit> findL4901A(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankRemit> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4901A " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = bankRemitReposDay.findAllByCustNoIs(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankRemitReposMon.findAllByCustNoIs(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankRemitReposHist.findAllByCustNoIs(custNo_0, pageable);
    else 
      slice = bankRemitRepos.findAllByCustNoIs(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankRemit> findL4901B(int acDate_0, String batchNo_1, int drawdownCode_2, int drawdownCode_3, int statusCode_4, int statusCode_5, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankRemit> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4901B " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " +  batchNo_1 + " drawdownCode_2 : " +  drawdownCode_2 + " drawdownCode_3 : " +  drawdownCode_3 + " statusCode_4 : " +  statusCode_4 + " statusCode_5 : " +  statusCode_5);
    if (dbName.equals(ContentName.onDay))
      slice = bankRemitReposDay.findAllByAcDateIsAndBatchNoIsAndDrawdownCodeGreaterThanEqualAndDrawdownCodeLessThanEqualAndStatusCodeGreaterThanEqualAndStatusCodeLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(acDate_0, batchNo_1, drawdownCode_2, drawdownCode_3, statusCode_4, statusCode_5, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankRemitReposMon.findAllByAcDateIsAndBatchNoIsAndDrawdownCodeGreaterThanEqualAndDrawdownCodeLessThanEqualAndStatusCodeGreaterThanEqualAndStatusCodeLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(acDate_0, batchNo_1, drawdownCode_2, drawdownCode_3, statusCode_4, statusCode_5, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankRemitReposHist.findAllByAcDateIsAndBatchNoIsAndDrawdownCodeGreaterThanEqualAndDrawdownCodeLessThanEqualAndStatusCodeGreaterThanEqualAndStatusCodeLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(acDate_0, batchNo_1, drawdownCode_2, drawdownCode_3, statusCode_4, statusCode_5, pageable);
    else 
      slice = bankRemitRepos.findAllByAcDateIsAndBatchNoIsAndDrawdownCodeGreaterThanEqualAndDrawdownCodeLessThanEqualAndStatusCodeGreaterThanEqualAndStatusCodeLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(acDate_0, batchNo_1, drawdownCode_2, drawdownCode_3, statusCode_4, statusCode_5, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankRemit> findL4901C(int acDate_0, int drawdownCode_1, int drawdownCode_2, int statusCode_3, int statusCode_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankRemit> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4901C " + dbName + " : " + "acDate_0 : " + acDate_0 + " drawdownCode_1 : " +  drawdownCode_1 + " drawdownCode_2 : " +  drawdownCode_2 + " statusCode_3 : " +  statusCode_3 + " statusCode_4 : " +  statusCode_4);
    if (dbName.equals(ContentName.onDay))
      slice = bankRemitReposDay.findAllByAcDateIsAndDrawdownCodeGreaterThanEqualAndDrawdownCodeLessThanEqualAndStatusCodeGreaterThanEqualAndStatusCodeLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(acDate_0, drawdownCode_1, drawdownCode_2, statusCode_3, statusCode_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankRemitReposMon.findAllByAcDateIsAndDrawdownCodeGreaterThanEqualAndDrawdownCodeLessThanEqualAndStatusCodeGreaterThanEqualAndStatusCodeLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(acDate_0, drawdownCode_1, drawdownCode_2, statusCode_3, statusCode_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankRemitReposHist.findAllByAcDateIsAndDrawdownCodeGreaterThanEqualAndDrawdownCodeLessThanEqualAndStatusCodeGreaterThanEqualAndStatusCodeLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(acDate_0, drawdownCode_1, drawdownCode_2, statusCode_3, statusCode_4, pageable);
    else 
      slice = bankRemitRepos.findAllByAcDateIsAndDrawdownCodeGreaterThanEqualAndDrawdownCodeLessThanEqualAndStatusCodeGreaterThanEqualAndStatusCodeLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(acDate_0, drawdownCode_1, drawdownCode_2, statusCode_3, statusCode_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public BankRemit findL4104AFirst(String titaTlrNo_0, String titaTxtNo_1, int drawdownCode_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findL4104AFirst " + dbName + " : " + "titaTlrNo_0 : " + titaTlrNo_0 + " titaTxtNo_1 : " +  titaTxtNo_1 + " drawdownCode_2 : " +  drawdownCode_2);
    Optional<BankRemit> bankRemitT = null;
    if (dbName.equals(ContentName.onDay))
      bankRemitT = bankRemitReposDay.findTopByTitaTlrNoIsAndTitaTxtNoIsAndDrawdownCodeIsOrderByAcDateDesc(titaTlrNo_0, titaTxtNo_1, drawdownCode_2);
    else if (dbName.equals(ContentName.onMon))
      bankRemitT = bankRemitReposMon.findTopByTitaTlrNoIsAndTitaTxtNoIsAndDrawdownCodeIsOrderByAcDateDesc(titaTlrNo_0, titaTxtNo_1, drawdownCode_2);
    else if (dbName.equals(ContentName.onHist))
      bankRemitT = bankRemitReposHist.findTopByTitaTlrNoIsAndTitaTxtNoIsAndDrawdownCodeIsOrderByAcDateDesc(titaTlrNo_0, titaTxtNo_1, drawdownCode_2);
    else 
      bankRemitT = bankRemitRepos.findTopByTitaTlrNoIsAndTitaTxtNoIsAndDrawdownCodeIsOrderByAcDateDesc(titaTlrNo_0, titaTxtNo_1, drawdownCode_2);

    return bankRemitT.isPresent() ? bankRemitT.get() : null;
  }

  @Override
  public BankRemit findL4104BFirst(int custNo_0, int facmNo_1, int bormNo_2, int drawdownCode_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findL4104BFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2 + " drawdownCode_3 : " +  drawdownCode_3);
    Optional<BankRemit> bankRemitT = null;
    if (dbName.equals(ContentName.onDay))
      bankRemitT = bankRemitReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndDrawdownCodeIs(custNo_0, facmNo_1, bormNo_2, drawdownCode_3);
    else if (dbName.equals(ContentName.onMon))
      bankRemitT = bankRemitReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndDrawdownCodeIs(custNo_0, facmNo_1, bormNo_2, drawdownCode_3);
    else if (dbName.equals(ContentName.onHist))
      bankRemitT = bankRemitReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndDrawdownCodeIs(custNo_0, facmNo_1, bormNo_2, drawdownCode_3);
    else 
      bankRemitT = bankRemitRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndDrawdownCodeIs(custNo_0, facmNo_1, bormNo_2, drawdownCode_3);

    return bankRemitT.isPresent() ? bankRemitT.get() : null;
  }

  @Override
  public BankRemit holdById(BankRemitId bankRemitId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + bankRemitId);
    Optional<BankRemit> bankRemit = null;
    if (dbName.equals(ContentName.onDay))
      bankRemit = bankRemitReposDay.findByBankRemitId(bankRemitId);
    else if (dbName.equals(ContentName.onMon))
      bankRemit = bankRemitReposMon.findByBankRemitId(bankRemitId);
    else if (dbName.equals(ContentName.onHist))
      bankRemit = bankRemitReposHist.findByBankRemitId(bankRemitId);
    else 
      bankRemit = bankRemitRepos.findByBankRemitId(bankRemitId);
    return bankRemit.isPresent() ? bankRemit.get() : null;
  }

  @Override
  public BankRemit holdById(BankRemit bankRemit, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + bankRemit.getBankRemitId());
    Optional<BankRemit> bankRemitT = null;
    if (dbName.equals(ContentName.onDay))
      bankRemitT = bankRemitReposDay.findByBankRemitId(bankRemit.getBankRemitId());
    else if (dbName.equals(ContentName.onMon))
      bankRemitT = bankRemitReposMon.findByBankRemitId(bankRemit.getBankRemitId());
    else if (dbName.equals(ContentName.onHist))
      bankRemitT = bankRemitReposHist.findByBankRemitId(bankRemit.getBankRemitId());
    else 
      bankRemitT = bankRemitRepos.findByBankRemitId(bankRemit.getBankRemitId());
    return bankRemitT.isPresent() ? bankRemitT.get() : null;
  }

  @Override
  public BankRemit insert(BankRemit bankRemit, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + bankRemit.getBankRemitId());
    if (this.findById(bankRemit.getBankRemitId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      bankRemit.setCreateEmpNo(empNot);

    if(bankRemit.getLastUpdateEmpNo() == null || bankRemit.getLastUpdateEmpNo().isEmpty())
      bankRemit.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return bankRemitReposDay.saveAndFlush(bankRemit);	
    else if (dbName.equals(ContentName.onMon))
      return bankRemitReposMon.saveAndFlush(bankRemit);
    else if (dbName.equals(ContentName.onHist))
      return bankRemitReposHist.saveAndFlush(bankRemit);
    else 
    return bankRemitRepos.saveAndFlush(bankRemit);
  }

  @Override
  public BankRemit update(BankRemit bankRemit, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + bankRemit.getBankRemitId());
    if (!empNot.isEmpty())
      bankRemit.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return bankRemitReposDay.saveAndFlush(bankRemit);	
    else if (dbName.equals(ContentName.onMon))
      return bankRemitReposMon.saveAndFlush(bankRemit);
    else if (dbName.equals(ContentName.onHist))
      return bankRemitReposHist.saveAndFlush(bankRemit);
    else 
    return bankRemitRepos.saveAndFlush(bankRemit);
  }

  @Override
  public BankRemit update2(BankRemit bankRemit, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + bankRemit.getBankRemitId());
    if (!empNot.isEmpty())
      bankRemit.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      bankRemitReposDay.saveAndFlush(bankRemit);	
    else if (dbName.equals(ContentName.onMon))
      bankRemitReposMon.saveAndFlush(bankRemit);
    else if (dbName.equals(ContentName.onHist))
        bankRemitReposHist.saveAndFlush(bankRemit);
    else 
      bankRemitRepos.saveAndFlush(bankRemit);	
    return this.findById(bankRemit.getBankRemitId());
  }

  @Override
  public void delete(BankRemit bankRemit, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + bankRemit.getBankRemitId());
    if (dbName.equals(ContentName.onDay)) {
      bankRemitReposDay.delete(bankRemit);	
      bankRemitReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRemitReposMon.delete(bankRemit);	
      bankRemitReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRemitReposHist.delete(bankRemit);
      bankRemitReposHist.flush();
    }
    else {
      bankRemitRepos.delete(bankRemit);
      bankRemitRepos.flush();
    }
   }

  @Override
  public void insertAll(List<BankRemit> bankRemit, TitaVo... titaVo) throws DBException {
    if (bankRemit == null || bankRemit.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (BankRemit t : bankRemit){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      bankRemit = bankRemitReposDay.saveAll(bankRemit);	
      bankRemitReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRemit = bankRemitReposMon.saveAll(bankRemit);	
      bankRemitReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRemit = bankRemitReposHist.saveAll(bankRemit);
      bankRemitReposHist.flush();
    }
    else {
      bankRemit = bankRemitRepos.saveAll(bankRemit);
      bankRemitRepos.flush();
    }
    }

  @Override
  public void updateAll(List<BankRemit> bankRemit, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (bankRemit == null || bankRemit.size() == 0)
      throw new DBException(6);

    for (BankRemit t : bankRemit) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      bankRemit = bankRemitReposDay.saveAll(bankRemit);	
      bankRemitReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRemit = bankRemitReposMon.saveAll(bankRemit);	
      bankRemitReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRemit = bankRemitReposHist.saveAll(bankRemit);
      bankRemitReposHist.flush();
    }
    else {
      bankRemit = bankRemitRepos.saveAll(bankRemit);
      bankRemitRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<BankRemit> bankRemit, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (bankRemit == null || bankRemit.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      bankRemitReposDay.deleteAll(bankRemit);	
      bankRemitReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankRemitReposMon.deleteAll(bankRemit);	
      bankRemitReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankRemitReposHist.deleteAll(bankRemit);
      bankRemitReposHist.flush();
    }
    else {
      bankRemitRepos.deleteAll(bankRemit);
      bankRemitRepos.flush();
    }
  }

}
