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
import com.st1.itx.db.domain.FacShareRelation;
import com.st1.itx.db.domain.FacShareRelationId;
import com.st1.itx.db.repository.online.FacShareRelationRepository;
import com.st1.itx.db.repository.day.FacShareRelationRepositoryDay;
import com.st1.itx.db.repository.mon.FacShareRelationRepositoryMon;
import com.st1.itx.db.repository.hist.FacShareRelationRepositoryHist;
import com.st1.itx.db.service.FacShareRelationService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("facShareRelationService")
@Repository
public class FacShareRelationServiceImpl extends ASpringJpaParm implements FacShareRelationService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private FacShareRelationRepository facShareRelationRepos;

  @Autowired
  private FacShareRelationRepositoryDay facShareRelationReposDay;

  @Autowired
  private FacShareRelationRepositoryMon facShareRelationReposMon;

  @Autowired
  private FacShareRelationRepositoryHist facShareRelationReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(facShareRelationRepos);
    org.junit.Assert.assertNotNull(facShareRelationReposDay);
    org.junit.Assert.assertNotNull(facShareRelationReposMon);
    org.junit.Assert.assertNotNull(facShareRelationReposHist);
  }

  @Override
  public FacShareRelation findById(FacShareRelationId facShareRelationId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + facShareRelationId);
    Optional<FacShareRelation> facShareRelation = null;
    if (dbName.equals(ContentName.onDay))
      facShareRelation = facShareRelationReposDay.findById(facShareRelationId);
    else if (dbName.equals(ContentName.onMon))
      facShareRelation = facShareRelationReposMon.findById(facShareRelationId);
    else if (dbName.equals(ContentName.onHist))
      facShareRelation = facShareRelationReposHist.findById(facShareRelationId);
    else 
      facShareRelation = facShareRelationRepos.findById(facShareRelationId);
    FacShareRelation obj = facShareRelation.isPresent() ? facShareRelation.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<FacShareRelation> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacShareRelation> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ApplNo", "RelApplNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ApplNo", "RelApplNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = facShareRelationReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facShareRelationReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facShareRelationReposHist.findAll(pageable);
    else 
      slice = facShareRelationRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<FacShareRelation> ApplNoAll(int applNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacShareRelation> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ApplNoAll " + dbName + " : " + "applNo_0 : " + applNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = facShareRelationReposDay.findAllByApplNoIsOrderByRelApplNoAsc(applNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facShareRelationReposMon.findAllByApplNoIsOrderByRelApplNoAsc(applNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facShareRelationReposHist.findAllByApplNoIsOrderByRelApplNoAsc(applNo_0, pageable);
    else 
      slice = facShareRelationRepos.findAllByApplNoIsOrderByRelApplNoAsc(applNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public FacShareRelation holdById(FacShareRelationId facShareRelationId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + facShareRelationId);
    Optional<FacShareRelation> facShareRelation = null;
    if (dbName.equals(ContentName.onDay))
      facShareRelation = facShareRelationReposDay.findByFacShareRelationId(facShareRelationId);
    else if (dbName.equals(ContentName.onMon))
      facShareRelation = facShareRelationReposMon.findByFacShareRelationId(facShareRelationId);
    else if (dbName.equals(ContentName.onHist))
      facShareRelation = facShareRelationReposHist.findByFacShareRelationId(facShareRelationId);
    else 
      facShareRelation = facShareRelationRepos.findByFacShareRelationId(facShareRelationId);
    return facShareRelation.isPresent() ? facShareRelation.get() : null;
  }

  @Override
  public FacShareRelation holdById(FacShareRelation facShareRelation, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + facShareRelation.getFacShareRelationId());
    Optional<FacShareRelation> facShareRelationT = null;
    if (dbName.equals(ContentName.onDay))
      facShareRelationT = facShareRelationReposDay.findByFacShareRelationId(facShareRelation.getFacShareRelationId());
    else if (dbName.equals(ContentName.onMon))
      facShareRelationT = facShareRelationReposMon.findByFacShareRelationId(facShareRelation.getFacShareRelationId());
    else if (dbName.equals(ContentName.onHist))
      facShareRelationT = facShareRelationReposHist.findByFacShareRelationId(facShareRelation.getFacShareRelationId());
    else 
      facShareRelationT = facShareRelationRepos.findByFacShareRelationId(facShareRelation.getFacShareRelationId());
    return facShareRelationT.isPresent() ? facShareRelationT.get() : null;
  }

  @Override
  public FacShareRelation insert(FacShareRelation facShareRelation, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + facShareRelation.getFacShareRelationId());
    if (this.findById(facShareRelation.getFacShareRelationId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      facShareRelation.setCreateEmpNo(empNot);

    if(facShareRelation.getLastUpdateEmpNo() == null || facShareRelation.getLastUpdateEmpNo().isEmpty())
      facShareRelation.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return facShareRelationReposDay.saveAndFlush(facShareRelation);	
    else if (dbName.equals(ContentName.onMon))
      return facShareRelationReposMon.saveAndFlush(facShareRelation);
    else if (dbName.equals(ContentName.onHist))
      return facShareRelationReposHist.saveAndFlush(facShareRelation);
    else 
    return facShareRelationRepos.saveAndFlush(facShareRelation);
  }

  @Override
  public FacShareRelation update(FacShareRelation facShareRelation, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + facShareRelation.getFacShareRelationId());
    if (!empNot.isEmpty())
      facShareRelation.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return facShareRelationReposDay.saveAndFlush(facShareRelation);	
    else if (dbName.equals(ContentName.onMon))
      return facShareRelationReposMon.saveAndFlush(facShareRelation);
    else if (dbName.equals(ContentName.onHist))
      return facShareRelationReposHist.saveAndFlush(facShareRelation);
    else 
    return facShareRelationRepos.saveAndFlush(facShareRelation);
  }

  @Override
  public FacShareRelation update2(FacShareRelation facShareRelation, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + facShareRelation.getFacShareRelationId());
    if (!empNot.isEmpty())
      facShareRelation.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      facShareRelationReposDay.saveAndFlush(facShareRelation);	
    else if (dbName.equals(ContentName.onMon))
      facShareRelationReposMon.saveAndFlush(facShareRelation);
    else if (dbName.equals(ContentName.onHist))
        facShareRelationReposHist.saveAndFlush(facShareRelation);
    else 
      facShareRelationRepos.saveAndFlush(facShareRelation);	
    return this.findById(facShareRelation.getFacShareRelationId());
  }

  @Override
  public void delete(FacShareRelation facShareRelation, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + facShareRelation.getFacShareRelationId());
    if (dbName.equals(ContentName.onDay)) {
      facShareRelationReposDay.delete(facShareRelation);	
      facShareRelationReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facShareRelationReposMon.delete(facShareRelation);	
      facShareRelationReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facShareRelationReposHist.delete(facShareRelation);
      facShareRelationReposHist.flush();
    }
    else {
      facShareRelationRepos.delete(facShareRelation);
      facShareRelationRepos.flush();
    }
   }

  @Override
  public void insertAll(List<FacShareRelation> facShareRelation, TitaVo... titaVo) throws DBException {
    if (facShareRelation == null || facShareRelation.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (FacShareRelation t : facShareRelation){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      facShareRelation = facShareRelationReposDay.saveAll(facShareRelation);	
      facShareRelationReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facShareRelation = facShareRelationReposMon.saveAll(facShareRelation);	
      facShareRelationReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facShareRelation = facShareRelationReposHist.saveAll(facShareRelation);
      facShareRelationReposHist.flush();
    }
    else {
      facShareRelation = facShareRelationRepos.saveAll(facShareRelation);
      facShareRelationRepos.flush();
    }
    }

  @Override
  public void updateAll(List<FacShareRelation> facShareRelation, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (facShareRelation == null || facShareRelation.size() == 0)
      throw new DBException(6);

    for (FacShareRelation t : facShareRelation) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      facShareRelation = facShareRelationReposDay.saveAll(facShareRelation);	
      facShareRelationReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facShareRelation = facShareRelationReposMon.saveAll(facShareRelation);	
      facShareRelationReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facShareRelation = facShareRelationReposHist.saveAll(facShareRelation);
      facShareRelationReposHist.flush();
    }
    else {
      facShareRelation = facShareRelationRepos.saveAll(facShareRelation);
      facShareRelationRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<FacShareRelation> facShareRelation, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (facShareRelation == null || facShareRelation.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      facShareRelationReposDay.deleteAll(facShareRelation);	
      facShareRelationReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facShareRelationReposMon.deleteAll(facShareRelation);	
      facShareRelationReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facShareRelationReposHist.deleteAll(facShareRelation);
      facShareRelationReposHist.flush();
    }
    else {
      facShareRelationRepos.deleteAll(facShareRelation);
      facShareRelationRepos.flush();
    }
  }

}
