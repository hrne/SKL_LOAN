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
import com.st1.itx.db.domain.CdBranchGroup;
import com.st1.itx.db.domain.CdBranchGroupId;
import com.st1.itx.db.repository.online.CdBranchGroupRepository;
import com.st1.itx.db.repository.day.CdBranchGroupRepositoryDay;
import com.st1.itx.db.repository.mon.CdBranchGroupRepositoryMon;
import com.st1.itx.db.repository.hist.CdBranchGroupRepositoryHist;
import com.st1.itx.db.service.CdBranchGroupService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdBranchGroupService")
@Repository
public class CdBranchGroupServiceImpl extends ASpringJpaParm implements CdBranchGroupService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdBranchGroupRepository cdBranchGroupRepos;

  @Autowired
  private CdBranchGroupRepositoryDay cdBranchGroupReposDay;

  @Autowired
  private CdBranchGroupRepositoryMon cdBranchGroupReposMon;

  @Autowired
  private CdBranchGroupRepositoryHist cdBranchGroupReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdBranchGroupRepos);
    org.junit.Assert.assertNotNull(cdBranchGroupReposDay);
    org.junit.Assert.assertNotNull(cdBranchGroupReposMon);
    org.junit.Assert.assertNotNull(cdBranchGroupReposHist);
  }

  @Override
  public CdBranchGroup findById(CdBranchGroupId cdBranchGroupId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + cdBranchGroupId);
    Optional<CdBranchGroup> cdBranchGroup = null;
    if (dbName.equals(ContentName.onDay))
      cdBranchGroup = cdBranchGroupReposDay.findById(cdBranchGroupId);
    else if (dbName.equals(ContentName.onMon))
      cdBranchGroup = cdBranchGroupReposMon.findById(cdBranchGroupId);
    else if (dbName.equals(ContentName.onHist))
      cdBranchGroup = cdBranchGroupReposHist.findById(cdBranchGroupId);
    else 
      cdBranchGroup = cdBranchGroupRepos.findById(cdBranchGroupId);
    CdBranchGroup obj = cdBranchGroup.isPresent() ? cdBranchGroup.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdBranchGroup> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBranchGroup> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "BranchNo", "GroupNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "BranchNo", "GroupNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdBranchGroupReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBranchGroupReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBranchGroupReposHist.findAll(pageable);
    else 
      slice = cdBranchGroupRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdBranchGroup> findByBranchNo(String branchNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBranchGroup> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBranchNo " + dbName + " : " + "branchNo_0 : " + branchNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdBranchGroupReposDay.findAllByBranchNoIsOrderByBranchNoAscGroupNoAsc(branchNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBranchGroupReposMon.findAllByBranchNoIsOrderByBranchNoAscGroupNoAsc(branchNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBranchGroupReposHist.findAllByBranchNoIsOrderByBranchNoAscGroupNoAsc(branchNo_0, pageable);
    else 
      slice = cdBranchGroupRepos.findAllByBranchNoIsOrderByBranchNoAscGroupNoAsc(branchNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdBranchGroup holdById(CdBranchGroupId cdBranchGroupId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdBranchGroupId);
    Optional<CdBranchGroup> cdBranchGroup = null;
    if (dbName.equals(ContentName.onDay))
      cdBranchGroup = cdBranchGroupReposDay.findByCdBranchGroupId(cdBranchGroupId);
    else if (dbName.equals(ContentName.onMon))
      cdBranchGroup = cdBranchGroupReposMon.findByCdBranchGroupId(cdBranchGroupId);
    else if (dbName.equals(ContentName.onHist))
      cdBranchGroup = cdBranchGroupReposHist.findByCdBranchGroupId(cdBranchGroupId);
    else 
      cdBranchGroup = cdBranchGroupRepos.findByCdBranchGroupId(cdBranchGroupId);
    return cdBranchGroup.isPresent() ? cdBranchGroup.get() : null;
  }

  @Override
  public CdBranchGroup holdById(CdBranchGroup cdBranchGroup, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdBranchGroup.getCdBranchGroupId());
    Optional<CdBranchGroup> cdBranchGroupT = null;
    if (dbName.equals(ContentName.onDay))
      cdBranchGroupT = cdBranchGroupReposDay.findByCdBranchGroupId(cdBranchGroup.getCdBranchGroupId());
    else if (dbName.equals(ContentName.onMon))
      cdBranchGroupT = cdBranchGroupReposMon.findByCdBranchGroupId(cdBranchGroup.getCdBranchGroupId());
    else if (dbName.equals(ContentName.onHist))
      cdBranchGroupT = cdBranchGroupReposHist.findByCdBranchGroupId(cdBranchGroup.getCdBranchGroupId());
    else 
      cdBranchGroupT = cdBranchGroupRepos.findByCdBranchGroupId(cdBranchGroup.getCdBranchGroupId());
    return cdBranchGroupT.isPresent() ? cdBranchGroupT.get() : null;
  }

  @Override
  public CdBranchGroup insert(CdBranchGroup cdBranchGroup, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + cdBranchGroup.getCdBranchGroupId());
    if (this.findById(cdBranchGroup.getCdBranchGroupId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdBranchGroup.setCreateEmpNo(empNot);

    if(cdBranchGroup.getLastUpdateEmpNo() == null || cdBranchGroup.getLastUpdateEmpNo().isEmpty())
      cdBranchGroup.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdBranchGroupReposDay.saveAndFlush(cdBranchGroup);	
    else if (dbName.equals(ContentName.onMon))
      return cdBranchGroupReposMon.saveAndFlush(cdBranchGroup);
    else if (dbName.equals(ContentName.onHist))
      return cdBranchGroupReposHist.saveAndFlush(cdBranchGroup);
    else 
    return cdBranchGroupRepos.saveAndFlush(cdBranchGroup);
  }

  @Override
  public CdBranchGroup update(CdBranchGroup cdBranchGroup, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + cdBranchGroup.getCdBranchGroupId());
    if (!empNot.isEmpty())
      cdBranchGroup.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdBranchGroupReposDay.saveAndFlush(cdBranchGroup);	
    else if (dbName.equals(ContentName.onMon))
      return cdBranchGroupReposMon.saveAndFlush(cdBranchGroup);
    else if (dbName.equals(ContentName.onHist))
      return cdBranchGroupReposHist.saveAndFlush(cdBranchGroup);
    else 
    return cdBranchGroupRepos.saveAndFlush(cdBranchGroup);
  }

  @Override
  public CdBranchGroup update2(CdBranchGroup cdBranchGroup, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + cdBranchGroup.getCdBranchGroupId());
    if (!empNot.isEmpty())
      cdBranchGroup.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdBranchGroupReposDay.saveAndFlush(cdBranchGroup);	
    else if (dbName.equals(ContentName.onMon))
      cdBranchGroupReposMon.saveAndFlush(cdBranchGroup);
    else if (dbName.equals(ContentName.onHist))
        cdBranchGroupReposHist.saveAndFlush(cdBranchGroup);
    else 
      cdBranchGroupRepos.saveAndFlush(cdBranchGroup);	
    return this.findById(cdBranchGroup.getCdBranchGroupId());
  }

  @Override
  public void delete(CdBranchGroup cdBranchGroup, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdBranchGroup.getCdBranchGroupId());
    if (dbName.equals(ContentName.onDay)) {
      cdBranchGroupReposDay.delete(cdBranchGroup);	
      cdBranchGroupReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBranchGroupReposMon.delete(cdBranchGroup);	
      cdBranchGroupReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBranchGroupReposHist.delete(cdBranchGroup);
      cdBranchGroupReposHist.flush();
    }
    else {
      cdBranchGroupRepos.delete(cdBranchGroup);
      cdBranchGroupRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdBranchGroup> cdBranchGroup, TitaVo... titaVo) throws DBException {
    if (cdBranchGroup == null || cdBranchGroup.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (CdBranchGroup t : cdBranchGroup){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdBranchGroup = cdBranchGroupReposDay.saveAll(cdBranchGroup);	
      cdBranchGroupReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBranchGroup = cdBranchGroupReposMon.saveAll(cdBranchGroup);	
      cdBranchGroupReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBranchGroup = cdBranchGroupReposHist.saveAll(cdBranchGroup);
      cdBranchGroupReposHist.flush();
    }
    else {
      cdBranchGroup = cdBranchGroupRepos.saveAll(cdBranchGroup);
      cdBranchGroupRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdBranchGroup> cdBranchGroup, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (cdBranchGroup == null || cdBranchGroup.size() == 0)
      throw new DBException(6);

    for (CdBranchGroup t : cdBranchGroup) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdBranchGroup = cdBranchGroupReposDay.saveAll(cdBranchGroup);	
      cdBranchGroupReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBranchGroup = cdBranchGroupReposMon.saveAll(cdBranchGroup);	
      cdBranchGroupReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBranchGroup = cdBranchGroupReposHist.saveAll(cdBranchGroup);
      cdBranchGroupReposHist.flush();
    }
    else {
      cdBranchGroup = cdBranchGroupRepos.saveAll(cdBranchGroup);
      cdBranchGroupRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdBranchGroup> cdBranchGroup, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdBranchGroup == null || cdBranchGroup.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdBranchGroupReposDay.deleteAll(cdBranchGroup);	
      cdBranchGroupReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBranchGroupReposMon.deleteAll(cdBranchGroup);	
      cdBranchGroupReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBranchGroupReposHist.deleteAll(cdBranchGroup);
      cdBranchGroupReposHist.flush();
    }
    else {
      cdBranchGroupRepos.deleteAll(cdBranchGroup);
      cdBranchGroupRepos.flush();
    }
  }

}
