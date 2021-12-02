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
import com.st1.itx.db.domain.LoanIfrs9Ip;
import com.st1.itx.db.domain.LoanIfrs9IpId;
import com.st1.itx.db.repository.online.LoanIfrs9IpRepository;
import com.st1.itx.db.repository.day.LoanIfrs9IpRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIfrs9IpRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIfrs9IpRepositoryHist;
import com.st1.itx.db.service.LoanIfrs9IpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIfrs9IpService")
@Repository
public class LoanIfrs9IpServiceImpl extends ASpringJpaParm implements LoanIfrs9IpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanIfrs9IpRepository loanIfrs9IpRepos;

  @Autowired
  private LoanIfrs9IpRepositoryDay loanIfrs9IpReposDay;

  @Autowired
  private LoanIfrs9IpRepositoryMon loanIfrs9IpReposMon;

  @Autowired
  private LoanIfrs9IpRepositoryHist loanIfrs9IpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanIfrs9IpRepos);
    org.junit.Assert.assertNotNull(loanIfrs9IpReposDay);
    org.junit.Assert.assertNotNull(loanIfrs9IpReposMon);
    org.junit.Assert.assertNotNull(loanIfrs9IpReposHist);
  }

  @Override
  public LoanIfrs9Ip findById(LoanIfrs9IpId loanIfrs9IpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + loanIfrs9IpId);
    Optional<LoanIfrs9Ip> loanIfrs9Ip = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9Ip = loanIfrs9IpReposDay.findById(loanIfrs9IpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9Ip = loanIfrs9IpReposMon.findById(loanIfrs9IpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9Ip = loanIfrs9IpReposHist.findById(loanIfrs9IpId);
    else 
      loanIfrs9Ip = loanIfrs9IpRepos.findById(loanIfrs9IpId);
    LoanIfrs9Ip obj = loanIfrs9Ip.isPresent() ? loanIfrs9Ip.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanIfrs9Ip> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanIfrs9Ip> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanIfrs9IpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanIfrs9IpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanIfrs9IpReposHist.findAll(pageable);
    else 
      slice = loanIfrs9IpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanIfrs9Ip holdById(LoanIfrs9IpId loanIfrs9IpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrs9IpId);
    Optional<LoanIfrs9Ip> loanIfrs9Ip = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9Ip = loanIfrs9IpReposDay.findByLoanIfrs9IpId(loanIfrs9IpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9Ip = loanIfrs9IpReposMon.findByLoanIfrs9IpId(loanIfrs9IpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9Ip = loanIfrs9IpReposHist.findByLoanIfrs9IpId(loanIfrs9IpId);
    else 
      loanIfrs9Ip = loanIfrs9IpRepos.findByLoanIfrs9IpId(loanIfrs9IpId);
    return loanIfrs9Ip.isPresent() ? loanIfrs9Ip.get() : null;
  }

  @Override
  public LoanIfrs9Ip holdById(LoanIfrs9Ip loanIfrs9Ip, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrs9Ip.getLoanIfrs9IpId());
    Optional<LoanIfrs9Ip> loanIfrs9IpT = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9IpT = loanIfrs9IpReposDay.findByLoanIfrs9IpId(loanIfrs9Ip.getLoanIfrs9IpId());
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9IpT = loanIfrs9IpReposMon.findByLoanIfrs9IpId(loanIfrs9Ip.getLoanIfrs9IpId());
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9IpT = loanIfrs9IpReposHist.findByLoanIfrs9IpId(loanIfrs9Ip.getLoanIfrs9IpId());
    else 
      loanIfrs9IpT = loanIfrs9IpRepos.findByLoanIfrs9IpId(loanIfrs9Ip.getLoanIfrs9IpId());
    return loanIfrs9IpT.isPresent() ? loanIfrs9IpT.get() : null;
  }

  @Override
  public LoanIfrs9Ip insert(LoanIfrs9Ip loanIfrs9Ip, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + loanIfrs9Ip.getLoanIfrs9IpId());
    if (this.findById(loanIfrs9Ip.getLoanIfrs9IpId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanIfrs9Ip.setCreateEmpNo(empNot);

    if(loanIfrs9Ip.getLastUpdateEmpNo() == null || loanIfrs9Ip.getLastUpdateEmpNo().isEmpty())
      loanIfrs9Ip.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrs9IpReposDay.saveAndFlush(loanIfrs9Ip);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrs9IpReposMon.saveAndFlush(loanIfrs9Ip);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrs9IpReposHist.saveAndFlush(loanIfrs9Ip);
    else 
    return loanIfrs9IpRepos.saveAndFlush(loanIfrs9Ip);
  }

  @Override
  public LoanIfrs9Ip update(LoanIfrs9Ip loanIfrs9Ip, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrs9Ip.getLoanIfrs9IpId());
    if (!empNot.isEmpty())
      loanIfrs9Ip.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrs9IpReposDay.saveAndFlush(loanIfrs9Ip);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrs9IpReposMon.saveAndFlush(loanIfrs9Ip);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrs9IpReposHist.saveAndFlush(loanIfrs9Ip);
    else 
    return loanIfrs9IpRepos.saveAndFlush(loanIfrs9Ip);
  }

  @Override
  public LoanIfrs9Ip update2(LoanIfrs9Ip loanIfrs9Ip, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrs9Ip.getLoanIfrs9IpId());
    if (!empNot.isEmpty())
      loanIfrs9Ip.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanIfrs9IpReposDay.saveAndFlush(loanIfrs9Ip);	
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9IpReposMon.saveAndFlush(loanIfrs9Ip);
    else if (dbName.equals(ContentName.onHist))
        loanIfrs9IpReposHist.saveAndFlush(loanIfrs9Ip);
    else 
      loanIfrs9IpRepos.saveAndFlush(loanIfrs9Ip);	
    return this.findById(loanIfrs9Ip.getLoanIfrs9IpId());
  }

  @Override
  public void delete(LoanIfrs9Ip loanIfrs9Ip, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + loanIfrs9Ip.getLoanIfrs9IpId());
    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9IpReposDay.delete(loanIfrs9Ip);	
      loanIfrs9IpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9IpReposMon.delete(loanIfrs9Ip);	
      loanIfrs9IpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9IpReposHist.delete(loanIfrs9Ip);
      loanIfrs9IpReposHist.flush();
    }
    else {
      loanIfrs9IpRepos.delete(loanIfrs9Ip);
      loanIfrs9IpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanIfrs9Ip> loanIfrs9Ip, TitaVo... titaVo) throws DBException {
    if (loanIfrs9Ip == null || loanIfrs9Ip.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (LoanIfrs9Ip t : loanIfrs9Ip){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9Ip = loanIfrs9IpReposDay.saveAll(loanIfrs9Ip);	
      loanIfrs9IpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9Ip = loanIfrs9IpReposMon.saveAll(loanIfrs9Ip);	
      loanIfrs9IpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9Ip = loanIfrs9IpReposHist.saveAll(loanIfrs9Ip);
      loanIfrs9IpReposHist.flush();
    }
    else {
      loanIfrs9Ip = loanIfrs9IpRepos.saveAll(loanIfrs9Ip);
      loanIfrs9IpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanIfrs9Ip> loanIfrs9Ip, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (loanIfrs9Ip == null || loanIfrs9Ip.size() == 0)
      throw new DBException(6);

    for (LoanIfrs9Ip t : loanIfrs9Ip) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9Ip = loanIfrs9IpReposDay.saveAll(loanIfrs9Ip);	
      loanIfrs9IpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9Ip = loanIfrs9IpReposMon.saveAll(loanIfrs9Ip);	
      loanIfrs9IpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9Ip = loanIfrs9IpReposHist.saveAll(loanIfrs9Ip);
      loanIfrs9IpReposHist.flush();
    }
    else {
      loanIfrs9Ip = loanIfrs9IpRepos.saveAll(loanIfrs9Ip);
      loanIfrs9IpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanIfrs9Ip> loanIfrs9Ip, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanIfrs9Ip == null || loanIfrs9Ip.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9IpReposDay.deleteAll(loanIfrs9Ip);	
      loanIfrs9IpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9IpReposMon.deleteAll(loanIfrs9Ip);	
      loanIfrs9IpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9IpReposHist.deleteAll(loanIfrs9Ip);
      loanIfrs9IpReposHist.flush();
    }
    else {
      loanIfrs9IpRepos.deleteAll(loanIfrs9Ip);
      loanIfrs9IpRepos.flush();
    }
  }

  @Override
  public void Usp_L7_LoanIfrs9Ip_Upd(int TBSDYF, String EmpNo, int NewAcFg, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9IpReposDay.uspL7Loanifrs9ipUpd(TBSDYF, EmpNo, NewAcFg);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9IpReposMon.uspL7Loanifrs9ipUpd(TBSDYF, EmpNo, NewAcFg);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9IpReposHist.uspL7Loanifrs9ipUpd(TBSDYF, EmpNo, NewAcFg);
   else
      loanIfrs9IpRepos.uspL7Loanifrs9ipUpd(TBSDYF, EmpNo, NewAcFg);
  }

}
