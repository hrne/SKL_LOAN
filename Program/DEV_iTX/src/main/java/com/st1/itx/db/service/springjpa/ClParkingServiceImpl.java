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
import com.st1.itx.db.domain.ClParking;
import com.st1.itx.db.domain.ClParkingId;
import com.st1.itx.db.repository.online.ClParkingRepository;
import com.st1.itx.db.repository.day.ClParkingRepositoryDay;
import com.st1.itx.db.repository.mon.ClParkingRepositoryMon;
import com.st1.itx.db.repository.hist.ClParkingRepositoryHist;
import com.st1.itx.db.service.ClParkingService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clParkingService")
@Repository
public class ClParkingServiceImpl extends ASpringJpaParm implements ClParkingService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private ClParkingRepository clParkingRepos;

  @Autowired
  private ClParkingRepositoryDay clParkingReposDay;

  @Autowired
  private ClParkingRepositoryMon clParkingReposMon;

  @Autowired
  private ClParkingRepositoryHist clParkingReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(clParkingRepos);
    org.junit.Assert.assertNotNull(clParkingReposDay);
    org.junit.Assert.assertNotNull(clParkingReposMon);
    org.junit.Assert.assertNotNull(clParkingReposHist);
  }

  @Override
  public ClParking findById(ClParkingId clParkingId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + clParkingId);
    Optional<ClParking> clParking = null;
    if (dbName.equals(ContentName.onDay))
      clParking = clParkingReposDay.findById(clParkingId);
    else if (dbName.equals(ContentName.onMon))
      clParking = clParkingReposMon.findById(clParkingId);
    else if (dbName.equals(ContentName.onHist))
      clParking = clParkingReposHist.findById(clParkingId);
    else 
      clParking = clParkingRepos.findById(clParkingId);
    ClParking obj = clParking.isPresent() ? clParking.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<ClParking> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClParking> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "ParkingSeqNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = clParkingReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clParkingReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clParkingReposHist.findAll(pageable);
    else 
      slice = clParkingRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClParking> clNoEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClParking> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("clNoEq " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = clParkingReposDay.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clParkingReposMon.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clParkingReposHist.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
    else 
      slice = clParkingRepos.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ClParking holdById(ClParkingId clParkingId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clParkingId);
    Optional<ClParking> clParking = null;
    if (dbName.equals(ContentName.onDay))
      clParking = clParkingReposDay.findByClParkingId(clParkingId);
    else if (dbName.equals(ContentName.onMon))
      clParking = clParkingReposMon.findByClParkingId(clParkingId);
    else if (dbName.equals(ContentName.onHist))
      clParking = clParkingReposHist.findByClParkingId(clParkingId);
    else 
      clParking = clParkingRepos.findByClParkingId(clParkingId);
    return clParking.isPresent() ? clParking.get() : null;
  }

  @Override
  public ClParking holdById(ClParking clParking, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clParking.getClParkingId());
    Optional<ClParking> clParkingT = null;
    if (dbName.equals(ContentName.onDay))
      clParkingT = clParkingReposDay.findByClParkingId(clParking.getClParkingId());
    else if (dbName.equals(ContentName.onMon))
      clParkingT = clParkingReposMon.findByClParkingId(clParking.getClParkingId());
    else if (dbName.equals(ContentName.onHist))
      clParkingT = clParkingReposHist.findByClParkingId(clParking.getClParkingId());
    else 
      clParkingT = clParkingRepos.findByClParkingId(clParking.getClParkingId());
    return clParkingT.isPresent() ? clParkingT.get() : null;
  }

  @Override
  public ClParking insert(ClParking clParking, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + clParking.getClParkingId());
    if (this.findById(clParking.getClParkingId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      clParking.setCreateEmpNo(empNot);

    if(clParking.getLastUpdateEmpNo() == null || clParking.getLastUpdateEmpNo().isEmpty())
      clParking.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clParkingReposDay.saveAndFlush(clParking);	
    else if (dbName.equals(ContentName.onMon))
      return clParkingReposMon.saveAndFlush(clParking);
    else if (dbName.equals(ContentName.onHist))
      return clParkingReposHist.saveAndFlush(clParking);
    else 
    return clParkingRepos.saveAndFlush(clParking);
  }

  @Override
  public ClParking update(ClParking clParking, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + clParking.getClParkingId());
    if (!empNot.isEmpty())
      clParking.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clParkingReposDay.saveAndFlush(clParking);	
    else if (dbName.equals(ContentName.onMon))
      return clParkingReposMon.saveAndFlush(clParking);
    else if (dbName.equals(ContentName.onHist))
      return clParkingReposHist.saveAndFlush(clParking);
    else 
    return clParkingRepos.saveAndFlush(clParking);
  }

  @Override
  public ClParking update2(ClParking clParking, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + clParking.getClParkingId());
    if (!empNot.isEmpty())
      clParking.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      clParkingReposDay.saveAndFlush(clParking);	
    else if (dbName.equals(ContentName.onMon))
      clParkingReposMon.saveAndFlush(clParking);
    else if (dbName.equals(ContentName.onHist))
        clParkingReposHist.saveAndFlush(clParking);
    else 
      clParkingRepos.saveAndFlush(clParking);	
    return this.findById(clParking.getClParkingId());
  }

  @Override
  public void delete(ClParking clParking, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + clParking.getClParkingId());
    if (dbName.equals(ContentName.onDay)) {
      clParkingReposDay.delete(clParking);	
      clParkingReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clParkingReposMon.delete(clParking);	
      clParkingReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clParkingReposHist.delete(clParking);
      clParkingReposHist.flush();
    }
    else {
      clParkingRepos.delete(clParking);
      clParkingRepos.flush();
    }
   }

  @Override
  public void insertAll(List<ClParking> clParking, TitaVo... titaVo) throws DBException {
    if (clParking == null || clParking.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (ClParking t : clParking){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      clParking = clParkingReposDay.saveAll(clParking);	
      clParkingReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clParking = clParkingReposMon.saveAll(clParking);	
      clParkingReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clParking = clParkingReposHist.saveAll(clParking);
      clParkingReposHist.flush();
    }
    else {
      clParking = clParkingRepos.saveAll(clParking);
      clParkingRepos.flush();
    }
    }

  @Override
  public void updateAll(List<ClParking> clParking, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (clParking == null || clParking.size() == 0)
      throw new DBException(6);

    for (ClParking t : clParking) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      clParking = clParkingReposDay.saveAll(clParking);	
      clParkingReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clParking = clParkingReposMon.saveAll(clParking);	
      clParkingReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clParking = clParkingReposHist.saveAll(clParking);
      clParkingReposHist.flush();
    }
    else {
      clParking = clParkingRepos.saveAll(clParking);
      clParkingRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<ClParking> clParking, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (clParking == null || clParking.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      clParkingReposDay.deleteAll(clParking);	
      clParkingReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clParkingReposMon.deleteAll(clParking);	
      clParkingReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clParkingReposHist.deleteAll(clParking);
      clParkingReposHist.flush();
    }
    else {
      clParkingRepos.deleteAll(clParking);
      clParkingRepos.flush();
    }
  }

}
