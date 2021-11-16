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
import com.st1.itx.db.domain.PfIntranetAdjust;
import com.st1.itx.db.repository.online.PfIntranetAdjustRepository;
import com.st1.itx.db.repository.day.PfIntranetAdjustRepositoryDay;
import com.st1.itx.db.repository.mon.PfIntranetAdjustRepositoryMon;
import com.st1.itx.db.repository.hist.PfIntranetAdjustRepositoryHist;
import com.st1.itx.db.service.PfIntranetAdjustService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("pfIntranetAdjustService")
@Repository
public class PfIntranetAdjustServiceImpl extends ASpringJpaParm implements PfIntranetAdjustService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private PfIntranetAdjustRepository pfIntranetAdjustRepos;

  @Autowired
  private PfIntranetAdjustRepositoryDay pfIntranetAdjustReposDay;

  @Autowired
  private PfIntranetAdjustRepositoryMon pfIntranetAdjustReposMon;

  @Autowired
  private PfIntranetAdjustRepositoryHist pfIntranetAdjustReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(pfIntranetAdjustRepos);
    org.junit.Assert.assertNotNull(pfIntranetAdjustReposDay);
    org.junit.Assert.assertNotNull(pfIntranetAdjustReposMon);
    org.junit.Assert.assertNotNull(pfIntranetAdjustReposHist);
  }

  @Override
  public PfIntranetAdjust findById(Long logNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + logNo);
    Optional<PfIntranetAdjust> pfIntranetAdjust = null;
    if (dbName.equals(ContentName.onDay))
      pfIntranetAdjust = pfIntranetAdjustReposDay.findById(logNo);
    else if (dbName.equals(ContentName.onMon))
      pfIntranetAdjust = pfIntranetAdjustReposMon.findById(logNo);
    else if (dbName.equals(ContentName.onHist))
      pfIntranetAdjust = pfIntranetAdjustReposHist.findById(logNo);
    else 
      pfIntranetAdjust = pfIntranetAdjustRepos.findById(logNo);
    PfIntranetAdjust obj = pfIntranetAdjust.isPresent() ? pfIntranetAdjust.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<PfIntranetAdjust> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfIntranetAdjust> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LogNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = pfIntranetAdjustReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfIntranetAdjustReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfIntranetAdjustReposHist.findAll(pageable);
    else 
      slice = pfIntranetAdjustRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PfIntranetAdjust findByBormFirst(int custNo_0, int facmNo_1, int bormNo_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findByBormFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2);
    Optional<PfIntranetAdjust> pfIntranetAdjustT = null;
    if (dbName.equals(ContentName.onDay))
      pfIntranetAdjustT = pfIntranetAdjustReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, bormNo_2);
    else if (dbName.equals(ContentName.onMon))
      pfIntranetAdjustT = pfIntranetAdjustReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, bormNo_2);
    else if (dbName.equals(ContentName.onHist))
      pfIntranetAdjustT = pfIntranetAdjustReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, bormNo_2);
    else 
      pfIntranetAdjustT = pfIntranetAdjustRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, bormNo_2);

    return pfIntranetAdjustT.isPresent() ? pfIntranetAdjustT.get() : null;
  }

  @Override
  public Slice<PfIntranetAdjust> findByWorkMonth(int workMonth_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfIntranetAdjust> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByWorkMonth " + dbName + " : " + "workMonth_0 : " + workMonth_0);
    if (dbName.equals(ContentName.onDay))
      slice = pfIntranetAdjustReposDay.findAllByWorkMonthIsOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfIntranetAdjustReposMon.findAllByWorkMonthIsOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfIntranetAdjustReposHist.findAllByWorkMonthIsOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, pageable);
    else 
      slice = pfIntranetAdjustRepos.findAllByWorkMonthIsOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PfIntranetAdjust holdById(Long logNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + logNo);
    Optional<PfIntranetAdjust> pfIntranetAdjust = null;
    if (dbName.equals(ContentName.onDay))
      pfIntranetAdjust = pfIntranetAdjustReposDay.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onMon))
      pfIntranetAdjust = pfIntranetAdjustReposMon.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onHist))
      pfIntranetAdjust = pfIntranetAdjustReposHist.findByLogNo(logNo);
    else 
      pfIntranetAdjust = pfIntranetAdjustRepos.findByLogNo(logNo);
    return pfIntranetAdjust.isPresent() ? pfIntranetAdjust.get() : null;
  }

  @Override
  public PfIntranetAdjust holdById(PfIntranetAdjust pfIntranetAdjust, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + pfIntranetAdjust.getLogNo());
    Optional<PfIntranetAdjust> pfIntranetAdjustT = null;
    if (dbName.equals(ContentName.onDay))
      pfIntranetAdjustT = pfIntranetAdjustReposDay.findByLogNo(pfIntranetAdjust.getLogNo());
    else if (dbName.equals(ContentName.onMon))
      pfIntranetAdjustT = pfIntranetAdjustReposMon.findByLogNo(pfIntranetAdjust.getLogNo());
    else if (dbName.equals(ContentName.onHist))
      pfIntranetAdjustT = pfIntranetAdjustReposHist.findByLogNo(pfIntranetAdjust.getLogNo());
    else 
      pfIntranetAdjustT = pfIntranetAdjustRepos.findByLogNo(pfIntranetAdjust.getLogNo());
    return pfIntranetAdjustT.isPresent() ? pfIntranetAdjustT.get() : null;
  }

  @Override
  public PfIntranetAdjust insert(PfIntranetAdjust pfIntranetAdjust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + pfIntranetAdjust.getLogNo());
    if (this.findById(pfIntranetAdjust.getLogNo()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      pfIntranetAdjust.setCreateEmpNo(empNot);

    if(pfIntranetAdjust.getLastUpdateEmpNo() == null || pfIntranetAdjust.getLastUpdateEmpNo().isEmpty())
      pfIntranetAdjust.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfIntranetAdjustReposDay.saveAndFlush(pfIntranetAdjust);	
    else if (dbName.equals(ContentName.onMon))
      return pfIntranetAdjustReposMon.saveAndFlush(pfIntranetAdjust);
    else if (dbName.equals(ContentName.onHist))
      return pfIntranetAdjustReposHist.saveAndFlush(pfIntranetAdjust);
    else 
    return pfIntranetAdjustRepos.saveAndFlush(pfIntranetAdjust);
  }

  @Override
  public PfIntranetAdjust update(PfIntranetAdjust pfIntranetAdjust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + pfIntranetAdjust.getLogNo());
    if (!empNot.isEmpty())
      pfIntranetAdjust.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfIntranetAdjustReposDay.saveAndFlush(pfIntranetAdjust);	
    else if (dbName.equals(ContentName.onMon))
      return pfIntranetAdjustReposMon.saveAndFlush(pfIntranetAdjust);
    else if (dbName.equals(ContentName.onHist))
      return pfIntranetAdjustReposHist.saveAndFlush(pfIntranetAdjust);
    else 
    return pfIntranetAdjustRepos.saveAndFlush(pfIntranetAdjust);
  }

  @Override
  public PfIntranetAdjust update2(PfIntranetAdjust pfIntranetAdjust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + pfIntranetAdjust.getLogNo());
    if (!empNot.isEmpty())
      pfIntranetAdjust.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      pfIntranetAdjustReposDay.saveAndFlush(pfIntranetAdjust);	
    else if (dbName.equals(ContentName.onMon))
      pfIntranetAdjustReposMon.saveAndFlush(pfIntranetAdjust);
    else if (dbName.equals(ContentName.onHist))
        pfIntranetAdjustReposHist.saveAndFlush(pfIntranetAdjust);
    else 
      pfIntranetAdjustRepos.saveAndFlush(pfIntranetAdjust);	
    return this.findById(pfIntranetAdjust.getLogNo());
  }

  @Override
  public void delete(PfIntranetAdjust pfIntranetAdjust, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + pfIntranetAdjust.getLogNo());
    if (dbName.equals(ContentName.onDay)) {
      pfIntranetAdjustReposDay.delete(pfIntranetAdjust);	
      pfIntranetAdjustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfIntranetAdjustReposMon.delete(pfIntranetAdjust);	
      pfIntranetAdjustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfIntranetAdjustReposHist.delete(pfIntranetAdjust);
      pfIntranetAdjustReposHist.flush();
    }
    else {
      pfIntranetAdjustRepos.delete(pfIntranetAdjust);
      pfIntranetAdjustRepos.flush();
    }
   }

  @Override
  public void insertAll(List<PfIntranetAdjust> pfIntranetAdjust, TitaVo... titaVo) throws DBException {
    if (pfIntranetAdjust == null || pfIntranetAdjust.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (PfIntranetAdjust t : pfIntranetAdjust){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      pfIntranetAdjust = pfIntranetAdjustReposDay.saveAll(pfIntranetAdjust);	
      pfIntranetAdjustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfIntranetAdjust = pfIntranetAdjustReposMon.saveAll(pfIntranetAdjust);	
      pfIntranetAdjustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfIntranetAdjust = pfIntranetAdjustReposHist.saveAll(pfIntranetAdjust);
      pfIntranetAdjustReposHist.flush();
    }
    else {
      pfIntranetAdjust = pfIntranetAdjustRepos.saveAll(pfIntranetAdjust);
      pfIntranetAdjustRepos.flush();
    }
    }

  @Override
  public void updateAll(List<PfIntranetAdjust> pfIntranetAdjust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (pfIntranetAdjust == null || pfIntranetAdjust.size() == 0)
      throw new DBException(6);

    for (PfIntranetAdjust t : pfIntranetAdjust) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      pfIntranetAdjust = pfIntranetAdjustReposDay.saveAll(pfIntranetAdjust);	
      pfIntranetAdjustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfIntranetAdjust = pfIntranetAdjustReposMon.saveAll(pfIntranetAdjust);	
      pfIntranetAdjustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfIntranetAdjust = pfIntranetAdjustReposHist.saveAll(pfIntranetAdjust);
      pfIntranetAdjustReposHist.flush();
    }
    else {
      pfIntranetAdjust = pfIntranetAdjustRepos.saveAll(pfIntranetAdjust);
      pfIntranetAdjustRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<PfIntranetAdjust> pfIntranetAdjust, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (pfIntranetAdjust == null || pfIntranetAdjust.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      pfIntranetAdjustReposDay.deleteAll(pfIntranetAdjust);	
      pfIntranetAdjustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfIntranetAdjustReposMon.deleteAll(pfIntranetAdjust);	
      pfIntranetAdjustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfIntranetAdjustReposHist.deleteAll(pfIntranetAdjust);
      pfIntranetAdjustReposHist.flush();
    }
    else {
      pfIntranetAdjustRepos.deleteAll(pfIntranetAdjust);
      pfIntranetAdjustRepos.flush();
    }
  }

}
