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
import com.st1.itx.db.domain.PfItDetailAdjust;
import com.st1.itx.db.repository.online.PfItDetailAdjustRepository;
import com.st1.itx.db.repository.day.PfItDetailAdjustRepositoryDay;
import com.st1.itx.db.repository.mon.PfItDetailAdjustRepositoryMon;
import com.st1.itx.db.repository.hist.PfItDetailAdjustRepositoryHist;
import com.st1.itx.db.service.PfItDetailAdjustService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("pfItDetailAdjustService")
@Repository
public class PfItDetailAdjustServiceImpl extends ASpringJpaParm implements PfItDetailAdjustService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private PfItDetailAdjustRepository pfItDetailAdjustRepos;

  @Autowired
  private PfItDetailAdjustRepositoryDay pfItDetailAdjustReposDay;

  @Autowired
  private PfItDetailAdjustRepositoryMon pfItDetailAdjustReposMon;

  @Autowired
  private PfItDetailAdjustRepositoryHist pfItDetailAdjustReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(pfItDetailAdjustRepos);
    org.junit.Assert.assertNotNull(pfItDetailAdjustReposDay);
    org.junit.Assert.assertNotNull(pfItDetailAdjustReposMon);
    org.junit.Assert.assertNotNull(pfItDetailAdjustReposHist);
  }

  @Override
  public PfItDetailAdjust findById(Long logNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + logNo);
    Optional<PfItDetailAdjust> pfItDetailAdjust = null;
    if (dbName.equals(ContentName.onDay))
      pfItDetailAdjust = pfItDetailAdjustReposDay.findById(logNo);
    else if (dbName.equals(ContentName.onMon))
      pfItDetailAdjust = pfItDetailAdjustReposMon.findById(logNo);
    else if (dbName.equals(ContentName.onHist))
      pfItDetailAdjust = pfItDetailAdjustReposHist.findById(logNo);
    else 
      pfItDetailAdjust = pfItDetailAdjustRepos.findById(logNo);
    PfItDetailAdjust obj = pfItDetailAdjust.isPresent() ? pfItDetailAdjust.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<PfItDetailAdjust> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfItDetailAdjust> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LogNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = pfItDetailAdjustReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfItDetailAdjustReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfItDetailAdjustReposHist.findAll(pageable);
    else 
      slice = pfItDetailAdjustRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PfItDetailAdjust findCustFacmBormFirst(int custNo_0, int facmNo_1, int bormNo_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findCustFacmBormFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2);
    Optional<PfItDetailAdjust> pfItDetailAdjustT = null;
    if (dbName.equals(ContentName.onDay))
      pfItDetailAdjustT = pfItDetailAdjustReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByWorkMonthAsc(custNo_0, facmNo_1, bormNo_2);
    else if (dbName.equals(ContentName.onMon))
      pfItDetailAdjustT = pfItDetailAdjustReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByWorkMonthAsc(custNo_0, facmNo_1, bormNo_2);
    else if (dbName.equals(ContentName.onHist))
      pfItDetailAdjustT = pfItDetailAdjustReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByWorkMonthAsc(custNo_0, facmNo_1, bormNo_2);
    else 
      pfItDetailAdjustT = pfItDetailAdjustRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByWorkMonthAsc(custNo_0, facmNo_1, bormNo_2);

    return pfItDetailAdjustT.isPresent() ? pfItDetailAdjustT.get() : null;
  }

  @Override
  public PfItDetailAdjust findWorkMonthFirst(int custNo_0, int facmNo_1, int bormNo_2, int workMonth_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findWorkMonthFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2 + " workMonth_3 : " +  workMonth_3);
    Optional<PfItDetailAdjust> pfItDetailAdjustT = null;
    if (dbName.equals(ContentName.onDay))
      pfItDetailAdjustT = pfItDetailAdjustReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndWorkMonthIs(custNo_0, facmNo_1, bormNo_2, workMonth_3);
    else if (dbName.equals(ContentName.onMon))
      pfItDetailAdjustT = pfItDetailAdjustReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndWorkMonthIs(custNo_0, facmNo_1, bormNo_2, workMonth_3);
    else if (dbName.equals(ContentName.onHist))
      pfItDetailAdjustT = pfItDetailAdjustReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndWorkMonthIs(custNo_0, facmNo_1, bormNo_2, workMonth_3);
    else 
      pfItDetailAdjustT = pfItDetailAdjustRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndWorkMonthIs(custNo_0, facmNo_1, bormNo_2, workMonth_3);

    return pfItDetailAdjustT.isPresent() ? pfItDetailAdjustT.get() : null;
  }

  @Override
  public PfItDetailAdjust holdById(Long logNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + logNo);
    Optional<PfItDetailAdjust> pfItDetailAdjust = null;
    if (dbName.equals(ContentName.onDay))
      pfItDetailAdjust = pfItDetailAdjustReposDay.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onMon))
      pfItDetailAdjust = pfItDetailAdjustReposMon.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onHist))
      pfItDetailAdjust = pfItDetailAdjustReposHist.findByLogNo(logNo);
    else 
      pfItDetailAdjust = pfItDetailAdjustRepos.findByLogNo(logNo);
    return pfItDetailAdjust.isPresent() ? pfItDetailAdjust.get() : null;
  }

  @Override
  public PfItDetailAdjust holdById(PfItDetailAdjust pfItDetailAdjust, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + pfItDetailAdjust.getLogNo());
    Optional<PfItDetailAdjust> pfItDetailAdjustT = null;
    if (dbName.equals(ContentName.onDay))
      pfItDetailAdjustT = pfItDetailAdjustReposDay.findByLogNo(pfItDetailAdjust.getLogNo());
    else if (dbName.equals(ContentName.onMon))
      pfItDetailAdjustT = pfItDetailAdjustReposMon.findByLogNo(pfItDetailAdjust.getLogNo());
    else if (dbName.equals(ContentName.onHist))
      pfItDetailAdjustT = pfItDetailAdjustReposHist.findByLogNo(pfItDetailAdjust.getLogNo());
    else 
      pfItDetailAdjustT = pfItDetailAdjustRepos.findByLogNo(pfItDetailAdjust.getLogNo());
    return pfItDetailAdjustT.isPresent() ? pfItDetailAdjustT.get() : null;
  }

  @Override
  public PfItDetailAdjust insert(PfItDetailAdjust pfItDetailAdjust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + pfItDetailAdjust.getLogNo());
    if (this.findById(pfItDetailAdjust.getLogNo(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      pfItDetailAdjust.setCreateEmpNo(empNot);

    if(pfItDetailAdjust.getLastUpdateEmpNo() == null || pfItDetailAdjust.getLastUpdateEmpNo().isEmpty())
      pfItDetailAdjust.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfItDetailAdjustReposDay.saveAndFlush(pfItDetailAdjust);	
    else if (dbName.equals(ContentName.onMon))
      return pfItDetailAdjustReposMon.saveAndFlush(pfItDetailAdjust);
    else if (dbName.equals(ContentName.onHist))
      return pfItDetailAdjustReposHist.saveAndFlush(pfItDetailAdjust);
    else 
    return pfItDetailAdjustRepos.saveAndFlush(pfItDetailAdjust);
  }

  @Override
  public PfItDetailAdjust update(PfItDetailAdjust pfItDetailAdjust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + pfItDetailAdjust.getLogNo());
    if (!empNot.isEmpty())
      pfItDetailAdjust.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfItDetailAdjustReposDay.saveAndFlush(pfItDetailAdjust);	
    else if (dbName.equals(ContentName.onMon))
      return pfItDetailAdjustReposMon.saveAndFlush(pfItDetailAdjust);
    else if (dbName.equals(ContentName.onHist))
      return pfItDetailAdjustReposHist.saveAndFlush(pfItDetailAdjust);
    else 
    return pfItDetailAdjustRepos.saveAndFlush(pfItDetailAdjust);
  }

  @Override
  public PfItDetailAdjust update2(PfItDetailAdjust pfItDetailAdjust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + pfItDetailAdjust.getLogNo());
    if (!empNot.isEmpty())
      pfItDetailAdjust.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      pfItDetailAdjustReposDay.saveAndFlush(pfItDetailAdjust);	
    else if (dbName.equals(ContentName.onMon))
      pfItDetailAdjustReposMon.saveAndFlush(pfItDetailAdjust);
    else if (dbName.equals(ContentName.onHist))
        pfItDetailAdjustReposHist.saveAndFlush(pfItDetailAdjust);
    else 
      pfItDetailAdjustRepos.saveAndFlush(pfItDetailAdjust);	
    return this.findById(pfItDetailAdjust.getLogNo());
  }

  @Override
  public void delete(PfItDetailAdjust pfItDetailAdjust, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + pfItDetailAdjust.getLogNo());
    if (dbName.equals(ContentName.onDay)) {
      pfItDetailAdjustReposDay.delete(pfItDetailAdjust);	
      pfItDetailAdjustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfItDetailAdjustReposMon.delete(pfItDetailAdjust);	
      pfItDetailAdjustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfItDetailAdjustReposHist.delete(pfItDetailAdjust);
      pfItDetailAdjustReposHist.flush();
    }
    else {
      pfItDetailAdjustRepos.delete(pfItDetailAdjust);
      pfItDetailAdjustRepos.flush();
    }
   }

  @Override
  public void insertAll(List<PfItDetailAdjust> pfItDetailAdjust, TitaVo... titaVo) throws DBException {
    if (pfItDetailAdjust == null || pfItDetailAdjust.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (PfItDetailAdjust t : pfItDetailAdjust){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      pfItDetailAdjust = pfItDetailAdjustReposDay.saveAll(pfItDetailAdjust);	
      pfItDetailAdjustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfItDetailAdjust = pfItDetailAdjustReposMon.saveAll(pfItDetailAdjust);	
      pfItDetailAdjustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfItDetailAdjust = pfItDetailAdjustReposHist.saveAll(pfItDetailAdjust);
      pfItDetailAdjustReposHist.flush();
    }
    else {
      pfItDetailAdjust = pfItDetailAdjustRepos.saveAll(pfItDetailAdjust);
      pfItDetailAdjustRepos.flush();
    }
    }

  @Override
  public void updateAll(List<PfItDetailAdjust> pfItDetailAdjust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (pfItDetailAdjust == null || pfItDetailAdjust.size() == 0)
      throw new DBException(6);

    for (PfItDetailAdjust t : pfItDetailAdjust) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      pfItDetailAdjust = pfItDetailAdjustReposDay.saveAll(pfItDetailAdjust);	
      pfItDetailAdjustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfItDetailAdjust = pfItDetailAdjustReposMon.saveAll(pfItDetailAdjust);	
      pfItDetailAdjustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfItDetailAdjust = pfItDetailAdjustReposHist.saveAll(pfItDetailAdjust);
      pfItDetailAdjustReposHist.flush();
    }
    else {
      pfItDetailAdjust = pfItDetailAdjustRepos.saveAll(pfItDetailAdjust);
      pfItDetailAdjustRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<PfItDetailAdjust> pfItDetailAdjust, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (pfItDetailAdjust == null || pfItDetailAdjust.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      pfItDetailAdjustReposDay.deleteAll(pfItDetailAdjust);	
      pfItDetailAdjustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfItDetailAdjustReposMon.deleteAll(pfItDetailAdjust);	
      pfItDetailAdjustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfItDetailAdjustReposHist.deleteAll(pfItDetailAdjust);
      pfItDetailAdjustReposHist.flush();
    }
    else {
      pfItDetailAdjustRepos.deleteAll(pfItDetailAdjust);
      pfItDetailAdjustRepos.flush();
    }
  }

}
