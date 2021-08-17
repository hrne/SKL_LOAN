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
import com.st1.itx.db.domain.ClBuildingOwner;
import com.st1.itx.db.domain.ClBuildingOwnerId;
import com.st1.itx.db.repository.online.ClBuildingOwnerRepository;
import com.st1.itx.db.repository.day.ClBuildingOwnerRepositoryDay;
import com.st1.itx.db.repository.mon.ClBuildingOwnerRepositoryMon;
import com.st1.itx.db.repository.hist.ClBuildingOwnerRepositoryHist;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clBuildingOwnerService")
@Repository
public class ClBuildingOwnerServiceImpl extends ASpringJpaParm implements ClBuildingOwnerService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private ClBuildingOwnerRepository clBuildingOwnerRepos;

  @Autowired
  private ClBuildingOwnerRepositoryDay clBuildingOwnerReposDay;

  @Autowired
  private ClBuildingOwnerRepositoryMon clBuildingOwnerReposMon;

  @Autowired
  private ClBuildingOwnerRepositoryHist clBuildingOwnerReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(clBuildingOwnerRepos);
    org.junit.Assert.assertNotNull(clBuildingOwnerReposDay);
    org.junit.Assert.assertNotNull(clBuildingOwnerReposMon);
    org.junit.Assert.assertNotNull(clBuildingOwnerReposHist);
  }

  @Override
  public ClBuildingOwner findById(ClBuildingOwnerId clBuildingOwnerId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + clBuildingOwnerId);
    Optional<ClBuildingOwner> clBuildingOwner = null;
    if (dbName.equals(ContentName.onDay))
      clBuildingOwner = clBuildingOwnerReposDay.findById(clBuildingOwnerId);
    else if (dbName.equals(ContentName.onMon))
      clBuildingOwner = clBuildingOwnerReposMon.findById(clBuildingOwnerId);
    else if (dbName.equals(ContentName.onHist))
      clBuildingOwner = clBuildingOwnerReposHist.findById(clBuildingOwnerId);
    else 
      clBuildingOwner = clBuildingOwnerRepos.findById(clBuildingOwnerId);
    ClBuildingOwner obj = clBuildingOwner.isPresent() ? clBuildingOwner.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<ClBuildingOwner> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClBuildingOwner> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "OwnerCustUKey"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = clBuildingOwnerReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clBuildingOwnerReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clBuildingOwnerReposHist.findAll(pageable);
    else 
      slice = clBuildingOwnerRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClBuildingOwner> clNoEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClBuildingOwner> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("clNoEq " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = clBuildingOwnerReposDay.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clBuildingOwnerReposMon.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clBuildingOwnerReposHist.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
    else 
      slice = clBuildingOwnerRepos.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClBuildingOwner> OwnerCustUKeyEq(String ownerCustUKey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClBuildingOwner> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("OwnerCustUKeyEq " + dbName + " : " + "ownerCustUKey_0 : " + ownerCustUKey_0);
    if (dbName.equals(ContentName.onDay))
      slice = clBuildingOwnerReposDay.findAllByOwnerCustUKeyIsOrderByClCode1AscClCode2AscClNoAsc(ownerCustUKey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clBuildingOwnerReposMon.findAllByOwnerCustUKeyIsOrderByClCode1AscClCode2AscClNoAsc(ownerCustUKey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clBuildingOwnerReposHist.findAllByOwnerCustUKeyIsOrderByClCode1AscClCode2AscClNoAsc(ownerCustUKey_0, pageable);
    else 
      slice = clBuildingOwnerRepos.findAllByOwnerCustUKeyIsOrderByClCode1AscClCode2AscClNoAsc(ownerCustUKey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ClBuildingOwner clNoFirst(int clCode1_0, int clCode2_1, int clNo_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("clNoFirst " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2);
    Optional<ClBuildingOwner> clBuildingOwnerT = null;
    if (dbName.equals(ContentName.onDay))
      clBuildingOwnerT = clBuildingOwnerReposDay.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByOwnerRelCodeAsc(clCode1_0, clCode2_1, clNo_2);
    else if (dbName.equals(ContentName.onMon))
      clBuildingOwnerT = clBuildingOwnerReposMon.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByOwnerRelCodeAsc(clCode1_0, clCode2_1, clNo_2);
    else if (dbName.equals(ContentName.onHist))
      clBuildingOwnerT = clBuildingOwnerReposHist.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByOwnerRelCodeAsc(clCode1_0, clCode2_1, clNo_2);
    else 
      clBuildingOwnerT = clBuildingOwnerRepos.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByOwnerRelCodeAsc(clCode1_0, clCode2_1, clNo_2);

    return clBuildingOwnerT.isPresent() ? clBuildingOwnerT.get() : null;
  }

  @Override
  public ClBuildingOwner holdById(ClBuildingOwnerId clBuildingOwnerId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clBuildingOwnerId);
    Optional<ClBuildingOwner> clBuildingOwner = null;
    if (dbName.equals(ContentName.onDay))
      clBuildingOwner = clBuildingOwnerReposDay.findByClBuildingOwnerId(clBuildingOwnerId);
    else if (dbName.equals(ContentName.onMon))
      clBuildingOwner = clBuildingOwnerReposMon.findByClBuildingOwnerId(clBuildingOwnerId);
    else if (dbName.equals(ContentName.onHist))
      clBuildingOwner = clBuildingOwnerReposHist.findByClBuildingOwnerId(clBuildingOwnerId);
    else 
      clBuildingOwner = clBuildingOwnerRepos.findByClBuildingOwnerId(clBuildingOwnerId);
    return clBuildingOwner.isPresent() ? clBuildingOwner.get() : null;
  }

  @Override
  public ClBuildingOwner holdById(ClBuildingOwner clBuildingOwner, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clBuildingOwner.getClBuildingOwnerId());
    Optional<ClBuildingOwner> clBuildingOwnerT = null;
    if (dbName.equals(ContentName.onDay))
      clBuildingOwnerT = clBuildingOwnerReposDay.findByClBuildingOwnerId(clBuildingOwner.getClBuildingOwnerId());
    else if (dbName.equals(ContentName.onMon))
      clBuildingOwnerT = clBuildingOwnerReposMon.findByClBuildingOwnerId(clBuildingOwner.getClBuildingOwnerId());
    else if (dbName.equals(ContentName.onHist))
      clBuildingOwnerT = clBuildingOwnerReposHist.findByClBuildingOwnerId(clBuildingOwner.getClBuildingOwnerId());
    else 
      clBuildingOwnerT = clBuildingOwnerRepos.findByClBuildingOwnerId(clBuildingOwner.getClBuildingOwnerId());
    return clBuildingOwnerT.isPresent() ? clBuildingOwnerT.get() : null;
  }

  @Override
  public ClBuildingOwner insert(ClBuildingOwner clBuildingOwner, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + clBuildingOwner.getClBuildingOwnerId());
    if (this.findById(clBuildingOwner.getClBuildingOwnerId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      clBuildingOwner.setCreateEmpNo(empNot);

    if(clBuildingOwner.getLastUpdateEmpNo() == null || clBuildingOwner.getLastUpdateEmpNo().isEmpty())
      clBuildingOwner.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clBuildingOwnerReposDay.saveAndFlush(clBuildingOwner);	
    else if (dbName.equals(ContentName.onMon))
      return clBuildingOwnerReposMon.saveAndFlush(clBuildingOwner);
    else if (dbName.equals(ContentName.onHist))
      return clBuildingOwnerReposHist.saveAndFlush(clBuildingOwner);
    else 
    return clBuildingOwnerRepos.saveAndFlush(clBuildingOwner);
  }

  @Override
  public ClBuildingOwner update(ClBuildingOwner clBuildingOwner, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + clBuildingOwner.getClBuildingOwnerId());
    if (!empNot.isEmpty())
      clBuildingOwner.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clBuildingOwnerReposDay.saveAndFlush(clBuildingOwner);	
    else if (dbName.equals(ContentName.onMon))
      return clBuildingOwnerReposMon.saveAndFlush(clBuildingOwner);
    else if (dbName.equals(ContentName.onHist))
      return clBuildingOwnerReposHist.saveAndFlush(clBuildingOwner);
    else 
    return clBuildingOwnerRepos.saveAndFlush(clBuildingOwner);
  }

  @Override
  public ClBuildingOwner update2(ClBuildingOwner clBuildingOwner, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + clBuildingOwner.getClBuildingOwnerId());
    if (!empNot.isEmpty())
      clBuildingOwner.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      clBuildingOwnerReposDay.saveAndFlush(clBuildingOwner);	
    else if (dbName.equals(ContentName.onMon))
      clBuildingOwnerReposMon.saveAndFlush(clBuildingOwner);
    else if (dbName.equals(ContentName.onHist))
        clBuildingOwnerReposHist.saveAndFlush(clBuildingOwner);
    else 
      clBuildingOwnerRepos.saveAndFlush(clBuildingOwner);	
    return this.findById(clBuildingOwner.getClBuildingOwnerId());
  }

  @Override
  public void delete(ClBuildingOwner clBuildingOwner, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + clBuildingOwner.getClBuildingOwnerId());
    if (dbName.equals(ContentName.onDay)) {
      clBuildingOwnerReposDay.delete(clBuildingOwner);	
      clBuildingOwnerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clBuildingOwnerReposMon.delete(clBuildingOwner);	
      clBuildingOwnerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clBuildingOwnerReposHist.delete(clBuildingOwner);
      clBuildingOwnerReposHist.flush();
    }
    else {
      clBuildingOwnerRepos.delete(clBuildingOwner);
      clBuildingOwnerRepos.flush();
    }
   }

  @Override
  public void insertAll(List<ClBuildingOwner> clBuildingOwner, TitaVo... titaVo) throws DBException {
    if (clBuildingOwner == null || clBuildingOwner.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (ClBuildingOwner t : clBuildingOwner){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      clBuildingOwner = clBuildingOwnerReposDay.saveAll(clBuildingOwner);	
      clBuildingOwnerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clBuildingOwner = clBuildingOwnerReposMon.saveAll(clBuildingOwner);	
      clBuildingOwnerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clBuildingOwner = clBuildingOwnerReposHist.saveAll(clBuildingOwner);
      clBuildingOwnerReposHist.flush();
    }
    else {
      clBuildingOwner = clBuildingOwnerRepos.saveAll(clBuildingOwner);
      clBuildingOwnerRepos.flush();
    }
    }

  @Override
  public void updateAll(List<ClBuildingOwner> clBuildingOwner, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (clBuildingOwner == null || clBuildingOwner.size() == 0)
      throw new DBException(6);

    for (ClBuildingOwner t : clBuildingOwner) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      clBuildingOwner = clBuildingOwnerReposDay.saveAll(clBuildingOwner);	
      clBuildingOwnerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clBuildingOwner = clBuildingOwnerReposMon.saveAll(clBuildingOwner);	
      clBuildingOwnerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clBuildingOwner = clBuildingOwnerReposHist.saveAll(clBuildingOwner);
      clBuildingOwnerReposHist.flush();
    }
    else {
      clBuildingOwner = clBuildingOwnerRepos.saveAll(clBuildingOwner);
      clBuildingOwnerRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<ClBuildingOwner> clBuildingOwner, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (clBuildingOwner == null || clBuildingOwner.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      clBuildingOwnerReposDay.deleteAll(clBuildingOwner);	
      clBuildingOwnerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clBuildingOwnerReposMon.deleteAll(clBuildingOwner);	
      clBuildingOwnerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clBuildingOwnerReposHist.deleteAll(clBuildingOwner);
      clBuildingOwnerReposHist.flush();
    }
    else {
      clBuildingOwnerRepos.deleteAll(clBuildingOwner);
      clBuildingOwnerRepos.flush();
    }
  }

}
