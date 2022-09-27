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
import com.st1.itx.db.domain.StakeholdersStaff;
import com.st1.itx.db.repository.online.StakeholdersStaffRepository;
import com.st1.itx.db.repository.day.StakeholdersStaffRepositoryDay;
import com.st1.itx.db.repository.mon.StakeholdersStaffRepositoryMon;
import com.st1.itx.db.repository.hist.StakeholdersStaffRepositoryHist;
import com.st1.itx.db.service.StakeholdersStaffService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("stakeholdersStaffService")
@Repository
public class StakeholdersStaffServiceImpl extends ASpringJpaParm implements StakeholdersStaffService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private StakeholdersStaffRepository stakeholdersStaffRepos;

  @Autowired
  private StakeholdersStaffRepositoryDay stakeholdersStaffReposDay;

  @Autowired
  private StakeholdersStaffRepositoryMon stakeholdersStaffReposMon;

  @Autowired
  private StakeholdersStaffRepositoryHist stakeholdersStaffReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(stakeholdersStaffRepos);
    org.junit.Assert.assertNotNull(stakeholdersStaffReposDay);
    org.junit.Assert.assertNotNull(stakeholdersStaffReposMon);
    org.junit.Assert.assertNotNull(stakeholdersStaffReposHist);
  }

  @Override
  public StakeholdersStaff findById(String staffId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + staffId);
    Optional<StakeholdersStaff> stakeholdersStaff = null;
    if (dbName.equals(ContentName.onDay))
      stakeholdersStaff = stakeholdersStaffReposDay.findById(staffId);
    else if (dbName.equals(ContentName.onMon))
      stakeholdersStaff = stakeholdersStaffReposMon.findById(staffId);
    else if (dbName.equals(ContentName.onHist))
      stakeholdersStaff = stakeholdersStaffReposHist.findById(staffId);
    else 
      stakeholdersStaff = stakeholdersStaffRepos.findById(staffId);
    StakeholdersStaff obj = stakeholdersStaff.isPresent() ? stakeholdersStaff.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<StakeholdersStaff> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<StakeholdersStaff> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "StaffId"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "StaffId"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = stakeholdersStaffReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = stakeholdersStaffReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = stakeholdersStaffReposHist.findAll(pageable);
    else 
      slice = stakeholdersStaffRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public StakeholdersStaff holdById(String staffId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + staffId);
    Optional<StakeholdersStaff> stakeholdersStaff = null;
    if (dbName.equals(ContentName.onDay))
      stakeholdersStaff = stakeholdersStaffReposDay.findByStaffId(staffId);
    else if (dbName.equals(ContentName.onMon))
      stakeholdersStaff = stakeholdersStaffReposMon.findByStaffId(staffId);
    else if (dbName.equals(ContentName.onHist))
      stakeholdersStaff = stakeholdersStaffReposHist.findByStaffId(staffId);
    else 
      stakeholdersStaff = stakeholdersStaffRepos.findByStaffId(staffId);
    return stakeholdersStaff.isPresent() ? stakeholdersStaff.get() : null;
  }

  @Override
  public StakeholdersStaff holdById(StakeholdersStaff stakeholdersStaff, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + stakeholdersStaff.getStaffId());
    Optional<StakeholdersStaff> stakeholdersStaffT = null;
    if (dbName.equals(ContentName.onDay))
      stakeholdersStaffT = stakeholdersStaffReposDay.findByStaffId(stakeholdersStaff.getStaffId());
    else if (dbName.equals(ContentName.onMon))
      stakeholdersStaffT = stakeholdersStaffReposMon.findByStaffId(stakeholdersStaff.getStaffId());
    else if (dbName.equals(ContentName.onHist))
      stakeholdersStaffT = stakeholdersStaffReposHist.findByStaffId(stakeholdersStaff.getStaffId());
    else 
      stakeholdersStaffT = stakeholdersStaffRepos.findByStaffId(stakeholdersStaff.getStaffId());
    return stakeholdersStaffT.isPresent() ? stakeholdersStaffT.get() : null;
  }

  @Override
  public StakeholdersStaff insert(StakeholdersStaff stakeholdersStaff, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + stakeholdersStaff.getStaffId());
    if (this.findById(stakeholdersStaff.getStaffId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      stakeholdersStaff.setCreateEmpNo(empNot);

    if(stakeholdersStaff.getLastUpdateEmpNo() == null || stakeholdersStaff.getLastUpdateEmpNo().isEmpty())
      stakeholdersStaff.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return stakeholdersStaffReposDay.saveAndFlush(stakeholdersStaff);	
    else if (dbName.equals(ContentName.onMon))
      return stakeholdersStaffReposMon.saveAndFlush(stakeholdersStaff);
    else if (dbName.equals(ContentName.onHist))
      return stakeholdersStaffReposHist.saveAndFlush(stakeholdersStaff);
    else 
    return stakeholdersStaffRepos.saveAndFlush(stakeholdersStaff);
  }

  @Override
  public StakeholdersStaff update(StakeholdersStaff stakeholdersStaff, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + stakeholdersStaff.getStaffId());
    if (!empNot.isEmpty())
      stakeholdersStaff.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return stakeholdersStaffReposDay.saveAndFlush(stakeholdersStaff);	
    else if (dbName.equals(ContentName.onMon))
      return stakeholdersStaffReposMon.saveAndFlush(stakeholdersStaff);
    else if (dbName.equals(ContentName.onHist))
      return stakeholdersStaffReposHist.saveAndFlush(stakeholdersStaff);
    else 
    return stakeholdersStaffRepos.saveAndFlush(stakeholdersStaff);
  }

  @Override
  public StakeholdersStaff update2(StakeholdersStaff stakeholdersStaff, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + stakeholdersStaff.getStaffId());
    if (!empNot.isEmpty())
      stakeholdersStaff.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      stakeholdersStaffReposDay.saveAndFlush(stakeholdersStaff);	
    else if (dbName.equals(ContentName.onMon))
      stakeholdersStaffReposMon.saveAndFlush(stakeholdersStaff);
    else if (dbName.equals(ContentName.onHist))
        stakeholdersStaffReposHist.saveAndFlush(stakeholdersStaff);
    else 
      stakeholdersStaffRepos.saveAndFlush(stakeholdersStaff);	
    return this.findById(stakeholdersStaff.getStaffId());
  }

  @Override
  public void delete(StakeholdersStaff stakeholdersStaff, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + stakeholdersStaff.getStaffId());
    if (dbName.equals(ContentName.onDay)) {
      stakeholdersStaffReposDay.delete(stakeholdersStaff);	
      stakeholdersStaffReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      stakeholdersStaffReposMon.delete(stakeholdersStaff);	
      stakeholdersStaffReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      stakeholdersStaffReposHist.delete(stakeholdersStaff);
      stakeholdersStaffReposHist.flush();
    }
    else {
      stakeholdersStaffRepos.delete(stakeholdersStaff);
      stakeholdersStaffRepos.flush();
    }
   }

  @Override
  public void insertAll(List<StakeholdersStaff> stakeholdersStaff, TitaVo... titaVo) throws DBException {
    if (stakeholdersStaff == null || stakeholdersStaff.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (StakeholdersStaff t : stakeholdersStaff){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      stakeholdersStaff = stakeholdersStaffReposDay.saveAll(stakeholdersStaff);	
      stakeholdersStaffReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      stakeholdersStaff = stakeholdersStaffReposMon.saveAll(stakeholdersStaff);	
      stakeholdersStaffReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      stakeholdersStaff = stakeholdersStaffReposHist.saveAll(stakeholdersStaff);
      stakeholdersStaffReposHist.flush();
    }
    else {
      stakeholdersStaff = stakeholdersStaffRepos.saveAll(stakeholdersStaff);
      stakeholdersStaffRepos.flush();
    }
    }

  @Override
  public void updateAll(List<StakeholdersStaff> stakeholdersStaff, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (stakeholdersStaff == null || stakeholdersStaff.size() == 0)
      throw new DBException(6);

    for (StakeholdersStaff t : stakeholdersStaff) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      stakeholdersStaff = stakeholdersStaffReposDay.saveAll(stakeholdersStaff);	
      stakeholdersStaffReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      stakeholdersStaff = stakeholdersStaffReposMon.saveAll(stakeholdersStaff);	
      stakeholdersStaffReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      stakeholdersStaff = stakeholdersStaffReposHist.saveAll(stakeholdersStaff);
      stakeholdersStaffReposHist.flush();
    }
    else {
      stakeholdersStaff = stakeholdersStaffRepos.saveAll(stakeholdersStaff);
      stakeholdersStaffRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<StakeholdersStaff> stakeholdersStaff, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (stakeholdersStaff == null || stakeholdersStaff.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      stakeholdersStaffReposDay.deleteAll(stakeholdersStaff);	
      stakeholdersStaffReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      stakeholdersStaffReposMon.deleteAll(stakeholdersStaff);	
      stakeholdersStaffReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      stakeholdersStaffReposHist.deleteAll(stakeholdersStaff);
      stakeholdersStaffReposHist.flush();
    }
    else {
      stakeholdersStaffRepos.deleteAll(stakeholdersStaff);
      stakeholdersStaffRepos.flush();
    }
  }

}
