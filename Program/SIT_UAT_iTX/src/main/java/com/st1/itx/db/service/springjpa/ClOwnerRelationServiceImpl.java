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
import com.st1.itx.db.domain.ClOwnerRelation;
import com.st1.itx.db.domain.ClOwnerRelationId;
import com.st1.itx.db.repository.online.ClOwnerRelationRepository;
import com.st1.itx.db.repository.day.ClOwnerRelationRepositoryDay;
import com.st1.itx.db.repository.mon.ClOwnerRelationRepositoryMon;
import com.st1.itx.db.repository.hist.ClOwnerRelationRepositoryHist;
import com.st1.itx.db.service.ClOwnerRelationService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clOwnerRelationService")
@Repository
public class ClOwnerRelationServiceImpl extends ASpringJpaParm implements ClOwnerRelationService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private ClOwnerRelationRepository clOwnerRelationRepos;

  @Autowired
  private ClOwnerRelationRepositoryDay clOwnerRelationReposDay;

  @Autowired
  private ClOwnerRelationRepositoryMon clOwnerRelationReposMon;

  @Autowired
  private ClOwnerRelationRepositoryHist clOwnerRelationReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(clOwnerRelationRepos);
    org.junit.Assert.assertNotNull(clOwnerRelationReposDay);
    org.junit.Assert.assertNotNull(clOwnerRelationReposMon);
    org.junit.Assert.assertNotNull(clOwnerRelationReposHist);
  }

  @Override
  public ClOwnerRelation findById(ClOwnerRelationId clOwnerRelationId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + clOwnerRelationId);
    Optional<ClOwnerRelation> clOwnerRelation = null;
    if (dbName.equals(ContentName.onDay))
      clOwnerRelation = clOwnerRelationReposDay.findById(clOwnerRelationId);
    else if (dbName.equals(ContentName.onMon))
      clOwnerRelation = clOwnerRelationReposMon.findById(clOwnerRelationId);
    else if (dbName.equals(ContentName.onHist))
      clOwnerRelation = clOwnerRelationReposHist.findById(clOwnerRelationId);
    else 
      clOwnerRelation = clOwnerRelationRepos.findById(clOwnerRelationId);
    ClOwnerRelation obj = clOwnerRelation.isPresent() ? clOwnerRelation.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<ClOwnerRelation> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClOwnerRelation> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "ApplNo", "OwnerCustUKey"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = clOwnerRelationReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clOwnerRelationReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clOwnerRelationReposHist.findAll(pageable);
    else 
      slice = clOwnerRelationRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClOwnerRelation> ApplNoAll(int clCode1_0, int clCode2_1, int clNo_2, int applNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClOwnerRelation> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ApplNoAll " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2 + " applNo_3 : " +  applNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = clOwnerRelationReposDay.findAllByClCode1IsAndClCode2IsAndClNoIsAndApplNoIsOrderByOwnerCustUKeyAsc(clCode1_0, clCode2_1, clNo_2, applNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clOwnerRelationReposMon.findAllByClCode1IsAndClCode2IsAndClNoIsAndApplNoIsOrderByOwnerCustUKeyAsc(clCode1_0, clCode2_1, clNo_2, applNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clOwnerRelationReposHist.findAllByClCode1IsAndClCode2IsAndClNoIsAndApplNoIsOrderByOwnerCustUKeyAsc(clCode1_0, clCode2_1, clNo_2, applNo_3, pageable);
    else 
      slice = clOwnerRelationRepos.findAllByClCode1IsAndClCode2IsAndClNoIsAndApplNoIsOrderByOwnerCustUKeyAsc(clCode1_0, clCode2_1, clNo_2, applNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ClOwnerRelation holdById(ClOwnerRelationId clOwnerRelationId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clOwnerRelationId);
    Optional<ClOwnerRelation> clOwnerRelation = null;
    if (dbName.equals(ContentName.onDay))
      clOwnerRelation = clOwnerRelationReposDay.findByClOwnerRelationId(clOwnerRelationId);
    else if (dbName.equals(ContentName.onMon))
      clOwnerRelation = clOwnerRelationReposMon.findByClOwnerRelationId(clOwnerRelationId);
    else if (dbName.equals(ContentName.onHist))
      clOwnerRelation = clOwnerRelationReposHist.findByClOwnerRelationId(clOwnerRelationId);
    else 
      clOwnerRelation = clOwnerRelationRepos.findByClOwnerRelationId(clOwnerRelationId);
    return clOwnerRelation.isPresent() ? clOwnerRelation.get() : null;
  }

  @Override
  public ClOwnerRelation holdById(ClOwnerRelation clOwnerRelation, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clOwnerRelation.getClOwnerRelationId());
    Optional<ClOwnerRelation> clOwnerRelationT = null;
    if (dbName.equals(ContentName.onDay))
      clOwnerRelationT = clOwnerRelationReposDay.findByClOwnerRelationId(clOwnerRelation.getClOwnerRelationId());
    else if (dbName.equals(ContentName.onMon))
      clOwnerRelationT = clOwnerRelationReposMon.findByClOwnerRelationId(clOwnerRelation.getClOwnerRelationId());
    else if (dbName.equals(ContentName.onHist))
      clOwnerRelationT = clOwnerRelationReposHist.findByClOwnerRelationId(clOwnerRelation.getClOwnerRelationId());
    else 
      clOwnerRelationT = clOwnerRelationRepos.findByClOwnerRelationId(clOwnerRelation.getClOwnerRelationId());
    return clOwnerRelationT.isPresent() ? clOwnerRelationT.get() : null;
  }

  @Override
  public ClOwnerRelation insert(ClOwnerRelation clOwnerRelation, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + clOwnerRelation.getClOwnerRelationId());
    if (this.findById(clOwnerRelation.getClOwnerRelationId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      clOwnerRelation.setCreateEmpNo(empNot);

    if(clOwnerRelation.getLastUpdateEmpNo() == null || clOwnerRelation.getLastUpdateEmpNo().isEmpty())
      clOwnerRelation.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clOwnerRelationReposDay.saveAndFlush(clOwnerRelation);	
    else if (dbName.equals(ContentName.onMon))
      return clOwnerRelationReposMon.saveAndFlush(clOwnerRelation);
    else if (dbName.equals(ContentName.onHist))
      return clOwnerRelationReposHist.saveAndFlush(clOwnerRelation);
    else 
    return clOwnerRelationRepos.saveAndFlush(clOwnerRelation);
  }

  @Override
  public ClOwnerRelation update(ClOwnerRelation clOwnerRelation, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + clOwnerRelation.getClOwnerRelationId());
    if (!empNot.isEmpty())
      clOwnerRelation.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clOwnerRelationReposDay.saveAndFlush(clOwnerRelation);	
    else if (dbName.equals(ContentName.onMon))
      return clOwnerRelationReposMon.saveAndFlush(clOwnerRelation);
    else if (dbName.equals(ContentName.onHist))
      return clOwnerRelationReposHist.saveAndFlush(clOwnerRelation);
    else 
    return clOwnerRelationRepos.saveAndFlush(clOwnerRelation);
  }

  @Override
  public ClOwnerRelation update2(ClOwnerRelation clOwnerRelation, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + clOwnerRelation.getClOwnerRelationId());
    if (!empNot.isEmpty())
      clOwnerRelation.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      clOwnerRelationReposDay.saveAndFlush(clOwnerRelation);	
    else if (dbName.equals(ContentName.onMon))
      clOwnerRelationReposMon.saveAndFlush(clOwnerRelation);
    else if (dbName.equals(ContentName.onHist))
        clOwnerRelationReposHist.saveAndFlush(clOwnerRelation);
    else 
      clOwnerRelationRepos.saveAndFlush(clOwnerRelation);	
    return this.findById(clOwnerRelation.getClOwnerRelationId());
  }

  @Override
  public void delete(ClOwnerRelation clOwnerRelation, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + clOwnerRelation.getClOwnerRelationId());
    if (dbName.equals(ContentName.onDay)) {
      clOwnerRelationReposDay.delete(clOwnerRelation);	
      clOwnerRelationReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clOwnerRelationReposMon.delete(clOwnerRelation);	
      clOwnerRelationReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clOwnerRelationReposHist.delete(clOwnerRelation);
      clOwnerRelationReposHist.flush();
    }
    else {
      clOwnerRelationRepos.delete(clOwnerRelation);
      clOwnerRelationRepos.flush();
    }
   }

  @Override
  public void insertAll(List<ClOwnerRelation> clOwnerRelation, TitaVo... titaVo) throws DBException {
    if (clOwnerRelation == null || clOwnerRelation.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (ClOwnerRelation t : clOwnerRelation){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      clOwnerRelation = clOwnerRelationReposDay.saveAll(clOwnerRelation);	
      clOwnerRelationReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clOwnerRelation = clOwnerRelationReposMon.saveAll(clOwnerRelation);	
      clOwnerRelationReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clOwnerRelation = clOwnerRelationReposHist.saveAll(clOwnerRelation);
      clOwnerRelationReposHist.flush();
    }
    else {
      clOwnerRelation = clOwnerRelationRepos.saveAll(clOwnerRelation);
      clOwnerRelationRepos.flush();
    }
    }

  @Override
  public void updateAll(List<ClOwnerRelation> clOwnerRelation, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (clOwnerRelation == null || clOwnerRelation.size() == 0)
      throw new DBException(6);

    for (ClOwnerRelation t : clOwnerRelation) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      clOwnerRelation = clOwnerRelationReposDay.saveAll(clOwnerRelation);	
      clOwnerRelationReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clOwnerRelation = clOwnerRelationReposMon.saveAll(clOwnerRelation);	
      clOwnerRelationReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clOwnerRelation = clOwnerRelationReposHist.saveAll(clOwnerRelation);
      clOwnerRelationReposHist.flush();
    }
    else {
      clOwnerRelation = clOwnerRelationRepos.saveAll(clOwnerRelation);
      clOwnerRelationRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<ClOwnerRelation> clOwnerRelation, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (clOwnerRelation == null || clOwnerRelation.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      clOwnerRelationReposDay.deleteAll(clOwnerRelation);	
      clOwnerRelationReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clOwnerRelationReposMon.deleteAll(clOwnerRelation);	
      clOwnerRelationReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clOwnerRelationReposHist.deleteAll(clOwnerRelation);
      clOwnerRelationReposHist.flush();
    }
    else {
      clOwnerRelationRepos.deleteAll(clOwnerRelation);
      clOwnerRelationRepos.flush();
    }
  }

}
