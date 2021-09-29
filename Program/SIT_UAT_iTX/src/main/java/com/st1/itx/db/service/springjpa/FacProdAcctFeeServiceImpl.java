package com.st1.itx.db.service.springjpa;

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
import com.st1.itx.db.domain.FacProdAcctFee;
import com.st1.itx.db.domain.FacProdAcctFeeId;
import com.st1.itx.db.repository.online.FacProdAcctFeeRepository;
import com.st1.itx.db.repository.day.FacProdAcctFeeRepositoryDay;
import com.st1.itx.db.repository.mon.FacProdAcctFeeRepositoryMon;
import com.st1.itx.db.repository.hist.FacProdAcctFeeRepositoryHist;
import com.st1.itx.db.service.FacProdAcctFeeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("facProdAcctFeeService")
@Repository
public class FacProdAcctFeeServiceImpl extends ASpringJpaParm implements FacProdAcctFeeService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private FacProdAcctFeeRepository facProdAcctFeeRepos;

  @Autowired
  private FacProdAcctFeeRepositoryDay facProdAcctFeeReposDay;

  @Autowired
  private FacProdAcctFeeRepositoryMon facProdAcctFeeReposMon;

  @Autowired
  private FacProdAcctFeeRepositoryHist facProdAcctFeeReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(facProdAcctFeeRepos);
    org.junit.Assert.assertNotNull(facProdAcctFeeReposDay);
    org.junit.Assert.assertNotNull(facProdAcctFeeReposMon);
    org.junit.Assert.assertNotNull(facProdAcctFeeReposHist);
  }

  @Override
  public FacProdAcctFee findById(FacProdAcctFeeId facProdAcctFeeId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + facProdAcctFeeId);
    Optional<FacProdAcctFee> facProdAcctFee = null;
    if (dbName.equals(ContentName.onDay))
      facProdAcctFee = facProdAcctFeeReposDay.findById(facProdAcctFeeId);
    else if (dbName.equals(ContentName.onMon))
      facProdAcctFee = facProdAcctFeeReposMon.findById(facProdAcctFeeId);
    else if (dbName.equals(ContentName.onHist))
      facProdAcctFee = facProdAcctFeeReposHist.findById(facProdAcctFeeId);
    else 
      facProdAcctFee = facProdAcctFeeRepos.findById(facProdAcctFeeId);
    FacProdAcctFee obj = facProdAcctFee.isPresent() ? facProdAcctFee.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<FacProdAcctFee> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacProdAcctFee> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ProdNo", "FeeType", "LoanLow"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ProdNo", "FeeType", "LoanLow"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = facProdAcctFeeReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facProdAcctFeeReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facProdAcctFeeReposHist.findAll(pageable);
    else 
      slice = facProdAcctFeeRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<FacProdAcctFee> acctFeeProdNoEq(String prodNo_0, String feeType_1, BigDecimal loanLow_2, BigDecimal loanLow_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacProdAcctFee> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acctFeeProdNoEq " + dbName + " : " + "prodNo_0 : " + prodNo_0 + " feeType_1 : " +  feeType_1 + " loanLow_2 : " +  loanLow_2 + " loanLow_3 : " +  loanLow_3);
    if (dbName.equals(ContentName.onDay))
      slice = facProdAcctFeeReposDay.findAllByProdNoIsAndFeeTypeIsAndLoanLowGreaterThanEqualAndLoanLowLessThanEqual(prodNo_0, feeType_1, loanLow_2, loanLow_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facProdAcctFeeReposMon.findAllByProdNoIsAndFeeTypeIsAndLoanLowGreaterThanEqualAndLoanLowLessThanEqual(prodNo_0, feeType_1, loanLow_2, loanLow_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facProdAcctFeeReposHist.findAllByProdNoIsAndFeeTypeIsAndLoanLowGreaterThanEqualAndLoanLowLessThanEqual(prodNo_0, feeType_1, loanLow_2, loanLow_3, pageable);
    else 
      slice = facProdAcctFeeRepos.findAllByProdNoIsAndFeeTypeIsAndLoanLowGreaterThanEqualAndLoanLowLessThanEqual(prodNo_0, feeType_1, loanLow_2, loanLow_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public FacProdAcctFee holdById(FacProdAcctFeeId facProdAcctFeeId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + facProdAcctFeeId);
    Optional<FacProdAcctFee> facProdAcctFee = null;
    if (dbName.equals(ContentName.onDay))
      facProdAcctFee = facProdAcctFeeReposDay.findByFacProdAcctFeeId(facProdAcctFeeId);
    else if (dbName.equals(ContentName.onMon))
      facProdAcctFee = facProdAcctFeeReposMon.findByFacProdAcctFeeId(facProdAcctFeeId);
    else if (dbName.equals(ContentName.onHist))
      facProdAcctFee = facProdAcctFeeReposHist.findByFacProdAcctFeeId(facProdAcctFeeId);
    else 
      facProdAcctFee = facProdAcctFeeRepos.findByFacProdAcctFeeId(facProdAcctFeeId);
    return facProdAcctFee.isPresent() ? facProdAcctFee.get() : null;
  }

  @Override
  public FacProdAcctFee holdById(FacProdAcctFee facProdAcctFee, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + facProdAcctFee.getFacProdAcctFeeId());
    Optional<FacProdAcctFee> facProdAcctFeeT = null;
    if (dbName.equals(ContentName.onDay))
      facProdAcctFeeT = facProdAcctFeeReposDay.findByFacProdAcctFeeId(facProdAcctFee.getFacProdAcctFeeId());
    else if (dbName.equals(ContentName.onMon))
      facProdAcctFeeT = facProdAcctFeeReposMon.findByFacProdAcctFeeId(facProdAcctFee.getFacProdAcctFeeId());
    else if (dbName.equals(ContentName.onHist))
      facProdAcctFeeT = facProdAcctFeeReposHist.findByFacProdAcctFeeId(facProdAcctFee.getFacProdAcctFeeId());
    else 
      facProdAcctFeeT = facProdAcctFeeRepos.findByFacProdAcctFeeId(facProdAcctFee.getFacProdAcctFeeId());
    return facProdAcctFeeT.isPresent() ? facProdAcctFeeT.get() : null;
  }

  @Override
  public FacProdAcctFee insert(FacProdAcctFee facProdAcctFee, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + facProdAcctFee.getFacProdAcctFeeId());
    if (this.findById(facProdAcctFee.getFacProdAcctFeeId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      facProdAcctFee.setCreateEmpNo(empNot);

    if(facProdAcctFee.getLastUpdateEmpNo() == null || facProdAcctFee.getLastUpdateEmpNo().isEmpty())
      facProdAcctFee.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return facProdAcctFeeReposDay.saveAndFlush(facProdAcctFee);	
    else if (dbName.equals(ContentName.onMon))
      return facProdAcctFeeReposMon.saveAndFlush(facProdAcctFee);
    else if (dbName.equals(ContentName.onHist))
      return facProdAcctFeeReposHist.saveAndFlush(facProdAcctFee);
    else 
    return facProdAcctFeeRepos.saveAndFlush(facProdAcctFee);
  }

  @Override
  public FacProdAcctFee update(FacProdAcctFee facProdAcctFee, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + facProdAcctFee.getFacProdAcctFeeId());
    if (!empNot.isEmpty())
      facProdAcctFee.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return facProdAcctFeeReposDay.saveAndFlush(facProdAcctFee);	
    else if (dbName.equals(ContentName.onMon))
      return facProdAcctFeeReposMon.saveAndFlush(facProdAcctFee);
    else if (dbName.equals(ContentName.onHist))
      return facProdAcctFeeReposHist.saveAndFlush(facProdAcctFee);
    else 
    return facProdAcctFeeRepos.saveAndFlush(facProdAcctFee);
  }

  @Override
  public FacProdAcctFee update2(FacProdAcctFee facProdAcctFee, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + facProdAcctFee.getFacProdAcctFeeId());
    if (!empNot.isEmpty())
      facProdAcctFee.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      facProdAcctFeeReposDay.saveAndFlush(facProdAcctFee);	
    else if (dbName.equals(ContentName.onMon))
      facProdAcctFeeReposMon.saveAndFlush(facProdAcctFee);
    else if (dbName.equals(ContentName.onHist))
        facProdAcctFeeReposHist.saveAndFlush(facProdAcctFee);
    else 
      facProdAcctFeeRepos.saveAndFlush(facProdAcctFee);	
    return this.findById(facProdAcctFee.getFacProdAcctFeeId());
  }

  @Override
  public void delete(FacProdAcctFee facProdAcctFee, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + facProdAcctFee.getFacProdAcctFeeId());
    if (dbName.equals(ContentName.onDay)) {
      facProdAcctFeeReposDay.delete(facProdAcctFee);	
      facProdAcctFeeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facProdAcctFeeReposMon.delete(facProdAcctFee);	
      facProdAcctFeeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facProdAcctFeeReposHist.delete(facProdAcctFee);
      facProdAcctFeeReposHist.flush();
    }
    else {
      facProdAcctFeeRepos.delete(facProdAcctFee);
      facProdAcctFeeRepos.flush();
    }
   }

  @Override
  public void insertAll(List<FacProdAcctFee> facProdAcctFee, TitaVo... titaVo) throws DBException {
    if (facProdAcctFee == null || facProdAcctFee.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (FacProdAcctFee t : facProdAcctFee){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      facProdAcctFee = facProdAcctFeeReposDay.saveAll(facProdAcctFee);	
      facProdAcctFeeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facProdAcctFee = facProdAcctFeeReposMon.saveAll(facProdAcctFee);	
      facProdAcctFeeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facProdAcctFee = facProdAcctFeeReposHist.saveAll(facProdAcctFee);
      facProdAcctFeeReposHist.flush();
    }
    else {
      facProdAcctFee = facProdAcctFeeRepos.saveAll(facProdAcctFee);
      facProdAcctFeeRepos.flush();
    }
    }

  @Override
  public void updateAll(List<FacProdAcctFee> facProdAcctFee, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (facProdAcctFee == null || facProdAcctFee.size() == 0)
      throw new DBException(6);

    for (FacProdAcctFee t : facProdAcctFee) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      facProdAcctFee = facProdAcctFeeReposDay.saveAll(facProdAcctFee);	
      facProdAcctFeeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facProdAcctFee = facProdAcctFeeReposMon.saveAll(facProdAcctFee);	
      facProdAcctFeeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facProdAcctFee = facProdAcctFeeReposHist.saveAll(facProdAcctFee);
      facProdAcctFeeReposHist.flush();
    }
    else {
      facProdAcctFee = facProdAcctFeeRepos.saveAll(facProdAcctFee);
      facProdAcctFeeRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<FacProdAcctFee> facProdAcctFee, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (facProdAcctFee == null || facProdAcctFee.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      facProdAcctFeeReposDay.deleteAll(facProdAcctFee);	
      facProdAcctFeeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facProdAcctFeeReposMon.deleteAll(facProdAcctFee);	
      facProdAcctFeeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facProdAcctFeeReposHist.deleteAll(facProdAcctFee);
      facProdAcctFeeReposHist.flush();
    }
    else {
      facProdAcctFeeRepos.deleteAll(facProdAcctFee);
      facProdAcctFeeRepos.flush();
    }
  }

}
