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
import com.st1.itx.db.domain.PfBsDetailAdjust;
import com.st1.itx.db.repository.online.PfBsDetailAdjustRepository;
import com.st1.itx.db.repository.day.PfBsDetailAdjustRepositoryDay;
import com.st1.itx.db.repository.mon.PfBsDetailAdjustRepositoryMon;
import com.st1.itx.db.repository.hist.PfBsDetailAdjustRepositoryHist;
import com.st1.itx.db.service.PfBsDetailAdjustService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("pfBsDetailAdjustService")
@Repository
public class PfBsDetailAdjustServiceImpl extends ASpringJpaParm implements PfBsDetailAdjustService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private PfBsDetailAdjustRepository pfBsDetailAdjustRepos;

  @Autowired
  private PfBsDetailAdjustRepositoryDay pfBsDetailAdjustReposDay;

  @Autowired
  private PfBsDetailAdjustRepositoryMon pfBsDetailAdjustReposMon;

  @Autowired
  private PfBsDetailAdjustRepositoryHist pfBsDetailAdjustReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(pfBsDetailAdjustRepos);
    org.junit.Assert.assertNotNull(pfBsDetailAdjustReposDay);
    org.junit.Assert.assertNotNull(pfBsDetailAdjustReposMon);
    org.junit.Assert.assertNotNull(pfBsDetailAdjustReposHist);
  }

  @Override
  public PfBsDetailAdjust findById(Long logNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + logNo);
    Optional<PfBsDetailAdjust> pfBsDetailAdjust = null;
    if (dbName.equals(ContentName.onDay))
      pfBsDetailAdjust = pfBsDetailAdjustReposDay.findById(logNo);
    else if (dbName.equals(ContentName.onMon))
      pfBsDetailAdjust = pfBsDetailAdjustReposMon.findById(logNo);
    else if (dbName.equals(ContentName.onHist))
      pfBsDetailAdjust = pfBsDetailAdjustReposHist.findById(logNo);
    else 
      pfBsDetailAdjust = pfBsDetailAdjustRepos.findById(logNo);
    PfBsDetailAdjust obj = pfBsDetailAdjust.isPresent() ? pfBsDetailAdjust.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<PfBsDetailAdjust> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfBsDetailAdjust> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LogNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = pfBsDetailAdjustReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfBsDetailAdjustReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfBsDetailAdjustReposHist.findAll(pageable);
    else 
      slice = pfBsDetailAdjustRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PfBsDetailAdjust findCustBormFirst(int custNo_0, int facmNo_1, int bormNo_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findCustBormFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2);
    Optional<PfBsDetailAdjust> pfBsDetailAdjustT = null;
    if (dbName.equals(ContentName.onDay))
      pfBsDetailAdjustT = pfBsDetailAdjustReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIs(custNo_0, facmNo_1, bormNo_2);
    else if (dbName.equals(ContentName.onMon))
      pfBsDetailAdjustT = pfBsDetailAdjustReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIs(custNo_0, facmNo_1, bormNo_2);
    else if (dbName.equals(ContentName.onHist))
      pfBsDetailAdjustT = pfBsDetailAdjustReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIs(custNo_0, facmNo_1, bormNo_2);
    else 
      pfBsDetailAdjustT = pfBsDetailAdjustRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIs(custNo_0, facmNo_1, bormNo_2);

    return pfBsDetailAdjustT.isPresent() ? pfBsDetailAdjustT.get() : null;
  }

  @Override
  public PfBsDetailAdjust holdById(Long logNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + logNo);
    Optional<PfBsDetailAdjust> pfBsDetailAdjust = null;
    if (dbName.equals(ContentName.onDay))
      pfBsDetailAdjust = pfBsDetailAdjustReposDay.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onMon))
      pfBsDetailAdjust = pfBsDetailAdjustReposMon.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onHist))
      pfBsDetailAdjust = pfBsDetailAdjustReposHist.findByLogNo(logNo);
    else 
      pfBsDetailAdjust = pfBsDetailAdjustRepos.findByLogNo(logNo);
    return pfBsDetailAdjust.isPresent() ? pfBsDetailAdjust.get() : null;
  }

  @Override
  public PfBsDetailAdjust holdById(PfBsDetailAdjust pfBsDetailAdjust, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + pfBsDetailAdjust.getLogNo());
    Optional<PfBsDetailAdjust> pfBsDetailAdjustT = null;
    if (dbName.equals(ContentName.onDay))
      pfBsDetailAdjustT = pfBsDetailAdjustReposDay.findByLogNo(pfBsDetailAdjust.getLogNo());
    else if (dbName.equals(ContentName.onMon))
      pfBsDetailAdjustT = pfBsDetailAdjustReposMon.findByLogNo(pfBsDetailAdjust.getLogNo());
    else if (dbName.equals(ContentName.onHist))
      pfBsDetailAdjustT = pfBsDetailAdjustReposHist.findByLogNo(pfBsDetailAdjust.getLogNo());
    else 
      pfBsDetailAdjustT = pfBsDetailAdjustRepos.findByLogNo(pfBsDetailAdjust.getLogNo());
    return pfBsDetailAdjustT.isPresent() ? pfBsDetailAdjustT.get() : null;
  }

  @Override
  public PfBsDetailAdjust insert(PfBsDetailAdjust pfBsDetailAdjust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + pfBsDetailAdjust.getLogNo());
    if (this.findById(pfBsDetailAdjust.getLogNo()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      pfBsDetailAdjust.setCreateEmpNo(empNot);

    if(pfBsDetailAdjust.getLastUpdateEmpNo() == null || pfBsDetailAdjust.getLastUpdateEmpNo().isEmpty())
      pfBsDetailAdjust.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfBsDetailAdjustReposDay.saveAndFlush(pfBsDetailAdjust);	
    else if (dbName.equals(ContentName.onMon))
      return pfBsDetailAdjustReposMon.saveAndFlush(pfBsDetailAdjust);
    else if (dbName.equals(ContentName.onHist))
      return pfBsDetailAdjustReposHist.saveAndFlush(pfBsDetailAdjust);
    else 
    return pfBsDetailAdjustRepos.saveAndFlush(pfBsDetailAdjust);
  }

  @Override
  public PfBsDetailAdjust update(PfBsDetailAdjust pfBsDetailAdjust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + pfBsDetailAdjust.getLogNo());
    if (!empNot.isEmpty())
      pfBsDetailAdjust.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfBsDetailAdjustReposDay.saveAndFlush(pfBsDetailAdjust);	
    else if (dbName.equals(ContentName.onMon))
      return pfBsDetailAdjustReposMon.saveAndFlush(pfBsDetailAdjust);
    else if (dbName.equals(ContentName.onHist))
      return pfBsDetailAdjustReposHist.saveAndFlush(pfBsDetailAdjust);
    else 
    return pfBsDetailAdjustRepos.saveAndFlush(pfBsDetailAdjust);
  }

  @Override
  public PfBsDetailAdjust update2(PfBsDetailAdjust pfBsDetailAdjust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + pfBsDetailAdjust.getLogNo());
    if (!empNot.isEmpty())
      pfBsDetailAdjust.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      pfBsDetailAdjustReposDay.saveAndFlush(pfBsDetailAdjust);	
    else if (dbName.equals(ContentName.onMon))
      pfBsDetailAdjustReposMon.saveAndFlush(pfBsDetailAdjust);
    else if (dbName.equals(ContentName.onHist))
        pfBsDetailAdjustReposHist.saveAndFlush(pfBsDetailAdjust);
    else 
      pfBsDetailAdjustRepos.saveAndFlush(pfBsDetailAdjust);	
    return this.findById(pfBsDetailAdjust.getLogNo());
  }

  @Override
  public void delete(PfBsDetailAdjust pfBsDetailAdjust, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + pfBsDetailAdjust.getLogNo());
    if (dbName.equals(ContentName.onDay)) {
      pfBsDetailAdjustReposDay.delete(pfBsDetailAdjust);	
      pfBsDetailAdjustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfBsDetailAdjustReposMon.delete(pfBsDetailAdjust);	
      pfBsDetailAdjustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfBsDetailAdjustReposHist.delete(pfBsDetailAdjust);
      pfBsDetailAdjustReposHist.flush();
    }
    else {
      pfBsDetailAdjustRepos.delete(pfBsDetailAdjust);
      pfBsDetailAdjustRepos.flush();
    }
   }

  @Override
  public void insertAll(List<PfBsDetailAdjust> pfBsDetailAdjust, TitaVo... titaVo) throws DBException {
    if (pfBsDetailAdjust == null || pfBsDetailAdjust.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (PfBsDetailAdjust t : pfBsDetailAdjust){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      pfBsDetailAdjust = pfBsDetailAdjustReposDay.saveAll(pfBsDetailAdjust);	
      pfBsDetailAdjustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfBsDetailAdjust = pfBsDetailAdjustReposMon.saveAll(pfBsDetailAdjust);	
      pfBsDetailAdjustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfBsDetailAdjust = pfBsDetailAdjustReposHist.saveAll(pfBsDetailAdjust);
      pfBsDetailAdjustReposHist.flush();
    }
    else {
      pfBsDetailAdjust = pfBsDetailAdjustRepos.saveAll(pfBsDetailAdjust);
      pfBsDetailAdjustRepos.flush();
    }
    }

  @Override
  public void updateAll(List<PfBsDetailAdjust> pfBsDetailAdjust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (pfBsDetailAdjust == null || pfBsDetailAdjust.size() == 0)
      throw new DBException(6);

    for (PfBsDetailAdjust t : pfBsDetailAdjust) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      pfBsDetailAdjust = pfBsDetailAdjustReposDay.saveAll(pfBsDetailAdjust);	
      pfBsDetailAdjustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfBsDetailAdjust = pfBsDetailAdjustReposMon.saveAll(pfBsDetailAdjust);	
      pfBsDetailAdjustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfBsDetailAdjust = pfBsDetailAdjustReposHist.saveAll(pfBsDetailAdjust);
      pfBsDetailAdjustReposHist.flush();
    }
    else {
      pfBsDetailAdjust = pfBsDetailAdjustRepos.saveAll(pfBsDetailAdjust);
      pfBsDetailAdjustRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<PfBsDetailAdjust> pfBsDetailAdjust, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (pfBsDetailAdjust == null || pfBsDetailAdjust.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      pfBsDetailAdjustReposDay.deleteAll(pfBsDetailAdjust);	
      pfBsDetailAdjustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfBsDetailAdjustReposMon.deleteAll(pfBsDetailAdjust);	
      pfBsDetailAdjustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfBsDetailAdjustReposHist.deleteAll(pfBsDetailAdjust);
      pfBsDetailAdjustReposHist.flush();
    }
    else {
      pfBsDetailAdjustRepos.deleteAll(pfBsDetailAdjust);
      pfBsDetailAdjustRepos.flush();
    }
  }

}
