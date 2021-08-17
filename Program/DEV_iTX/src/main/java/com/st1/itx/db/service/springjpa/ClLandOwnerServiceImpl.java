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
import com.st1.itx.db.domain.ClLandOwner;
import com.st1.itx.db.domain.ClLandOwnerId;
import com.st1.itx.db.repository.online.ClLandOwnerRepository;
import com.st1.itx.db.repository.day.ClLandOwnerRepositoryDay;
import com.st1.itx.db.repository.mon.ClLandOwnerRepositoryMon;
import com.st1.itx.db.repository.hist.ClLandOwnerRepositoryHist;
import com.st1.itx.db.service.ClLandOwnerService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clLandOwnerService")
@Repository
public class ClLandOwnerServiceImpl extends ASpringJpaParm implements ClLandOwnerService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private ClLandOwnerRepository clLandOwnerRepos;

  @Autowired
  private ClLandOwnerRepositoryDay clLandOwnerReposDay;

  @Autowired
  private ClLandOwnerRepositoryMon clLandOwnerReposMon;

  @Autowired
  private ClLandOwnerRepositoryHist clLandOwnerReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(clLandOwnerRepos);
    org.junit.Assert.assertNotNull(clLandOwnerReposDay);
    org.junit.Assert.assertNotNull(clLandOwnerReposMon);
    org.junit.Assert.assertNotNull(clLandOwnerReposHist);
  }

  @Override
  public ClLandOwner findById(ClLandOwnerId clLandOwnerId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + clLandOwnerId);
    Optional<ClLandOwner> clLandOwner = null;
    if (dbName.equals(ContentName.onDay))
      clLandOwner = clLandOwnerReposDay.findById(clLandOwnerId);
    else if (dbName.equals(ContentName.onMon))
      clLandOwner = clLandOwnerReposMon.findById(clLandOwnerId);
    else if (dbName.equals(ContentName.onHist))
      clLandOwner = clLandOwnerReposHist.findById(clLandOwnerId);
    else 
      clLandOwner = clLandOwnerRepos.findById(clLandOwnerId);
    ClLandOwner obj = clLandOwner.isPresent() ? clLandOwner.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<ClLandOwner> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClLandOwner> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "LandSeq", "OwnerCustUKey"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = clLandOwnerReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clLandOwnerReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clLandOwnerReposHist.findAll(pageable);
    else 
      slice = clLandOwnerRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClLandOwner> LandSeqEq(int clCode1_0, int clCode2_1, int clNo_2, int landSeq_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClLandOwner> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("LandSeqEq " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2 + " landSeq_3 : " +  landSeq_3);
    if (dbName.equals(ContentName.onDay))
      slice = clLandOwnerReposDay.findAllByClCode1IsAndClCode2IsAndClNoIsAndLandSeqIsOrderByClCode1AscClCode2AscClNoAscLandSeqAsc(clCode1_0, clCode2_1, clNo_2, landSeq_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clLandOwnerReposMon.findAllByClCode1IsAndClCode2IsAndClNoIsAndLandSeqIsOrderByClCode1AscClCode2AscClNoAscLandSeqAsc(clCode1_0, clCode2_1, clNo_2, landSeq_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clLandOwnerReposHist.findAllByClCode1IsAndClCode2IsAndClNoIsAndLandSeqIsOrderByClCode1AscClCode2AscClNoAscLandSeqAsc(clCode1_0, clCode2_1, clNo_2, landSeq_3, pageable);
    else 
      slice = clLandOwnerRepos.findAllByClCode1IsAndClCode2IsAndClNoIsAndLandSeqIsOrderByClCode1AscClCode2AscClNoAscLandSeqAsc(clCode1_0, clCode2_1, clNo_2, landSeq_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClLandOwner> OwnerCustUKeyEq(String ownerCustUKey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClLandOwner> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("OwnerCustUKeyEq " + dbName + " : " + "ownerCustUKey_0 : " + ownerCustUKey_0);
    if (dbName.equals(ContentName.onDay))
      slice = clLandOwnerReposDay.findAllByOwnerCustUKeyIs(ownerCustUKey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clLandOwnerReposMon.findAllByOwnerCustUKeyIs(ownerCustUKey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clLandOwnerReposHist.findAllByOwnerCustUKeyIs(ownerCustUKey_0, pageable);
    else 
      slice = clLandOwnerRepos.findAllByOwnerCustUKeyIs(ownerCustUKey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ClLandOwner holdById(ClLandOwnerId clLandOwnerId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clLandOwnerId);
    Optional<ClLandOwner> clLandOwner = null;
    if (dbName.equals(ContentName.onDay))
      clLandOwner = clLandOwnerReposDay.findByClLandOwnerId(clLandOwnerId);
    else if (dbName.equals(ContentName.onMon))
      clLandOwner = clLandOwnerReposMon.findByClLandOwnerId(clLandOwnerId);
    else if (dbName.equals(ContentName.onHist))
      clLandOwner = clLandOwnerReposHist.findByClLandOwnerId(clLandOwnerId);
    else 
      clLandOwner = clLandOwnerRepos.findByClLandOwnerId(clLandOwnerId);
    return clLandOwner.isPresent() ? clLandOwner.get() : null;
  }

  @Override
  public ClLandOwner holdById(ClLandOwner clLandOwner, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clLandOwner.getClLandOwnerId());
    Optional<ClLandOwner> clLandOwnerT = null;
    if (dbName.equals(ContentName.onDay))
      clLandOwnerT = clLandOwnerReposDay.findByClLandOwnerId(clLandOwner.getClLandOwnerId());
    else if (dbName.equals(ContentName.onMon))
      clLandOwnerT = clLandOwnerReposMon.findByClLandOwnerId(clLandOwner.getClLandOwnerId());
    else if (dbName.equals(ContentName.onHist))
      clLandOwnerT = clLandOwnerReposHist.findByClLandOwnerId(clLandOwner.getClLandOwnerId());
    else 
      clLandOwnerT = clLandOwnerRepos.findByClLandOwnerId(clLandOwner.getClLandOwnerId());
    return clLandOwnerT.isPresent() ? clLandOwnerT.get() : null;
  }

  @Override
  public ClLandOwner insert(ClLandOwner clLandOwner, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + clLandOwner.getClLandOwnerId());
    if (this.findById(clLandOwner.getClLandOwnerId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      clLandOwner.setCreateEmpNo(empNot);

    if(clLandOwner.getLastUpdateEmpNo() == null || clLandOwner.getLastUpdateEmpNo().isEmpty())
      clLandOwner.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clLandOwnerReposDay.saveAndFlush(clLandOwner);	
    else if (dbName.equals(ContentName.onMon))
      return clLandOwnerReposMon.saveAndFlush(clLandOwner);
    else if (dbName.equals(ContentName.onHist))
      return clLandOwnerReposHist.saveAndFlush(clLandOwner);
    else 
    return clLandOwnerRepos.saveAndFlush(clLandOwner);
  }

  @Override
  public ClLandOwner update(ClLandOwner clLandOwner, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + clLandOwner.getClLandOwnerId());
    if (!empNot.isEmpty())
      clLandOwner.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clLandOwnerReposDay.saveAndFlush(clLandOwner);	
    else if (dbName.equals(ContentName.onMon))
      return clLandOwnerReposMon.saveAndFlush(clLandOwner);
    else if (dbName.equals(ContentName.onHist))
      return clLandOwnerReposHist.saveAndFlush(clLandOwner);
    else 
    return clLandOwnerRepos.saveAndFlush(clLandOwner);
  }

  @Override
  public ClLandOwner update2(ClLandOwner clLandOwner, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + clLandOwner.getClLandOwnerId());
    if (!empNot.isEmpty())
      clLandOwner.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      clLandOwnerReposDay.saveAndFlush(clLandOwner);	
    else if (dbName.equals(ContentName.onMon))
      clLandOwnerReposMon.saveAndFlush(clLandOwner);
    else if (dbName.equals(ContentName.onHist))
        clLandOwnerReposHist.saveAndFlush(clLandOwner);
    else 
      clLandOwnerRepos.saveAndFlush(clLandOwner);	
    return this.findById(clLandOwner.getClLandOwnerId());
  }

  @Override
  public void delete(ClLandOwner clLandOwner, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + clLandOwner.getClLandOwnerId());
    if (dbName.equals(ContentName.onDay)) {
      clLandOwnerReposDay.delete(clLandOwner);	
      clLandOwnerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clLandOwnerReposMon.delete(clLandOwner);	
      clLandOwnerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clLandOwnerReposHist.delete(clLandOwner);
      clLandOwnerReposHist.flush();
    }
    else {
      clLandOwnerRepos.delete(clLandOwner);
      clLandOwnerRepos.flush();
    }
   }

  @Override
  public void insertAll(List<ClLandOwner> clLandOwner, TitaVo... titaVo) throws DBException {
    if (clLandOwner == null || clLandOwner.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (ClLandOwner t : clLandOwner){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      clLandOwner = clLandOwnerReposDay.saveAll(clLandOwner);	
      clLandOwnerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clLandOwner = clLandOwnerReposMon.saveAll(clLandOwner);	
      clLandOwnerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clLandOwner = clLandOwnerReposHist.saveAll(clLandOwner);
      clLandOwnerReposHist.flush();
    }
    else {
      clLandOwner = clLandOwnerRepos.saveAll(clLandOwner);
      clLandOwnerRepos.flush();
    }
    }

  @Override
  public void updateAll(List<ClLandOwner> clLandOwner, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (clLandOwner == null || clLandOwner.size() == 0)
      throw new DBException(6);

    for (ClLandOwner t : clLandOwner) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      clLandOwner = clLandOwnerReposDay.saveAll(clLandOwner);	
      clLandOwnerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clLandOwner = clLandOwnerReposMon.saveAll(clLandOwner);	
      clLandOwnerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clLandOwner = clLandOwnerReposHist.saveAll(clLandOwner);
      clLandOwnerReposHist.flush();
    }
    else {
      clLandOwner = clLandOwnerRepos.saveAll(clLandOwner);
      clLandOwnerRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<ClLandOwner> clLandOwner, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (clLandOwner == null || clLandOwner.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      clLandOwnerReposDay.deleteAll(clLandOwner);	
      clLandOwnerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clLandOwnerReposMon.deleteAll(clLandOwner);	
      clLandOwnerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clLandOwnerReposHist.deleteAll(clLandOwner);
      clLandOwnerReposHist.flush();
    }
    else {
      clLandOwnerRepos.deleteAll(clLandOwner);
      clLandOwnerRepos.flush();
    }
  }

}
