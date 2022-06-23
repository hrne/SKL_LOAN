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
import com.st1.itx.db.domain.JcicMonthlyLoanData;
import com.st1.itx.db.domain.JcicMonthlyLoanDataId;
import com.st1.itx.db.repository.online.JcicMonthlyLoanDataRepository;
import com.st1.itx.db.repository.day.JcicMonthlyLoanDataRepositoryDay;
import com.st1.itx.db.repository.mon.JcicMonthlyLoanDataRepositoryMon;
import com.st1.itx.db.repository.hist.JcicMonthlyLoanDataRepositoryHist;
import com.st1.itx.db.service.JcicMonthlyLoanDataService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicMonthlyLoanDataService")
@Repository
public class JcicMonthlyLoanDataServiceImpl extends ASpringJpaParm implements JcicMonthlyLoanDataService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicMonthlyLoanDataRepository jcicMonthlyLoanDataRepos;

  @Autowired
  private JcicMonthlyLoanDataRepositoryDay jcicMonthlyLoanDataReposDay;

  @Autowired
  private JcicMonthlyLoanDataRepositoryMon jcicMonthlyLoanDataReposMon;

  @Autowired
  private JcicMonthlyLoanDataRepositoryHist jcicMonthlyLoanDataReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicMonthlyLoanDataRepos);
    org.junit.Assert.assertNotNull(jcicMonthlyLoanDataReposDay);
    org.junit.Assert.assertNotNull(jcicMonthlyLoanDataReposMon);
    org.junit.Assert.assertNotNull(jcicMonthlyLoanDataReposHist);
  }

  @Override
  public JcicMonthlyLoanData findById(JcicMonthlyLoanDataId jcicMonthlyLoanDataId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicMonthlyLoanDataId);
    Optional<JcicMonthlyLoanData> jcicMonthlyLoanData = null;
    if (dbName.equals(ContentName.onDay))
      jcicMonthlyLoanData = jcicMonthlyLoanDataReposDay.findById(jcicMonthlyLoanDataId);
    else if (dbName.equals(ContentName.onMon))
      jcicMonthlyLoanData = jcicMonthlyLoanDataReposMon.findById(jcicMonthlyLoanDataId);
    else if (dbName.equals(ContentName.onHist))
      jcicMonthlyLoanData = jcicMonthlyLoanDataReposHist.findById(jcicMonthlyLoanDataId);
    else 
      jcicMonthlyLoanData = jcicMonthlyLoanDataRepos.findById(jcicMonthlyLoanDataId);
    JcicMonthlyLoanData obj = jcicMonthlyLoanData.isPresent() ? jcicMonthlyLoanData.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicMonthlyLoanData> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicMonthlyLoanData> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicMonthlyLoanDataReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicMonthlyLoanDataReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicMonthlyLoanDataReposHist.findAll(pageable);
    else 
      slice = jcicMonthlyLoanDataRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicMonthlyLoanData holdById(JcicMonthlyLoanDataId jcicMonthlyLoanDataId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicMonthlyLoanDataId);
    Optional<JcicMonthlyLoanData> jcicMonthlyLoanData = null;
    if (dbName.equals(ContentName.onDay))
      jcicMonthlyLoanData = jcicMonthlyLoanDataReposDay.findByJcicMonthlyLoanDataId(jcicMonthlyLoanDataId);
    else if (dbName.equals(ContentName.onMon))
      jcicMonthlyLoanData = jcicMonthlyLoanDataReposMon.findByJcicMonthlyLoanDataId(jcicMonthlyLoanDataId);
    else if (dbName.equals(ContentName.onHist))
      jcicMonthlyLoanData = jcicMonthlyLoanDataReposHist.findByJcicMonthlyLoanDataId(jcicMonthlyLoanDataId);
    else 
      jcicMonthlyLoanData = jcicMonthlyLoanDataRepos.findByJcicMonthlyLoanDataId(jcicMonthlyLoanDataId);
    return jcicMonthlyLoanData.isPresent() ? jcicMonthlyLoanData.get() : null;
  }

  @Override
  public JcicMonthlyLoanData holdById(JcicMonthlyLoanData jcicMonthlyLoanData, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicMonthlyLoanData.getJcicMonthlyLoanDataId());
    Optional<JcicMonthlyLoanData> jcicMonthlyLoanDataT = null;
    if (dbName.equals(ContentName.onDay))
      jcicMonthlyLoanDataT = jcicMonthlyLoanDataReposDay.findByJcicMonthlyLoanDataId(jcicMonthlyLoanData.getJcicMonthlyLoanDataId());
    else if (dbName.equals(ContentName.onMon))
      jcicMonthlyLoanDataT = jcicMonthlyLoanDataReposMon.findByJcicMonthlyLoanDataId(jcicMonthlyLoanData.getJcicMonthlyLoanDataId());
    else if (dbName.equals(ContentName.onHist))
      jcicMonthlyLoanDataT = jcicMonthlyLoanDataReposHist.findByJcicMonthlyLoanDataId(jcicMonthlyLoanData.getJcicMonthlyLoanDataId());
    else 
      jcicMonthlyLoanDataT = jcicMonthlyLoanDataRepos.findByJcicMonthlyLoanDataId(jcicMonthlyLoanData.getJcicMonthlyLoanDataId());
    return jcicMonthlyLoanDataT.isPresent() ? jcicMonthlyLoanDataT.get() : null;
  }

  @Override
  public JcicMonthlyLoanData insert(JcicMonthlyLoanData jcicMonthlyLoanData, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicMonthlyLoanData.getJcicMonthlyLoanDataId());
    if (this.findById(jcicMonthlyLoanData.getJcicMonthlyLoanDataId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicMonthlyLoanData.setCreateEmpNo(empNot);

    if(jcicMonthlyLoanData.getLastUpdateEmpNo() == null || jcicMonthlyLoanData.getLastUpdateEmpNo().isEmpty())
      jcicMonthlyLoanData.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicMonthlyLoanDataReposDay.saveAndFlush(jcicMonthlyLoanData);	
    else if (dbName.equals(ContentName.onMon))
      return jcicMonthlyLoanDataReposMon.saveAndFlush(jcicMonthlyLoanData);
    else if (dbName.equals(ContentName.onHist))
      return jcicMonthlyLoanDataReposHist.saveAndFlush(jcicMonthlyLoanData);
    else 
    return jcicMonthlyLoanDataRepos.saveAndFlush(jcicMonthlyLoanData);
  }

  @Override
  public JcicMonthlyLoanData update(JcicMonthlyLoanData jcicMonthlyLoanData, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicMonthlyLoanData.getJcicMonthlyLoanDataId());
    if (!empNot.isEmpty())
      jcicMonthlyLoanData.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicMonthlyLoanDataReposDay.saveAndFlush(jcicMonthlyLoanData);	
    else if (dbName.equals(ContentName.onMon))
      return jcicMonthlyLoanDataReposMon.saveAndFlush(jcicMonthlyLoanData);
    else if (dbName.equals(ContentName.onHist))
      return jcicMonthlyLoanDataReposHist.saveAndFlush(jcicMonthlyLoanData);
    else 
    return jcicMonthlyLoanDataRepos.saveAndFlush(jcicMonthlyLoanData);
  }

  @Override
  public JcicMonthlyLoanData update2(JcicMonthlyLoanData jcicMonthlyLoanData, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicMonthlyLoanData.getJcicMonthlyLoanDataId());
    if (!empNot.isEmpty())
      jcicMonthlyLoanData.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicMonthlyLoanDataReposDay.saveAndFlush(jcicMonthlyLoanData);	
    else if (dbName.equals(ContentName.onMon))
      jcicMonthlyLoanDataReposMon.saveAndFlush(jcicMonthlyLoanData);
    else if (dbName.equals(ContentName.onHist))
        jcicMonthlyLoanDataReposHist.saveAndFlush(jcicMonthlyLoanData);
    else 
      jcicMonthlyLoanDataRepos.saveAndFlush(jcicMonthlyLoanData);	
    return this.findById(jcicMonthlyLoanData.getJcicMonthlyLoanDataId());
  }

  @Override
  public void delete(JcicMonthlyLoanData jcicMonthlyLoanData, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicMonthlyLoanData.getJcicMonthlyLoanDataId());
    if (dbName.equals(ContentName.onDay)) {
      jcicMonthlyLoanDataReposDay.delete(jcicMonthlyLoanData);	
      jcicMonthlyLoanDataReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicMonthlyLoanDataReposMon.delete(jcicMonthlyLoanData);	
      jcicMonthlyLoanDataReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicMonthlyLoanDataReposHist.delete(jcicMonthlyLoanData);
      jcicMonthlyLoanDataReposHist.flush();
    }
    else {
      jcicMonthlyLoanDataRepos.delete(jcicMonthlyLoanData);
      jcicMonthlyLoanDataRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicMonthlyLoanData> jcicMonthlyLoanData, TitaVo... titaVo) throws DBException {
    if (jcicMonthlyLoanData == null || jcicMonthlyLoanData.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicMonthlyLoanData t : jcicMonthlyLoanData){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicMonthlyLoanData = jcicMonthlyLoanDataReposDay.saveAll(jcicMonthlyLoanData);	
      jcicMonthlyLoanDataReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicMonthlyLoanData = jcicMonthlyLoanDataReposMon.saveAll(jcicMonthlyLoanData);	
      jcicMonthlyLoanDataReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicMonthlyLoanData = jcicMonthlyLoanDataReposHist.saveAll(jcicMonthlyLoanData);
      jcicMonthlyLoanDataReposHist.flush();
    }
    else {
      jcicMonthlyLoanData = jcicMonthlyLoanDataRepos.saveAll(jcicMonthlyLoanData);
      jcicMonthlyLoanDataRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicMonthlyLoanData> jcicMonthlyLoanData, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicMonthlyLoanData == null || jcicMonthlyLoanData.size() == 0)
      throw new DBException(6);

    for (JcicMonthlyLoanData t : jcicMonthlyLoanData) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicMonthlyLoanData = jcicMonthlyLoanDataReposDay.saveAll(jcicMonthlyLoanData);	
      jcicMonthlyLoanDataReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicMonthlyLoanData = jcicMonthlyLoanDataReposMon.saveAll(jcicMonthlyLoanData);	
      jcicMonthlyLoanDataReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicMonthlyLoanData = jcicMonthlyLoanDataReposHist.saveAll(jcicMonthlyLoanData);
      jcicMonthlyLoanDataReposHist.flush();
    }
    else {
      jcicMonthlyLoanData = jcicMonthlyLoanDataRepos.saveAll(jcicMonthlyLoanData);
      jcicMonthlyLoanDataRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicMonthlyLoanData> jcicMonthlyLoanData, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicMonthlyLoanData == null || jcicMonthlyLoanData.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicMonthlyLoanDataReposDay.deleteAll(jcicMonthlyLoanData);	
      jcicMonthlyLoanDataReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicMonthlyLoanDataReposMon.deleteAll(jcicMonthlyLoanData);	
      jcicMonthlyLoanDataReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicMonthlyLoanDataReposHist.deleteAll(jcicMonthlyLoanData);
      jcicMonthlyLoanDataReposHist.flush();
    }
    else {
      jcicMonthlyLoanDataRepos.deleteAll(jcicMonthlyLoanData);
      jcicMonthlyLoanDataRepos.flush();
    }
  }

  @Override
  public void Usp_L8_JcicMonthlyLoanData_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jcicMonthlyLoanDataReposDay.uspL8JcicmonthlyloandataUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jcicMonthlyLoanDataReposMon.uspL8JcicmonthlyloandataUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jcicMonthlyLoanDataReposHist.uspL8JcicmonthlyloandataUpd(TBSDYF, EmpNo);
   else
      jcicMonthlyLoanDataRepos.uspL8JcicmonthlyloandataUpd(TBSDYF, EmpNo);
  }

}
