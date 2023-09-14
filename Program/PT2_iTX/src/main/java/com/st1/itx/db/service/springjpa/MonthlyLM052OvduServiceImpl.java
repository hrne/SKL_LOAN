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
import com.st1.itx.db.domain.MonthlyLM052Ovdu;
import com.st1.itx.db.domain.MonthlyLM052OvduId;
import com.st1.itx.db.repository.online.MonthlyLM052OvduRepository;
import com.st1.itx.db.repository.day.MonthlyLM052OvduRepositoryDay;
import com.st1.itx.db.repository.mon.MonthlyLM052OvduRepositoryMon;
import com.st1.itx.db.repository.hist.MonthlyLM052OvduRepositoryHist;
import com.st1.itx.db.service.MonthlyLM052OvduService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("monthlyLM052OvduService")
@Repository
public class MonthlyLM052OvduServiceImpl extends ASpringJpaParm implements MonthlyLM052OvduService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private MonthlyLM052OvduRepository monthlyLM052OvduRepos;

  @Autowired
  private MonthlyLM052OvduRepositoryDay monthlyLM052OvduReposDay;

  @Autowired
  private MonthlyLM052OvduRepositoryMon monthlyLM052OvduReposMon;

  @Autowired
  private MonthlyLM052OvduRepositoryHist monthlyLM052OvduReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(monthlyLM052OvduRepos);
    org.junit.Assert.assertNotNull(monthlyLM052OvduReposDay);
    org.junit.Assert.assertNotNull(monthlyLM052OvduReposMon);
    org.junit.Assert.assertNotNull(monthlyLM052OvduReposHist);
  }

  @Override
  public MonthlyLM052Ovdu findById(MonthlyLM052OvduId monthlyLM052OvduId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + monthlyLM052OvduId);
    Optional<MonthlyLM052Ovdu> monthlyLM052Ovdu = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM052Ovdu = monthlyLM052OvduReposDay.findById(monthlyLM052OvduId);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052Ovdu = monthlyLM052OvduReposMon.findById(monthlyLM052OvduId);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM052Ovdu = monthlyLM052OvduReposHist.findById(monthlyLM052OvduId);
    else 
      monthlyLM052Ovdu = monthlyLM052OvduRepos.findById(monthlyLM052OvduId);
    MonthlyLM052Ovdu obj = monthlyLM052Ovdu.isPresent() ? monthlyLM052Ovdu.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<MonthlyLM052Ovdu> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MonthlyLM052Ovdu> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "YearMonth", "OvduNo", "AcctCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "YearMonth", "OvduNo", "AcctCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = monthlyLM052OvduReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = monthlyLM052OvduReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = monthlyLM052OvduReposHist.findAll(pageable);
    else 
      slice = monthlyLM052OvduRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public MonthlyLM052Ovdu holdById(MonthlyLM052OvduId monthlyLM052OvduId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + monthlyLM052OvduId);
    Optional<MonthlyLM052Ovdu> monthlyLM052Ovdu = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM052Ovdu = monthlyLM052OvduReposDay.findByMonthlyLM052OvduId(monthlyLM052OvduId);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052Ovdu = monthlyLM052OvduReposMon.findByMonthlyLM052OvduId(monthlyLM052OvduId);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM052Ovdu = monthlyLM052OvduReposHist.findByMonthlyLM052OvduId(monthlyLM052OvduId);
    else 
      monthlyLM052Ovdu = monthlyLM052OvduRepos.findByMonthlyLM052OvduId(monthlyLM052OvduId);
    return monthlyLM052Ovdu.isPresent() ? monthlyLM052Ovdu.get() : null;
  }

  @Override
  public MonthlyLM052Ovdu holdById(MonthlyLM052Ovdu monthlyLM052Ovdu, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + monthlyLM052Ovdu.getMonthlyLM052OvduId());
    Optional<MonthlyLM052Ovdu> monthlyLM052OvduT = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM052OvduT = monthlyLM052OvduReposDay.findByMonthlyLM052OvduId(monthlyLM052Ovdu.getMonthlyLM052OvduId());
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052OvduT = monthlyLM052OvduReposMon.findByMonthlyLM052OvduId(monthlyLM052Ovdu.getMonthlyLM052OvduId());
    else if (dbName.equals(ContentName.onHist))
      monthlyLM052OvduT = monthlyLM052OvduReposHist.findByMonthlyLM052OvduId(monthlyLM052Ovdu.getMonthlyLM052OvduId());
    else 
      monthlyLM052OvduT = monthlyLM052OvduRepos.findByMonthlyLM052OvduId(monthlyLM052Ovdu.getMonthlyLM052OvduId());
    return monthlyLM052OvduT.isPresent() ? monthlyLM052OvduT.get() : null;
  }

  @Override
  public MonthlyLM052Ovdu insert(MonthlyLM052Ovdu monthlyLM052Ovdu, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + monthlyLM052Ovdu.getMonthlyLM052OvduId());
    if (this.findById(monthlyLM052Ovdu.getMonthlyLM052OvduId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      monthlyLM052Ovdu.setCreateEmpNo(empNot);

    if(monthlyLM052Ovdu.getLastUpdateEmpNo() == null || monthlyLM052Ovdu.getLastUpdateEmpNo().isEmpty())
      monthlyLM052Ovdu.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return monthlyLM052OvduReposDay.saveAndFlush(monthlyLM052Ovdu);	
    else if (dbName.equals(ContentName.onMon))
      return monthlyLM052OvduReposMon.saveAndFlush(monthlyLM052Ovdu);
    else if (dbName.equals(ContentName.onHist))
      return monthlyLM052OvduReposHist.saveAndFlush(monthlyLM052Ovdu);
    else 
    return monthlyLM052OvduRepos.saveAndFlush(monthlyLM052Ovdu);
  }

  @Override
  public MonthlyLM052Ovdu update(MonthlyLM052Ovdu monthlyLM052Ovdu, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + monthlyLM052Ovdu.getMonthlyLM052OvduId());
    if (!empNot.isEmpty())
      monthlyLM052Ovdu.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return monthlyLM052OvduReposDay.saveAndFlush(monthlyLM052Ovdu);	
    else if (dbName.equals(ContentName.onMon))
      return monthlyLM052OvduReposMon.saveAndFlush(monthlyLM052Ovdu);
    else if (dbName.equals(ContentName.onHist))
      return monthlyLM052OvduReposHist.saveAndFlush(monthlyLM052Ovdu);
    else 
    return monthlyLM052OvduRepos.saveAndFlush(monthlyLM052Ovdu);
  }

  @Override
  public MonthlyLM052Ovdu update2(MonthlyLM052Ovdu monthlyLM052Ovdu, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + monthlyLM052Ovdu.getMonthlyLM052OvduId());
    if (!empNot.isEmpty())
      monthlyLM052Ovdu.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      monthlyLM052OvduReposDay.saveAndFlush(monthlyLM052Ovdu);	
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052OvduReposMon.saveAndFlush(monthlyLM052Ovdu);
    else if (dbName.equals(ContentName.onHist))
        monthlyLM052OvduReposHist.saveAndFlush(monthlyLM052Ovdu);
    else 
      monthlyLM052OvduRepos.saveAndFlush(monthlyLM052Ovdu);	
    return this.findById(monthlyLM052Ovdu.getMonthlyLM052OvduId());
  }

  @Override
  public void delete(MonthlyLM052Ovdu monthlyLM052Ovdu, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + monthlyLM052Ovdu.getMonthlyLM052OvduId());
    if (dbName.equals(ContentName.onDay)) {
      monthlyLM052OvduReposDay.delete(monthlyLM052Ovdu);	
      monthlyLM052OvduReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM052OvduReposMon.delete(monthlyLM052Ovdu);	
      monthlyLM052OvduReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM052OvduReposHist.delete(monthlyLM052Ovdu);
      monthlyLM052OvduReposHist.flush();
    }
    else {
      monthlyLM052OvduRepos.delete(monthlyLM052Ovdu);
      monthlyLM052OvduRepos.flush();
    }
   }

  @Override
  public void insertAll(List<MonthlyLM052Ovdu> monthlyLM052Ovdu, TitaVo... titaVo) throws DBException {
    if (monthlyLM052Ovdu == null || monthlyLM052Ovdu.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (MonthlyLM052Ovdu t : monthlyLM052Ovdu){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      monthlyLM052Ovdu = monthlyLM052OvduReposDay.saveAll(monthlyLM052Ovdu);	
      monthlyLM052OvduReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM052Ovdu = monthlyLM052OvduReposMon.saveAll(monthlyLM052Ovdu);	
      monthlyLM052OvduReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM052Ovdu = monthlyLM052OvduReposHist.saveAll(monthlyLM052Ovdu);
      monthlyLM052OvduReposHist.flush();
    }
    else {
      monthlyLM052Ovdu = monthlyLM052OvduRepos.saveAll(monthlyLM052Ovdu);
      monthlyLM052OvduRepos.flush();
    }
    }

  @Override
  public void updateAll(List<MonthlyLM052Ovdu> monthlyLM052Ovdu, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (monthlyLM052Ovdu == null || monthlyLM052Ovdu.size() == 0)
      throw new DBException(6);

    for (MonthlyLM052Ovdu t : monthlyLM052Ovdu) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      monthlyLM052Ovdu = monthlyLM052OvduReposDay.saveAll(monthlyLM052Ovdu);	
      monthlyLM052OvduReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM052Ovdu = monthlyLM052OvduReposMon.saveAll(monthlyLM052Ovdu);	
      monthlyLM052OvduReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM052Ovdu = monthlyLM052OvduReposHist.saveAll(monthlyLM052Ovdu);
      monthlyLM052OvduReposHist.flush();
    }
    else {
      monthlyLM052Ovdu = monthlyLM052OvduRepos.saveAll(monthlyLM052Ovdu);
      monthlyLM052OvduRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<MonthlyLM052Ovdu> monthlyLM052Ovdu, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (monthlyLM052Ovdu == null || monthlyLM052Ovdu.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      monthlyLM052OvduReposDay.deleteAll(monthlyLM052Ovdu);	
      monthlyLM052OvduReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM052OvduReposMon.deleteAll(monthlyLM052Ovdu);	
      monthlyLM052OvduReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM052OvduReposHist.deleteAll(monthlyLM052Ovdu);
      monthlyLM052OvduReposHist.flush();
    }
    else {
      monthlyLM052OvduRepos.deleteAll(monthlyLM052Ovdu);
      monthlyLM052OvduRepos.flush();
    }
  }

  @Override
  public void Usp_L9_MonthlyLM052Ovdu_Ins(int tbsdyf,  String empNo,  String jobTxSeq, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      monthlyLM052OvduReposDay.uspL9Monthlylm052ovduIns(tbsdyf,  empNo,  jobTxSeq);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052OvduReposMon.uspL9Monthlylm052ovduIns(tbsdyf,  empNo,  jobTxSeq);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM052OvduReposHist.uspL9Monthlylm052ovduIns(tbsdyf,  empNo,  jobTxSeq);
   else
      monthlyLM052OvduRepos.uspL9Monthlylm052ovduIns(tbsdyf,  empNo,  jobTxSeq);
  }

}
