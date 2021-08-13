package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.st1.itx.db.domain.ClBuildingParking;
import com.st1.itx.db.domain.ClBuildingParkingId;
import com.st1.itx.db.repository.online.ClBuildingParkingRepository;
import com.st1.itx.db.repository.day.ClBuildingParkingRepositoryDay;
import com.st1.itx.db.repository.mon.ClBuildingParkingRepositoryMon;
import com.st1.itx.db.repository.hist.ClBuildingParkingRepositoryHist;
import com.st1.itx.db.service.ClBuildingParkingService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clBuildingParkingService")
@Repository
public class ClBuildingParkingServiceImpl implements ClBuildingParkingService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(ClBuildingParkingServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private ClBuildingParkingRepository clBuildingParkingRepos;

  @Autowired
  private ClBuildingParkingRepositoryDay clBuildingParkingReposDay;

  @Autowired
  private ClBuildingParkingRepositoryMon clBuildingParkingReposMon;

  @Autowired
  private ClBuildingParkingRepositoryHist clBuildingParkingReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(clBuildingParkingRepos);
    org.junit.Assert.assertNotNull(clBuildingParkingReposDay);
    org.junit.Assert.assertNotNull(clBuildingParkingReposMon);
    org.junit.Assert.assertNotNull(clBuildingParkingReposHist);
  }

  @Override
  public ClBuildingParking findById(ClBuildingParkingId clBuildingParkingId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + clBuildingParkingId);
    Optional<ClBuildingParking> clBuildingParking = null;
    if (dbName.equals(ContentName.onDay))
      clBuildingParking = clBuildingParkingReposDay.findById(clBuildingParkingId);
    else if (dbName.equals(ContentName.onMon))
      clBuildingParking = clBuildingParkingReposMon.findById(clBuildingParkingId);
    else if (dbName.equals(ContentName.onHist))
      clBuildingParking = clBuildingParkingReposHist.findById(clBuildingParkingId);
    else 
      clBuildingParking = clBuildingParkingRepos.findById(clBuildingParkingId);
    ClBuildingParking obj = clBuildingParking.isPresent() ? clBuildingParking.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<ClBuildingParking> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClBuildingParking> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "ParkingBdNo1", "ParkingBdNo2"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = clBuildingParkingReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clBuildingParkingReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clBuildingParkingReposHist.findAll(pageable);
    else 
      slice = clBuildingParkingRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClBuildingParking> clNoEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClBuildingParking> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("clNoEq " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = clBuildingParkingReposDay.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clBuildingParkingReposMon.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clBuildingParkingReposHist.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
    else 
      slice = clBuildingParkingRepos.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClBuildingParking> parkingBdNo1Eq(int parkingBdNo1_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClBuildingParking> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("parkingBdNo1Eq " + dbName + " : " + "parkingBdNo1_0 : " + parkingBdNo1_0);
    if (dbName.equals(ContentName.onDay))
      slice = clBuildingParkingReposDay.findAllByParkingBdNo1Is(parkingBdNo1_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clBuildingParkingReposMon.findAllByParkingBdNo1Is(parkingBdNo1_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clBuildingParkingReposHist.findAllByParkingBdNo1Is(parkingBdNo1_0, pageable);
    else 
      slice = clBuildingParkingRepos.findAllByParkingBdNo1Is(parkingBdNo1_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClBuildingParking> parkingBdNo2Eq(int parkingBdNo1_0, int parkingBdNo2_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClBuildingParking> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("parkingBdNo2Eq " + dbName + " : " + "parkingBdNo1_0 : " + parkingBdNo1_0 + " parkingBdNo2_1 : " +  parkingBdNo2_1);
    if (dbName.equals(ContentName.onDay))
      slice = clBuildingParkingReposDay.findAllByParkingBdNo1IsAndParkingBdNo2Is(parkingBdNo1_0, parkingBdNo2_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clBuildingParkingReposMon.findAllByParkingBdNo1IsAndParkingBdNo2Is(parkingBdNo1_0, parkingBdNo2_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clBuildingParkingReposHist.findAllByParkingBdNo1IsAndParkingBdNo2Is(parkingBdNo1_0, parkingBdNo2_1, pageable);
    else 
      slice = clBuildingParkingRepos.findAllByParkingBdNo1IsAndParkingBdNo2Is(parkingBdNo1_0, parkingBdNo2_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ClBuildingParking holdById(ClBuildingParkingId clBuildingParkingId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + clBuildingParkingId);
    Optional<ClBuildingParking> clBuildingParking = null;
    if (dbName.equals(ContentName.onDay))
      clBuildingParking = clBuildingParkingReposDay.findByClBuildingParkingId(clBuildingParkingId);
    else if (dbName.equals(ContentName.onMon))
      clBuildingParking = clBuildingParkingReposMon.findByClBuildingParkingId(clBuildingParkingId);
    else if (dbName.equals(ContentName.onHist))
      clBuildingParking = clBuildingParkingReposHist.findByClBuildingParkingId(clBuildingParkingId);
    else 
      clBuildingParking = clBuildingParkingRepos.findByClBuildingParkingId(clBuildingParkingId);
    return clBuildingParking.isPresent() ? clBuildingParking.get() : null;
  }

  @Override
  public ClBuildingParking holdById(ClBuildingParking clBuildingParking, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + clBuildingParking.getClBuildingParkingId());
    Optional<ClBuildingParking> clBuildingParkingT = null;
    if (dbName.equals(ContentName.onDay))
      clBuildingParkingT = clBuildingParkingReposDay.findByClBuildingParkingId(clBuildingParking.getClBuildingParkingId());
    else if (dbName.equals(ContentName.onMon))
      clBuildingParkingT = clBuildingParkingReposMon.findByClBuildingParkingId(clBuildingParking.getClBuildingParkingId());
    else if (dbName.equals(ContentName.onHist))
      clBuildingParkingT = clBuildingParkingReposHist.findByClBuildingParkingId(clBuildingParking.getClBuildingParkingId());
    else 
      clBuildingParkingT = clBuildingParkingRepos.findByClBuildingParkingId(clBuildingParking.getClBuildingParkingId());
    return clBuildingParkingT.isPresent() ? clBuildingParkingT.get() : null;
  }

  @Override
  public ClBuildingParking insert(ClBuildingParking clBuildingParking, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + clBuildingParking.getClBuildingParkingId());
    if (this.findById(clBuildingParking.getClBuildingParkingId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      clBuildingParking.setCreateEmpNo(empNot);

    if(clBuildingParking.getLastUpdateEmpNo() == null || clBuildingParking.getLastUpdateEmpNo().isEmpty())
      clBuildingParking.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clBuildingParkingReposDay.saveAndFlush(clBuildingParking);	
    else if (dbName.equals(ContentName.onMon))
      return clBuildingParkingReposMon.saveAndFlush(clBuildingParking);
    else if (dbName.equals(ContentName.onHist))
      return clBuildingParkingReposHist.saveAndFlush(clBuildingParking);
    else 
    return clBuildingParkingRepos.saveAndFlush(clBuildingParking);
  }

  @Override
  public ClBuildingParking update(ClBuildingParking clBuildingParking, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + clBuildingParking.getClBuildingParkingId());
    if (!empNot.isEmpty())
      clBuildingParking.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clBuildingParkingReposDay.saveAndFlush(clBuildingParking);	
    else if (dbName.equals(ContentName.onMon))
      return clBuildingParkingReposMon.saveAndFlush(clBuildingParking);
    else if (dbName.equals(ContentName.onHist))
      return clBuildingParkingReposHist.saveAndFlush(clBuildingParking);
    else 
    return clBuildingParkingRepos.saveAndFlush(clBuildingParking);
  }

  @Override
  public ClBuildingParking update2(ClBuildingParking clBuildingParking, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + clBuildingParking.getClBuildingParkingId());
    if (!empNot.isEmpty())
      clBuildingParking.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      clBuildingParkingReposDay.saveAndFlush(clBuildingParking);	
    else if (dbName.equals(ContentName.onMon))
      clBuildingParkingReposMon.saveAndFlush(clBuildingParking);
    else if (dbName.equals(ContentName.onHist))
        clBuildingParkingReposHist.saveAndFlush(clBuildingParking);
    else 
      clBuildingParkingRepos.saveAndFlush(clBuildingParking);	
    return this.findById(clBuildingParking.getClBuildingParkingId());
  }

  @Override
  public void delete(ClBuildingParking clBuildingParking, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + clBuildingParking.getClBuildingParkingId());
    if (dbName.equals(ContentName.onDay)) {
      clBuildingParkingReposDay.delete(clBuildingParking);	
      clBuildingParkingReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clBuildingParkingReposMon.delete(clBuildingParking);	
      clBuildingParkingReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clBuildingParkingReposHist.delete(clBuildingParking);
      clBuildingParkingReposHist.flush();
    }
    else {
      clBuildingParkingRepos.delete(clBuildingParking);
      clBuildingParkingRepos.flush();
    }
   }

  @Override
  public void insertAll(List<ClBuildingParking> clBuildingParking, TitaVo... titaVo) throws DBException {
    if (clBuildingParking == null || clBuildingParking.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (ClBuildingParking t : clBuildingParking){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      clBuildingParking = clBuildingParkingReposDay.saveAll(clBuildingParking);	
      clBuildingParkingReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clBuildingParking = clBuildingParkingReposMon.saveAll(clBuildingParking);	
      clBuildingParkingReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clBuildingParking = clBuildingParkingReposHist.saveAll(clBuildingParking);
      clBuildingParkingReposHist.flush();
    }
    else {
      clBuildingParking = clBuildingParkingRepos.saveAll(clBuildingParking);
      clBuildingParkingRepos.flush();
    }
    }

  @Override
  public void updateAll(List<ClBuildingParking> clBuildingParking, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (clBuildingParking == null || clBuildingParking.size() == 0)
      throw new DBException(6);

    for (ClBuildingParking t : clBuildingParking) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      clBuildingParking = clBuildingParkingReposDay.saveAll(clBuildingParking);	
      clBuildingParkingReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clBuildingParking = clBuildingParkingReposMon.saveAll(clBuildingParking);	
      clBuildingParkingReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clBuildingParking = clBuildingParkingReposHist.saveAll(clBuildingParking);
      clBuildingParkingReposHist.flush();
    }
    else {
      clBuildingParking = clBuildingParkingRepos.saveAll(clBuildingParking);
      clBuildingParkingRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<ClBuildingParking> clBuildingParking, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (clBuildingParking == null || clBuildingParking.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      clBuildingParkingReposDay.deleteAll(clBuildingParking);	
      clBuildingParkingReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clBuildingParkingReposMon.deleteAll(clBuildingParking);	
      clBuildingParkingReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clBuildingParkingReposHist.deleteAll(clBuildingParking);
      clBuildingParkingReposHist.flush();
    }
    else {
      clBuildingParkingRepos.deleteAll(clBuildingParking);
      clBuildingParkingRepos.flush();
    }
  }

}
