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
import com.st1.itx.db.domain.NegFinAcct;
import com.st1.itx.db.repository.online.NegFinAcctRepository;
import com.st1.itx.db.repository.day.NegFinAcctRepositoryDay;
import com.st1.itx.db.repository.mon.NegFinAcctRepositoryMon;
import com.st1.itx.db.repository.hist.NegFinAcctRepositoryHist;
import com.st1.itx.db.service.NegFinAcctService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("negFinAcctService")
@Repository
public class NegFinAcctServiceImpl extends ASpringJpaParm implements NegFinAcctService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private NegFinAcctRepository negFinAcctRepos;

  @Autowired
  private NegFinAcctRepositoryDay negFinAcctReposDay;

  @Autowired
  private NegFinAcctRepositoryMon negFinAcctReposMon;

  @Autowired
  private NegFinAcctRepositoryHist negFinAcctReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(negFinAcctRepos);
    org.junit.Assert.assertNotNull(negFinAcctReposDay);
    org.junit.Assert.assertNotNull(negFinAcctReposMon);
    org.junit.Assert.assertNotNull(negFinAcctReposHist);
  }

  @Override
  public NegFinAcct findById(String finCode, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + finCode);
    Optional<NegFinAcct> negFinAcct = null;
    if (dbName.equals(ContentName.onDay))
      negFinAcct = negFinAcctReposDay.findById(finCode);
    else if (dbName.equals(ContentName.onMon))
      negFinAcct = negFinAcctReposMon.findById(finCode);
    else if (dbName.equals(ContentName.onHist))
      negFinAcct = negFinAcctReposHist.findById(finCode);
    else 
      negFinAcct = negFinAcctRepos.findById(finCode);
    NegFinAcct obj = negFinAcct.isPresent() ? negFinAcct.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<NegFinAcct> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegFinAcct> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "FinCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "FinCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = negFinAcctReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negFinAcctReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negFinAcctReposHist.findAll(pageable);
    else 
      slice = negFinAcctRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public NegFinAcct holdById(String finCode, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + finCode);
    Optional<NegFinAcct> negFinAcct = null;
    if (dbName.equals(ContentName.onDay))
      negFinAcct = negFinAcctReposDay.findByFinCode(finCode);
    else if (dbName.equals(ContentName.onMon))
      negFinAcct = negFinAcctReposMon.findByFinCode(finCode);
    else if (dbName.equals(ContentName.onHist))
      negFinAcct = negFinAcctReposHist.findByFinCode(finCode);
    else 
      negFinAcct = negFinAcctRepos.findByFinCode(finCode);
    return negFinAcct.isPresent() ? negFinAcct.get() : null;
  }

  @Override
  public NegFinAcct holdById(NegFinAcct negFinAcct, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + negFinAcct.getFinCode());
    Optional<NegFinAcct> negFinAcctT = null;
    if (dbName.equals(ContentName.onDay))
      negFinAcctT = negFinAcctReposDay.findByFinCode(negFinAcct.getFinCode());
    else if (dbName.equals(ContentName.onMon))
      negFinAcctT = negFinAcctReposMon.findByFinCode(negFinAcct.getFinCode());
    else if (dbName.equals(ContentName.onHist))
      negFinAcctT = negFinAcctReposHist.findByFinCode(negFinAcct.getFinCode());
    else 
      negFinAcctT = negFinAcctRepos.findByFinCode(negFinAcct.getFinCode());
    return negFinAcctT.isPresent() ? negFinAcctT.get() : null;
  }

  @Override
  public NegFinAcct insert(NegFinAcct negFinAcct, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + negFinAcct.getFinCode());
    if (this.findById(negFinAcct.getFinCode(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      negFinAcct.setCreateEmpNo(empNot);

    if(negFinAcct.getLastUpdateEmpNo() == null || negFinAcct.getLastUpdateEmpNo().isEmpty())
      negFinAcct.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return negFinAcctReposDay.saveAndFlush(negFinAcct);	
    else if (dbName.equals(ContentName.onMon))
      return negFinAcctReposMon.saveAndFlush(negFinAcct);
    else if (dbName.equals(ContentName.onHist))
      return negFinAcctReposHist.saveAndFlush(negFinAcct);
    else 
    return negFinAcctRepos.saveAndFlush(negFinAcct);
  }

  @Override
  public NegFinAcct update(NegFinAcct negFinAcct, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + negFinAcct.getFinCode());
    if (!empNot.isEmpty())
      negFinAcct.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return negFinAcctReposDay.saveAndFlush(negFinAcct);	
    else if (dbName.equals(ContentName.onMon))
      return negFinAcctReposMon.saveAndFlush(negFinAcct);
    else if (dbName.equals(ContentName.onHist))
      return negFinAcctReposHist.saveAndFlush(negFinAcct);
    else 
    return negFinAcctRepos.saveAndFlush(negFinAcct);
  }

  @Override
  public NegFinAcct update2(NegFinAcct negFinAcct, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + negFinAcct.getFinCode());
    if (!empNot.isEmpty())
      negFinAcct.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      negFinAcctReposDay.saveAndFlush(negFinAcct);	
    else if (dbName.equals(ContentName.onMon))
      negFinAcctReposMon.saveAndFlush(negFinAcct);
    else if (dbName.equals(ContentName.onHist))
        negFinAcctReposHist.saveAndFlush(negFinAcct);
    else 
      negFinAcctRepos.saveAndFlush(negFinAcct);	
    return this.findById(negFinAcct.getFinCode());
  }

  @Override
  public void delete(NegFinAcct negFinAcct, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + negFinAcct.getFinCode());
    if (dbName.equals(ContentName.onDay)) {
      negFinAcctReposDay.delete(negFinAcct);	
      negFinAcctReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negFinAcctReposMon.delete(negFinAcct);	
      negFinAcctReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negFinAcctReposHist.delete(negFinAcct);
      negFinAcctReposHist.flush();
    }
    else {
      negFinAcctRepos.delete(negFinAcct);
      negFinAcctRepos.flush();
    }
   }

  @Override
  public void insertAll(List<NegFinAcct> negFinAcct, TitaVo... titaVo) throws DBException {
    if (negFinAcct == null || negFinAcct.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (NegFinAcct t : negFinAcct){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      negFinAcct = negFinAcctReposDay.saveAll(negFinAcct);	
      negFinAcctReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negFinAcct = negFinAcctReposMon.saveAll(negFinAcct);	
      negFinAcctReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negFinAcct = negFinAcctReposHist.saveAll(negFinAcct);
      negFinAcctReposHist.flush();
    }
    else {
      negFinAcct = negFinAcctRepos.saveAll(negFinAcct);
      negFinAcctRepos.flush();
    }
    }

  @Override
  public void updateAll(List<NegFinAcct> negFinAcct, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (negFinAcct == null || negFinAcct.size() == 0)
      throw new DBException(6);

    for (NegFinAcct t : negFinAcct) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      negFinAcct = negFinAcctReposDay.saveAll(negFinAcct);	
      negFinAcctReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negFinAcct = negFinAcctReposMon.saveAll(negFinAcct);	
      negFinAcctReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negFinAcct = negFinAcctReposHist.saveAll(negFinAcct);
      negFinAcctReposHist.flush();
    }
    else {
      negFinAcct = negFinAcctRepos.saveAll(negFinAcct);
      negFinAcctRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<NegFinAcct> negFinAcct, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (negFinAcct == null || negFinAcct.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      negFinAcctReposDay.deleteAll(negFinAcct);	
      negFinAcctReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negFinAcctReposMon.deleteAll(negFinAcct);	
      negFinAcctReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negFinAcctReposHist.deleteAll(negFinAcct);
      negFinAcctReposHist.flush();
    }
    else {
      negFinAcctRepos.deleteAll(negFinAcct);
      negFinAcctRepos.flush();
    }
  }

}
