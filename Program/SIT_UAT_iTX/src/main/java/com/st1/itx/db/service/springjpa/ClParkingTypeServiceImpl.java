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
import com.st1.itx.db.domain.ClParkingType;
import com.st1.itx.db.domain.ClParkingTypeId;
import com.st1.itx.db.repository.online.ClParkingTypeRepository;
import com.st1.itx.db.repository.day.ClParkingTypeRepositoryDay;
import com.st1.itx.db.repository.mon.ClParkingTypeRepositoryMon;
import com.st1.itx.db.repository.hist.ClParkingTypeRepositoryHist;
import com.st1.itx.db.service.ClParkingTypeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clParkingTypeService")
@Repository
public class ClParkingTypeServiceImpl extends ASpringJpaParm implements ClParkingTypeService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private ClParkingTypeRepository clParkingTypeRepos;

  @Autowired
  private ClParkingTypeRepositoryDay clParkingTypeReposDay;

  @Autowired
  private ClParkingTypeRepositoryMon clParkingTypeReposMon;

  @Autowired
  private ClParkingTypeRepositoryHist clParkingTypeReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(clParkingTypeRepos);
    org.junit.Assert.assertNotNull(clParkingTypeReposDay);
    org.junit.Assert.assertNotNull(clParkingTypeReposMon);
    org.junit.Assert.assertNotNull(clParkingTypeReposHist);
  }

  @Override
  public ClParkingType findById(ClParkingTypeId clParkingTypeId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + clParkingTypeId);
    Optional<ClParkingType> clParkingType = null;
    if (dbName.equals(ContentName.onDay))
      clParkingType = clParkingTypeReposDay.findById(clParkingTypeId);
    else if (dbName.equals(ContentName.onMon))
      clParkingType = clParkingTypeReposMon.findById(clParkingTypeId);
    else if (dbName.equals(ContentName.onHist))
      clParkingType = clParkingTypeReposHist.findById(clParkingTypeId);
    else 
      clParkingType = clParkingTypeRepos.findById(clParkingTypeId);
    ClParkingType obj = clParkingType.isPresent() ? clParkingType.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<ClParkingType> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClParkingType> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "ParkingTypeCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "ParkingTypeCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = clParkingTypeReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clParkingTypeReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clParkingTypeReposHist.findAll(pageable);
    else 
      slice = clParkingTypeRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClParkingType> clNoEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClParkingType> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("clNoEq " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = clParkingTypeReposDay.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clParkingTypeReposMon.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clParkingTypeReposHist.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
    else 
      slice = clParkingTypeRepos.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ClParkingType holdById(ClParkingTypeId clParkingTypeId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clParkingTypeId);
    Optional<ClParkingType> clParkingType = null;
    if (dbName.equals(ContentName.onDay))
      clParkingType = clParkingTypeReposDay.findByClParkingTypeId(clParkingTypeId);
    else if (dbName.equals(ContentName.onMon))
      clParkingType = clParkingTypeReposMon.findByClParkingTypeId(clParkingTypeId);
    else if (dbName.equals(ContentName.onHist))
      clParkingType = clParkingTypeReposHist.findByClParkingTypeId(clParkingTypeId);
    else 
      clParkingType = clParkingTypeRepos.findByClParkingTypeId(clParkingTypeId);
    return clParkingType.isPresent() ? clParkingType.get() : null;
  }

  @Override
  public ClParkingType holdById(ClParkingType clParkingType, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clParkingType.getClParkingTypeId());
    Optional<ClParkingType> clParkingTypeT = null;
    if (dbName.equals(ContentName.onDay))
      clParkingTypeT = clParkingTypeReposDay.findByClParkingTypeId(clParkingType.getClParkingTypeId());
    else if (dbName.equals(ContentName.onMon))
      clParkingTypeT = clParkingTypeReposMon.findByClParkingTypeId(clParkingType.getClParkingTypeId());
    else if (dbName.equals(ContentName.onHist))
      clParkingTypeT = clParkingTypeReposHist.findByClParkingTypeId(clParkingType.getClParkingTypeId());
    else 
      clParkingTypeT = clParkingTypeRepos.findByClParkingTypeId(clParkingType.getClParkingTypeId());
    return clParkingTypeT.isPresent() ? clParkingTypeT.get() : null;
  }

  @Override
  public ClParkingType insert(ClParkingType clParkingType, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + clParkingType.getClParkingTypeId());
    if (this.findById(clParkingType.getClParkingTypeId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      clParkingType.setCreateEmpNo(empNot);

    if(clParkingType.getLastUpdateEmpNo() == null || clParkingType.getLastUpdateEmpNo().isEmpty())
      clParkingType.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clParkingTypeReposDay.saveAndFlush(clParkingType);	
    else if (dbName.equals(ContentName.onMon))
      return clParkingTypeReposMon.saveAndFlush(clParkingType);
    else if (dbName.equals(ContentName.onHist))
      return clParkingTypeReposHist.saveAndFlush(clParkingType);
    else 
    return clParkingTypeRepos.saveAndFlush(clParkingType);
  }

  @Override
  public ClParkingType update(ClParkingType clParkingType, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + clParkingType.getClParkingTypeId());
    if (!empNot.isEmpty())
      clParkingType.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clParkingTypeReposDay.saveAndFlush(clParkingType);	
    else if (dbName.equals(ContentName.onMon))
      return clParkingTypeReposMon.saveAndFlush(clParkingType);
    else if (dbName.equals(ContentName.onHist))
      return clParkingTypeReposHist.saveAndFlush(clParkingType);
    else 
    return clParkingTypeRepos.saveAndFlush(clParkingType);
  }

  @Override
  public ClParkingType update2(ClParkingType clParkingType, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + clParkingType.getClParkingTypeId());
    if (!empNot.isEmpty())
      clParkingType.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      clParkingTypeReposDay.saveAndFlush(clParkingType);	
    else if (dbName.equals(ContentName.onMon))
      clParkingTypeReposMon.saveAndFlush(clParkingType);
    else if (dbName.equals(ContentName.onHist))
        clParkingTypeReposHist.saveAndFlush(clParkingType);
    else 
      clParkingTypeRepos.saveAndFlush(clParkingType);	
    return this.findById(clParkingType.getClParkingTypeId());
  }

  @Override
  public void delete(ClParkingType clParkingType, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + clParkingType.getClParkingTypeId());
    if (dbName.equals(ContentName.onDay)) {
      clParkingTypeReposDay.delete(clParkingType);	
      clParkingTypeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clParkingTypeReposMon.delete(clParkingType);	
      clParkingTypeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clParkingTypeReposHist.delete(clParkingType);
      clParkingTypeReposHist.flush();
    }
    else {
      clParkingTypeRepos.delete(clParkingType);
      clParkingTypeRepos.flush();
    }
   }

  @Override
  public void insertAll(List<ClParkingType> clParkingType, TitaVo... titaVo) throws DBException {
    if (clParkingType == null || clParkingType.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (ClParkingType t : clParkingType){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      clParkingType = clParkingTypeReposDay.saveAll(clParkingType);	
      clParkingTypeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clParkingType = clParkingTypeReposMon.saveAll(clParkingType);	
      clParkingTypeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clParkingType = clParkingTypeReposHist.saveAll(clParkingType);
      clParkingTypeReposHist.flush();
    }
    else {
      clParkingType = clParkingTypeRepos.saveAll(clParkingType);
      clParkingTypeRepos.flush();
    }
    }

  @Override
  public void updateAll(List<ClParkingType> clParkingType, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (clParkingType == null || clParkingType.size() == 0)
      throw new DBException(6);

    for (ClParkingType t : clParkingType) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      clParkingType = clParkingTypeReposDay.saveAll(clParkingType);	
      clParkingTypeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clParkingType = clParkingTypeReposMon.saveAll(clParkingType);	
      clParkingTypeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clParkingType = clParkingTypeReposHist.saveAll(clParkingType);
      clParkingTypeReposHist.flush();
    }
    else {
      clParkingType = clParkingTypeRepos.saveAll(clParkingType);
      clParkingTypeRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<ClParkingType> clParkingType, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (clParkingType == null || clParkingType.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      clParkingTypeReposDay.deleteAll(clParkingType);	
      clParkingTypeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clParkingTypeReposMon.deleteAll(clParkingType);	
      clParkingTypeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clParkingTypeReposHist.deleteAll(clParkingType);
      clParkingTypeReposHist.flush();
    }
    else {
      clParkingTypeRepos.deleteAll(clParkingType);
      clParkingTypeRepos.flush();
    }
  }

}
