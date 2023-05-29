package com.st1.itx.db.service.springjpa;

import java.util.ArrayList;
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
import com.st1.itx.db.domain.TxFtpUser;
import com.st1.itx.db.repository.online.TxFtpUserRepository;
import com.st1.itx.db.repository.day.TxFtpUserRepositoryDay;
import com.st1.itx.db.repository.mon.TxFtpUserRepositoryMon;
import com.st1.itx.db.repository.hist.TxFtpUserRepositoryHist;
import com.st1.itx.db.service.TxFtpUserService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txFtpUserService")
@Repository
public class TxFtpUserServiceImpl extends ASpringJpaParm implements TxFtpUserService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxFtpUserRepository txFtpUserRepos;

  @Autowired
  private TxFtpUserRepositoryDay txFtpUserReposDay;

  @Autowired
  private TxFtpUserRepositoryMon txFtpUserReposMon;

  @Autowired
  private TxFtpUserRepositoryHist txFtpUserReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txFtpUserRepos);
    org.junit.Assert.assertNotNull(txFtpUserReposDay);
    org.junit.Assert.assertNotNull(txFtpUserReposMon);
    org.junit.Assert.assertNotNull(txFtpUserReposHist);
  }

  @Override
  public TxFtpUser findById(String userId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + userId);
    Optional<TxFtpUser> txFtpUser = null;
    if (dbName.equals(ContentName.onDay))
      txFtpUser = txFtpUserReposDay.findById(userId);
    else if (dbName.equals(ContentName.onMon))
      txFtpUser = txFtpUserReposMon.findById(userId);
    else if (dbName.equals(ContentName.onHist))
      txFtpUser = txFtpUserReposHist.findById(userId);
    else 
      txFtpUser = txFtpUserRepos.findById(userId);
    TxFtpUser obj = txFtpUser.isPresent() ? txFtpUser.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxFtpUser> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxFtpUser> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "UserId"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "UserId"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txFtpUserReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txFtpUserReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txFtpUserReposHist.findAll(pageable);
    else 
      slice = txFtpUserRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxFtpUser holdById(String userId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + userId);
    Optional<TxFtpUser> txFtpUser = null;
    if (dbName.equals(ContentName.onDay))
      txFtpUser = txFtpUserReposDay.findByUserId(userId);
    else if (dbName.equals(ContentName.onMon))
      txFtpUser = txFtpUserReposMon.findByUserId(userId);
    else if (dbName.equals(ContentName.onHist))
      txFtpUser = txFtpUserReposHist.findByUserId(userId);
    else 
      txFtpUser = txFtpUserRepos.findByUserId(userId);
    return txFtpUser.isPresent() ? txFtpUser.get() : null;
  }

  @Override
  public TxFtpUser holdById(TxFtpUser txFtpUser, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txFtpUser.getUserId());
    Optional<TxFtpUser> txFtpUserT = null;
    if (dbName.equals(ContentName.onDay))
      txFtpUserT = txFtpUserReposDay.findByUserId(txFtpUser.getUserId());
    else if (dbName.equals(ContentName.onMon))
      txFtpUserT = txFtpUserReposMon.findByUserId(txFtpUser.getUserId());
    else if (dbName.equals(ContentName.onHist))
      txFtpUserT = txFtpUserReposHist.findByUserId(txFtpUser.getUserId());
    else 
      txFtpUserT = txFtpUserRepos.findByUserId(txFtpUser.getUserId());
    return txFtpUserT.isPresent() ? txFtpUserT.get() : null;
  }

  @Override
  public TxFtpUser insert(TxFtpUser txFtpUser, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + txFtpUser.getUserId());
    if (this.findById(txFtpUser.getUserId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txFtpUser.setCreateEmpNo(empNot);

    if(txFtpUser.getLastUpdateEmpNo() == null || txFtpUser.getLastUpdateEmpNo().isEmpty())
      txFtpUser.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txFtpUserReposDay.saveAndFlush(txFtpUser);	
    else if (dbName.equals(ContentName.onMon))
      return txFtpUserReposMon.saveAndFlush(txFtpUser);
    else if (dbName.equals(ContentName.onHist))
      return txFtpUserReposHist.saveAndFlush(txFtpUser);
    else 
    return txFtpUserRepos.saveAndFlush(txFtpUser);
  }

  @Override
  public TxFtpUser update(TxFtpUser txFtpUser, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txFtpUser.getUserId());
    if (!empNot.isEmpty())
      txFtpUser.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txFtpUserReposDay.saveAndFlush(txFtpUser);	
    else if (dbName.equals(ContentName.onMon))
      return txFtpUserReposMon.saveAndFlush(txFtpUser);
    else if (dbName.equals(ContentName.onHist))
      return txFtpUserReposHist.saveAndFlush(txFtpUser);
    else 
    return txFtpUserRepos.saveAndFlush(txFtpUser);
  }

  @Override
  public TxFtpUser update2(TxFtpUser txFtpUser, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txFtpUser.getUserId());
    if (!empNot.isEmpty())
      txFtpUser.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txFtpUserReposDay.saveAndFlush(txFtpUser);	
    else if (dbName.equals(ContentName.onMon))
      txFtpUserReposMon.saveAndFlush(txFtpUser);
    else if (dbName.equals(ContentName.onHist))
        txFtpUserReposHist.saveAndFlush(txFtpUser);
    else 
      txFtpUserRepos.saveAndFlush(txFtpUser);	
    return this.findById(txFtpUser.getUserId());
  }

  @Override
  public void delete(TxFtpUser txFtpUser, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + txFtpUser.getUserId());
    if (dbName.equals(ContentName.onDay)) {
      txFtpUserReposDay.delete(txFtpUser);	
      txFtpUserReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txFtpUserReposMon.delete(txFtpUser);	
      txFtpUserReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txFtpUserReposHist.delete(txFtpUser);
      txFtpUserReposHist.flush();
    }
    else {
      txFtpUserRepos.delete(txFtpUser);
      txFtpUserRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxFtpUser> txFtpUser, TitaVo... titaVo) throws DBException {
    if (txFtpUser == null || txFtpUser.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (TxFtpUser t : txFtpUser){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      txFtpUser = txFtpUserReposDay.saveAll(txFtpUser);	
      txFtpUserReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txFtpUser = txFtpUserReposMon.saveAll(txFtpUser);	
      txFtpUserReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txFtpUser = txFtpUserReposHist.saveAll(txFtpUser);
      txFtpUserReposHist.flush();
    }
    else {
      txFtpUser = txFtpUserRepos.saveAll(txFtpUser);
      txFtpUserRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxFtpUser> txFtpUser, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (txFtpUser == null || txFtpUser.size() == 0)
      throw new DBException(6);

    for (TxFtpUser t : txFtpUser) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txFtpUser = txFtpUserReposDay.saveAll(txFtpUser);	
      txFtpUserReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txFtpUser = txFtpUserReposMon.saveAll(txFtpUser);	
      txFtpUserReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txFtpUser = txFtpUserReposHist.saveAll(txFtpUser);
      txFtpUserReposHist.flush();
    }
    else {
      txFtpUser = txFtpUserRepos.saveAll(txFtpUser);
      txFtpUserRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxFtpUser> txFtpUser, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txFtpUser == null || txFtpUser.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txFtpUserReposDay.deleteAll(txFtpUser);	
      txFtpUserReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txFtpUserReposMon.deleteAll(txFtpUser);	
      txFtpUserReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txFtpUserReposHist.deleteAll(txFtpUser);
      txFtpUserReposHist.flush();
    }
    else {
      txFtpUserRepos.deleteAll(txFtpUser);
      txFtpUserRepos.flush();
    }
  }

}
