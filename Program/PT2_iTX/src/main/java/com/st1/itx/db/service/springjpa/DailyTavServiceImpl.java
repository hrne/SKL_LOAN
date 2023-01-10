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
import com.st1.itx.db.domain.DailyTav;
import com.st1.itx.db.domain.DailyTavId;
import com.st1.itx.db.repository.online.DailyTavRepository;
import com.st1.itx.db.repository.day.DailyTavRepositoryDay;
import com.st1.itx.db.repository.mon.DailyTavRepositoryMon;
import com.st1.itx.db.repository.hist.DailyTavRepositoryHist;
import com.st1.itx.db.service.DailyTavService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("dailyTavService")
@Repository
public class DailyTavServiceImpl extends ASpringJpaParm implements DailyTavService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private DailyTavRepository dailyTavRepos;

  @Autowired
  private DailyTavRepositoryDay dailyTavReposDay;

  @Autowired
  private DailyTavRepositoryMon dailyTavReposMon;

  @Autowired
  private DailyTavRepositoryHist dailyTavReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(dailyTavRepos);
    org.junit.Assert.assertNotNull(dailyTavReposDay);
    org.junit.Assert.assertNotNull(dailyTavReposMon);
    org.junit.Assert.assertNotNull(dailyTavReposHist);
  }

  @Override
  public DailyTav findById(DailyTavId dailyTavId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + dailyTavId);
    Optional<DailyTav> dailyTav = null;
    if (dbName.equals(ContentName.onDay))
      dailyTav = dailyTavReposDay.findById(dailyTavId);
    else if (dbName.equals(ContentName.onMon))
      dailyTav = dailyTavReposMon.findById(dailyTavId);
    else if (dbName.equals(ContentName.onHist))
      dailyTav = dailyTavReposHist.findById(dailyTavId);
    else 
      dailyTav = dailyTavRepos.findById(dailyTavId);
    DailyTav obj = dailyTav.isPresent() ? dailyTav.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<DailyTav> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<DailyTav> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AcDate", "CustNo", "FacmNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcDate", "CustNo", "FacmNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = dailyTavReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = dailyTavReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = dailyTavReposHist.findAll(pageable);
    else 
      slice = dailyTavRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<DailyTav> CustNoAcDateRange(int custNo_0, int acDate_1, int acDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<DailyTav> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustNoAcDateRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " acDate_1 : " +  acDate_1 + " acDate_2 : " +  acDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = dailyTavReposDay.findAllByCustNoIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(custNo_0, acDate_1, acDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = dailyTavReposMon.findAllByCustNoIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(custNo_0, acDate_1, acDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = dailyTavReposHist.findAllByCustNoIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(custNo_0, acDate_1, acDate_2, pageable);
    else 
      slice = dailyTavRepos.findAllByCustNoIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(custNo_0, acDate_1, acDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public DailyTav holdById(DailyTavId dailyTavId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + dailyTavId);
    Optional<DailyTav> dailyTav = null;
    if (dbName.equals(ContentName.onDay))
      dailyTav = dailyTavReposDay.findByDailyTavId(dailyTavId);
    else if (dbName.equals(ContentName.onMon))
      dailyTav = dailyTavReposMon.findByDailyTavId(dailyTavId);
    else if (dbName.equals(ContentName.onHist))
      dailyTav = dailyTavReposHist.findByDailyTavId(dailyTavId);
    else 
      dailyTav = dailyTavRepos.findByDailyTavId(dailyTavId);
    return dailyTav.isPresent() ? dailyTav.get() : null;
  }

  @Override
  public DailyTav holdById(DailyTav dailyTav, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + dailyTav.getDailyTavId());
    Optional<DailyTav> dailyTavT = null;
    if (dbName.equals(ContentName.onDay))
      dailyTavT = dailyTavReposDay.findByDailyTavId(dailyTav.getDailyTavId());
    else if (dbName.equals(ContentName.onMon))
      dailyTavT = dailyTavReposMon.findByDailyTavId(dailyTav.getDailyTavId());
    else if (dbName.equals(ContentName.onHist))
      dailyTavT = dailyTavReposHist.findByDailyTavId(dailyTav.getDailyTavId());
    else 
      dailyTavT = dailyTavRepos.findByDailyTavId(dailyTav.getDailyTavId());
    return dailyTavT.isPresent() ? dailyTavT.get() : null;
  }

  @Override
  public DailyTav insert(DailyTav dailyTav, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + dailyTav.getDailyTavId());
    if (this.findById(dailyTav.getDailyTavId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      dailyTav.setCreateEmpNo(empNot);

    if(dailyTav.getLastUpdateEmpNo() == null || dailyTav.getLastUpdateEmpNo().isEmpty())
      dailyTav.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return dailyTavReposDay.saveAndFlush(dailyTav);	
    else if (dbName.equals(ContentName.onMon))
      return dailyTavReposMon.saveAndFlush(dailyTav);
    else if (dbName.equals(ContentName.onHist))
      return dailyTavReposHist.saveAndFlush(dailyTav);
    else 
    return dailyTavRepos.saveAndFlush(dailyTav);
  }

  @Override
  public DailyTav update(DailyTav dailyTav, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + dailyTav.getDailyTavId());
    if (!empNot.isEmpty())
      dailyTav.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return dailyTavReposDay.saveAndFlush(dailyTav);	
    else if (dbName.equals(ContentName.onMon))
      return dailyTavReposMon.saveAndFlush(dailyTav);
    else if (dbName.equals(ContentName.onHist))
      return dailyTavReposHist.saveAndFlush(dailyTav);
    else 
    return dailyTavRepos.saveAndFlush(dailyTav);
  }

  @Override
  public DailyTav update2(DailyTav dailyTav, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + dailyTav.getDailyTavId());
    if (!empNot.isEmpty())
      dailyTav.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      dailyTavReposDay.saveAndFlush(dailyTav);	
    else if (dbName.equals(ContentName.onMon))
      dailyTavReposMon.saveAndFlush(dailyTav);
    else if (dbName.equals(ContentName.onHist))
        dailyTavReposHist.saveAndFlush(dailyTav);
    else 
      dailyTavRepos.saveAndFlush(dailyTav);	
    return this.findById(dailyTav.getDailyTavId());
  }

  @Override
  public void delete(DailyTav dailyTav, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + dailyTav.getDailyTavId());
    if (dbName.equals(ContentName.onDay)) {
      dailyTavReposDay.delete(dailyTav);	
      dailyTavReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      dailyTavReposMon.delete(dailyTav);	
      dailyTavReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      dailyTavReposHist.delete(dailyTav);
      dailyTavReposHist.flush();
    }
    else {
      dailyTavRepos.delete(dailyTav);
      dailyTavRepos.flush();
    }
   }

  @Override
  public void insertAll(List<DailyTav> dailyTav, TitaVo... titaVo) throws DBException {
    if (dailyTav == null || dailyTav.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (DailyTav t : dailyTav){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      dailyTav = dailyTavReposDay.saveAll(dailyTav);	
      dailyTavReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      dailyTav = dailyTavReposMon.saveAll(dailyTav);	
      dailyTavReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      dailyTav = dailyTavReposHist.saveAll(dailyTav);
      dailyTavReposHist.flush();
    }
    else {
      dailyTav = dailyTavRepos.saveAll(dailyTav);
      dailyTavRepos.flush();
    }
    }

  @Override
  public void updateAll(List<DailyTav> dailyTav, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (dailyTav == null || dailyTav.size() == 0)
      throw new DBException(6);

    for (DailyTav t : dailyTav) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      dailyTav = dailyTavReposDay.saveAll(dailyTav);	
      dailyTavReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      dailyTav = dailyTavReposMon.saveAll(dailyTav);	
      dailyTavReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      dailyTav = dailyTavReposHist.saveAll(dailyTav);
      dailyTavReposHist.flush();
    }
    else {
      dailyTav = dailyTavRepos.saveAll(dailyTav);
      dailyTavRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<DailyTav> dailyTav, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dailyTav == null || dailyTav.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      dailyTavReposDay.deleteAll(dailyTav);	
      dailyTavReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      dailyTavReposMon.deleteAll(dailyTav);	
      dailyTavReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      dailyTavReposHist.deleteAll(dailyTav);
      dailyTavReposHist.flush();
    }
    else {
      dailyTavRepos.deleteAll(dailyTav);
      dailyTavRepos.flush();
    }
  }

}
