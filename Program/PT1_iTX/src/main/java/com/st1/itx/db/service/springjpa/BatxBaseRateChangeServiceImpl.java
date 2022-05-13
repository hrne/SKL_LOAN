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
import com.st1.itx.db.domain.BatxBaseRateChange;
import com.st1.itx.db.domain.BatxBaseRateChangeId;
import com.st1.itx.db.repository.online.BatxBaseRateChangeRepository;
import com.st1.itx.db.repository.day.BatxBaseRateChangeRepositoryDay;
import com.st1.itx.db.repository.mon.BatxBaseRateChangeRepositoryMon;
import com.st1.itx.db.repository.hist.BatxBaseRateChangeRepositoryHist;
import com.st1.itx.db.service.BatxBaseRateChangeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("batxBaseRateChangeService")
@Repository
public class BatxBaseRateChangeServiceImpl extends ASpringJpaParm implements BatxBaseRateChangeService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private BatxBaseRateChangeRepository batxBaseRateChangeRepos;

  @Autowired
  private BatxBaseRateChangeRepositoryDay batxBaseRateChangeReposDay;

  @Autowired
  private BatxBaseRateChangeRepositoryMon batxBaseRateChangeReposMon;

  @Autowired
  private BatxBaseRateChangeRepositoryHist batxBaseRateChangeReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(batxBaseRateChangeRepos);
    org.junit.Assert.assertNotNull(batxBaseRateChangeReposDay);
    org.junit.Assert.assertNotNull(batxBaseRateChangeReposMon);
    org.junit.Assert.assertNotNull(batxBaseRateChangeReposHist);
  }

  @Override
  public BatxBaseRateChange findById(BatxBaseRateChangeId batxBaseRateChangeId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + batxBaseRateChangeId);
    Optional<BatxBaseRateChange> batxBaseRateChange = null;
    if (dbName.equals(ContentName.onDay))
      batxBaseRateChange = batxBaseRateChangeReposDay.findById(batxBaseRateChangeId);
    else if (dbName.equals(ContentName.onMon))
      batxBaseRateChange = batxBaseRateChangeReposMon.findById(batxBaseRateChangeId);
    else if (dbName.equals(ContentName.onHist))
      batxBaseRateChange = batxBaseRateChangeReposHist.findById(batxBaseRateChangeId);
    else 
      batxBaseRateChange = batxBaseRateChangeRepos.findById(batxBaseRateChangeId);
    BatxBaseRateChange obj = batxBaseRateChange.isPresent() ? batxBaseRateChange.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<BatxBaseRateChange> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxBaseRateChange> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AdjDate", "CustNo", "FacmNo", "BormNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AdjDate", "CustNo", "FacmNo", "BormNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = batxBaseRateChangeReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxBaseRateChangeReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxBaseRateChangeReposHist.findAll(pageable);
    else 
      slice = batxBaseRateChangeRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public BatxBaseRateChange holdById(BatxBaseRateChangeId batxBaseRateChangeId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + batxBaseRateChangeId);
    Optional<BatxBaseRateChange> batxBaseRateChange = null;
    if (dbName.equals(ContentName.onDay))
      batxBaseRateChange = batxBaseRateChangeReposDay.findByBatxBaseRateChangeId(batxBaseRateChangeId);
    else if (dbName.equals(ContentName.onMon))
      batxBaseRateChange = batxBaseRateChangeReposMon.findByBatxBaseRateChangeId(batxBaseRateChangeId);
    else if (dbName.equals(ContentName.onHist))
      batxBaseRateChange = batxBaseRateChangeReposHist.findByBatxBaseRateChangeId(batxBaseRateChangeId);
    else 
      batxBaseRateChange = batxBaseRateChangeRepos.findByBatxBaseRateChangeId(batxBaseRateChangeId);
    return batxBaseRateChange.isPresent() ? batxBaseRateChange.get() : null;
  }

  @Override
  public BatxBaseRateChange holdById(BatxBaseRateChange batxBaseRateChange, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + batxBaseRateChange.getBatxBaseRateChangeId());
    Optional<BatxBaseRateChange> batxBaseRateChangeT = null;
    if (dbName.equals(ContentName.onDay))
      batxBaseRateChangeT = batxBaseRateChangeReposDay.findByBatxBaseRateChangeId(batxBaseRateChange.getBatxBaseRateChangeId());
    else if (dbName.equals(ContentName.onMon))
      batxBaseRateChangeT = batxBaseRateChangeReposMon.findByBatxBaseRateChangeId(batxBaseRateChange.getBatxBaseRateChangeId());
    else if (dbName.equals(ContentName.onHist))
      batxBaseRateChangeT = batxBaseRateChangeReposHist.findByBatxBaseRateChangeId(batxBaseRateChange.getBatxBaseRateChangeId());
    else 
      batxBaseRateChangeT = batxBaseRateChangeRepos.findByBatxBaseRateChangeId(batxBaseRateChange.getBatxBaseRateChangeId());
    return batxBaseRateChangeT.isPresent() ? batxBaseRateChangeT.get() : null;
  }

  @Override
  public BatxBaseRateChange insert(BatxBaseRateChange batxBaseRateChange, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + batxBaseRateChange.getBatxBaseRateChangeId());
    if (this.findById(batxBaseRateChange.getBatxBaseRateChangeId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      batxBaseRateChange.setCreateEmpNo(empNot);

    if(batxBaseRateChange.getLastUpdateEmpNo() == null || batxBaseRateChange.getLastUpdateEmpNo().isEmpty())
      batxBaseRateChange.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return batxBaseRateChangeReposDay.saveAndFlush(batxBaseRateChange);	
    else if (dbName.equals(ContentName.onMon))
      return batxBaseRateChangeReposMon.saveAndFlush(batxBaseRateChange);
    else if (dbName.equals(ContentName.onHist))
      return batxBaseRateChangeReposHist.saveAndFlush(batxBaseRateChange);
    else 
    return batxBaseRateChangeRepos.saveAndFlush(batxBaseRateChange);
  }

  @Override
  public BatxBaseRateChange update(BatxBaseRateChange batxBaseRateChange, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + batxBaseRateChange.getBatxBaseRateChangeId());
    if (!empNot.isEmpty())
      batxBaseRateChange.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return batxBaseRateChangeReposDay.saveAndFlush(batxBaseRateChange);	
    else if (dbName.equals(ContentName.onMon))
      return batxBaseRateChangeReposMon.saveAndFlush(batxBaseRateChange);
    else if (dbName.equals(ContentName.onHist))
      return batxBaseRateChangeReposHist.saveAndFlush(batxBaseRateChange);
    else 
    return batxBaseRateChangeRepos.saveAndFlush(batxBaseRateChange);
  }

  @Override
  public BatxBaseRateChange update2(BatxBaseRateChange batxBaseRateChange, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + batxBaseRateChange.getBatxBaseRateChangeId());
    if (!empNot.isEmpty())
      batxBaseRateChange.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      batxBaseRateChangeReposDay.saveAndFlush(batxBaseRateChange);	
    else if (dbName.equals(ContentName.onMon))
      batxBaseRateChangeReposMon.saveAndFlush(batxBaseRateChange);
    else if (dbName.equals(ContentName.onHist))
        batxBaseRateChangeReposHist.saveAndFlush(batxBaseRateChange);
    else 
      batxBaseRateChangeRepos.saveAndFlush(batxBaseRateChange);	
    return this.findById(batxBaseRateChange.getBatxBaseRateChangeId());
  }

  @Override
  public void delete(BatxBaseRateChange batxBaseRateChange, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + batxBaseRateChange.getBatxBaseRateChangeId());
    if (dbName.equals(ContentName.onDay)) {
      batxBaseRateChangeReposDay.delete(batxBaseRateChange);	
      batxBaseRateChangeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      batxBaseRateChangeReposMon.delete(batxBaseRateChange);	
      batxBaseRateChangeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      batxBaseRateChangeReposHist.delete(batxBaseRateChange);
      batxBaseRateChangeReposHist.flush();
    }
    else {
      batxBaseRateChangeRepos.delete(batxBaseRateChange);
      batxBaseRateChangeRepos.flush();
    }
   }

  @Override
  public void insertAll(List<BatxBaseRateChange> batxBaseRateChange, TitaVo... titaVo) throws DBException {
    if (batxBaseRateChange == null || batxBaseRateChange.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (BatxBaseRateChange t : batxBaseRateChange){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      batxBaseRateChange = batxBaseRateChangeReposDay.saveAll(batxBaseRateChange);	
      batxBaseRateChangeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      batxBaseRateChange = batxBaseRateChangeReposMon.saveAll(batxBaseRateChange);	
      batxBaseRateChangeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      batxBaseRateChange = batxBaseRateChangeReposHist.saveAll(batxBaseRateChange);
      batxBaseRateChangeReposHist.flush();
    }
    else {
      batxBaseRateChange = batxBaseRateChangeRepos.saveAll(batxBaseRateChange);
      batxBaseRateChangeRepos.flush();
    }
    }

  @Override
  public void updateAll(List<BatxBaseRateChange> batxBaseRateChange, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (batxBaseRateChange == null || batxBaseRateChange.size() == 0)
      throw new DBException(6);

    for (BatxBaseRateChange t : batxBaseRateChange) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      batxBaseRateChange = batxBaseRateChangeReposDay.saveAll(batxBaseRateChange);	
      batxBaseRateChangeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      batxBaseRateChange = batxBaseRateChangeReposMon.saveAll(batxBaseRateChange);	
      batxBaseRateChangeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      batxBaseRateChange = batxBaseRateChangeReposHist.saveAll(batxBaseRateChange);
      batxBaseRateChangeReposHist.flush();
    }
    else {
      batxBaseRateChange = batxBaseRateChangeRepos.saveAll(batxBaseRateChange);
      batxBaseRateChangeRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<BatxBaseRateChange> batxBaseRateChange, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (batxBaseRateChange == null || batxBaseRateChange.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      batxBaseRateChangeReposDay.deleteAll(batxBaseRateChange);	
      batxBaseRateChangeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      batxBaseRateChangeReposMon.deleteAll(batxBaseRateChange);	
      batxBaseRateChangeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      batxBaseRateChangeReposHist.deleteAll(batxBaseRateChange);
      batxBaseRateChangeReposHist.flush();
    }
    else {
      batxBaseRateChangeRepos.deleteAll(batxBaseRateChange);
      batxBaseRateChangeRepos.flush();
    }
  }

}
