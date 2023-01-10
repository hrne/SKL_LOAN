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
import com.st1.itx.db.domain.FinHoldRel;
import com.st1.itx.db.repository.online.FinHoldRelRepository;
import com.st1.itx.db.repository.day.FinHoldRelRepositoryDay;
import com.st1.itx.db.repository.mon.FinHoldRelRepositoryMon;
import com.st1.itx.db.repository.hist.FinHoldRelRepositoryHist;
import com.st1.itx.db.service.FinHoldRelService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("finHoldRelService")
@Repository
public class FinHoldRelServiceImpl extends ASpringJpaParm implements FinHoldRelService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private FinHoldRelRepository finHoldRelRepos;

  @Autowired
  private FinHoldRelRepositoryDay finHoldRelReposDay;

  @Autowired
  private FinHoldRelRepositoryMon finHoldRelReposMon;

  @Autowired
  private FinHoldRelRepositoryHist finHoldRelReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(finHoldRelRepos);
    org.junit.Assert.assertNotNull(finHoldRelReposDay);
    org.junit.Assert.assertNotNull(finHoldRelReposMon);
    org.junit.Assert.assertNotNull(finHoldRelReposHist);
  }

  @Override
  public FinHoldRel findById(String id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + id);
    Optional<FinHoldRel> finHoldRel = null;
    if (dbName.equals(ContentName.onDay))
      finHoldRel = finHoldRelReposDay.findById(id);
    else if (dbName.equals(ContentName.onMon))
      finHoldRel = finHoldRelReposMon.findById(id);
    else if (dbName.equals(ContentName.onHist))
      finHoldRel = finHoldRelReposHist.findById(id);
    else 
      finHoldRel = finHoldRelRepos.findById(id);
    FinHoldRel obj = finHoldRel.isPresent() ? finHoldRel.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<FinHoldRel> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FinHoldRel> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Id"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Id"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = finHoldRelReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = finHoldRelReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = finHoldRelReposHist.findAll(pageable);
    else 
      slice = finHoldRelRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public FinHoldRel holdById(String id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + id);
    Optional<FinHoldRel> finHoldRel = null;
    if (dbName.equals(ContentName.onDay))
      finHoldRel = finHoldRelReposDay.findById(id);
    else if (dbName.equals(ContentName.onMon))
      finHoldRel = finHoldRelReposMon.findById(id);
    else if (dbName.equals(ContentName.onHist))
      finHoldRel = finHoldRelReposHist.findById(id);
    else 
      finHoldRel = finHoldRelRepos.findById(id);
    return finHoldRel.isPresent() ? finHoldRel.get() : null;
  }

  @Override
  public FinHoldRel holdById(FinHoldRel finHoldRel, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + finHoldRel.getId());
    Optional<FinHoldRel> finHoldRelT = null;
    if (dbName.equals(ContentName.onDay))
      finHoldRelT = finHoldRelReposDay.findById(finHoldRel.getId());
    else if (dbName.equals(ContentName.onMon))
      finHoldRelT = finHoldRelReposMon.findById(finHoldRel.getId());
    else if (dbName.equals(ContentName.onHist))
      finHoldRelT = finHoldRelReposHist.findById(finHoldRel.getId());
    else 
      finHoldRelT = finHoldRelRepos.findById(finHoldRel.getId());
    return finHoldRelT.isPresent() ? finHoldRelT.get() : null;
  }

  @Override
  public FinHoldRel insert(FinHoldRel finHoldRel, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + finHoldRel.getId());
    if (this.findById(finHoldRel.getId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      finHoldRel.setCreateEmpNo(empNot);

    if(finHoldRel.getLastUpdateEmpNo() == null || finHoldRel.getLastUpdateEmpNo().isEmpty())
      finHoldRel.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return finHoldRelReposDay.saveAndFlush(finHoldRel);	
    else if (dbName.equals(ContentName.onMon))
      return finHoldRelReposMon.saveAndFlush(finHoldRel);
    else if (dbName.equals(ContentName.onHist))
      return finHoldRelReposHist.saveAndFlush(finHoldRel);
    else 
    return finHoldRelRepos.saveAndFlush(finHoldRel);
  }

  @Override
  public FinHoldRel update(FinHoldRel finHoldRel, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + finHoldRel.getId());
    if (!empNot.isEmpty())
      finHoldRel.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return finHoldRelReposDay.saveAndFlush(finHoldRel);	
    else if (dbName.equals(ContentName.onMon))
      return finHoldRelReposMon.saveAndFlush(finHoldRel);
    else if (dbName.equals(ContentName.onHist))
      return finHoldRelReposHist.saveAndFlush(finHoldRel);
    else 
    return finHoldRelRepos.saveAndFlush(finHoldRel);
  }

  @Override
  public FinHoldRel update2(FinHoldRel finHoldRel, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + finHoldRel.getId());
    if (!empNot.isEmpty())
      finHoldRel.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      finHoldRelReposDay.saveAndFlush(finHoldRel);	
    else if (dbName.equals(ContentName.onMon))
      finHoldRelReposMon.saveAndFlush(finHoldRel);
    else if (dbName.equals(ContentName.onHist))
        finHoldRelReposHist.saveAndFlush(finHoldRel);
    else 
      finHoldRelRepos.saveAndFlush(finHoldRel);	
    return this.findById(finHoldRel.getId());
  }

  @Override
  public void delete(FinHoldRel finHoldRel, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + finHoldRel.getId());
    if (dbName.equals(ContentName.onDay)) {
      finHoldRelReposDay.delete(finHoldRel);	
      finHoldRelReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finHoldRelReposMon.delete(finHoldRel);	
      finHoldRelReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finHoldRelReposHist.delete(finHoldRel);
      finHoldRelReposHist.flush();
    }
    else {
      finHoldRelRepos.delete(finHoldRel);
      finHoldRelRepos.flush();
    }
   }

  @Override
  public void insertAll(List<FinHoldRel> finHoldRel, TitaVo... titaVo) throws DBException {
    if (finHoldRel == null || finHoldRel.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (FinHoldRel t : finHoldRel){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      finHoldRel = finHoldRelReposDay.saveAll(finHoldRel);	
      finHoldRelReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finHoldRel = finHoldRelReposMon.saveAll(finHoldRel);	
      finHoldRelReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finHoldRel = finHoldRelReposHist.saveAll(finHoldRel);
      finHoldRelReposHist.flush();
    }
    else {
      finHoldRel = finHoldRelRepos.saveAll(finHoldRel);
      finHoldRelRepos.flush();
    }
    }

  @Override
  public void updateAll(List<FinHoldRel> finHoldRel, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (finHoldRel == null || finHoldRel.size() == 0)
      throw new DBException(6);

    for (FinHoldRel t : finHoldRel) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      finHoldRel = finHoldRelReposDay.saveAll(finHoldRel);	
      finHoldRelReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finHoldRel = finHoldRelReposMon.saveAll(finHoldRel);	
      finHoldRelReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finHoldRel = finHoldRelReposHist.saveAll(finHoldRel);
      finHoldRelReposHist.flush();
    }
    else {
      finHoldRel = finHoldRelRepos.saveAll(finHoldRel);
      finHoldRelRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<FinHoldRel> finHoldRel, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (finHoldRel == null || finHoldRel.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      finHoldRelReposDay.deleteAll(finHoldRel);	
      finHoldRelReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finHoldRelReposMon.deleteAll(finHoldRel);	
      finHoldRelReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finHoldRelReposHist.deleteAll(finHoldRel);
      finHoldRelReposHist.flush();
    }
    else {
      finHoldRelRepos.deleteAll(finHoldRel);
      finHoldRelRepos.flush();
    }
  }

}
