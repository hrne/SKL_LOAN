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
import com.st1.itx.db.domain.CdBranch;
import com.st1.itx.db.repository.online.CdBranchRepository;
import com.st1.itx.db.repository.day.CdBranchRepositoryDay;
import com.st1.itx.db.repository.mon.CdBranchRepositoryMon;
import com.st1.itx.db.repository.hist.CdBranchRepositoryHist;
import com.st1.itx.db.service.CdBranchService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdBranchService")
@Repository
public class CdBranchServiceImpl implements CdBranchService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(CdBranchServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdBranchRepository cdBranchRepos;

  @Autowired
  private CdBranchRepositoryDay cdBranchReposDay;

  @Autowired
  private CdBranchRepositoryMon cdBranchReposMon;

  @Autowired
  private CdBranchRepositoryHist cdBranchReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdBranchRepos);
    org.junit.Assert.assertNotNull(cdBranchReposDay);
    org.junit.Assert.assertNotNull(cdBranchReposMon);
    org.junit.Assert.assertNotNull(cdBranchReposHist);
  }

  @Override
  public CdBranch findById(String branchNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + branchNo);
    Optional<CdBranch> cdBranch = null;
    if (dbName.equals(ContentName.onDay))
      cdBranch = cdBranchReposDay.findById(branchNo);
    else if (dbName.equals(ContentName.onMon))
      cdBranch = cdBranchReposMon.findById(branchNo);
    else if (dbName.equals(ContentName.onHist))
      cdBranch = cdBranchReposHist.findById(branchNo);
    else 
      cdBranch = cdBranchRepos.findById(branchNo);
    CdBranch obj = cdBranch.isPresent() ? cdBranch.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdBranch> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBranch> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "BranchNo"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdBranchReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBranchReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBranchReposHist.findAll(pageable);
    else 
      slice = cdBranchRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdBranch> BranchNoLike(String branchNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBranch> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("BranchNoLike " + dbName + " : " + "branchNo_0 : " + branchNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdBranchReposDay.findAllByBranchNoLikeOrderByBranchNoAsc(branchNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBranchReposMon.findAllByBranchNoLikeOrderByBranchNoAsc(branchNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBranchReposHist.findAllByBranchNoLikeOrderByBranchNoAsc(branchNo_0, pageable);
    else 
      slice = cdBranchRepos.findAllByBranchNoLikeOrderByBranchNoAsc(branchNo_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdBranch holdById(String branchNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + branchNo);
    Optional<CdBranch> cdBranch = null;
    if (dbName.equals(ContentName.onDay))
      cdBranch = cdBranchReposDay.findByBranchNo(branchNo);
    else if (dbName.equals(ContentName.onMon))
      cdBranch = cdBranchReposMon.findByBranchNo(branchNo);
    else if (dbName.equals(ContentName.onHist))
      cdBranch = cdBranchReposHist.findByBranchNo(branchNo);
    else 
      cdBranch = cdBranchRepos.findByBranchNo(branchNo);
    return cdBranch.isPresent() ? cdBranch.get() : null;
  }

  @Override
  public CdBranch holdById(CdBranch cdBranch, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + cdBranch.getBranchNo());
    Optional<CdBranch> cdBranchT = null;
    if (dbName.equals(ContentName.onDay))
      cdBranchT = cdBranchReposDay.findByBranchNo(cdBranch.getBranchNo());
    else if (dbName.equals(ContentName.onMon))
      cdBranchT = cdBranchReposMon.findByBranchNo(cdBranch.getBranchNo());
    else if (dbName.equals(ContentName.onHist))
      cdBranchT = cdBranchReposHist.findByBranchNo(cdBranch.getBranchNo());
    else 
      cdBranchT = cdBranchRepos.findByBranchNo(cdBranch.getBranchNo());
    return cdBranchT.isPresent() ? cdBranchT.get() : null;
  }

  @Override
  public CdBranch insert(CdBranch cdBranch, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + cdBranch.getBranchNo());
    if (this.findById(cdBranch.getBranchNo()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdBranch.setCreateEmpNo(empNot);

    if(cdBranch.getLastUpdateEmpNo() == null || cdBranch.getLastUpdateEmpNo().isEmpty())
      cdBranch.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdBranchReposDay.saveAndFlush(cdBranch);	
    else if (dbName.equals(ContentName.onMon))
      return cdBranchReposMon.saveAndFlush(cdBranch);
    else if (dbName.equals(ContentName.onHist))
      return cdBranchReposHist.saveAndFlush(cdBranch);
    else 
    return cdBranchRepos.saveAndFlush(cdBranch);
  }

  @Override
  public CdBranch update(CdBranch cdBranch, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + cdBranch.getBranchNo());
    if (!empNot.isEmpty())
      cdBranch.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdBranchReposDay.saveAndFlush(cdBranch);	
    else if (dbName.equals(ContentName.onMon))
      return cdBranchReposMon.saveAndFlush(cdBranch);
    else if (dbName.equals(ContentName.onHist))
      return cdBranchReposHist.saveAndFlush(cdBranch);
    else 
    return cdBranchRepos.saveAndFlush(cdBranch);
  }

  @Override
  public CdBranch update2(CdBranch cdBranch, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + cdBranch.getBranchNo());
    if (!empNot.isEmpty())
      cdBranch.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdBranchReposDay.saveAndFlush(cdBranch);	
    else if (dbName.equals(ContentName.onMon))
      cdBranchReposMon.saveAndFlush(cdBranch);
    else if (dbName.equals(ContentName.onHist))
        cdBranchReposHist.saveAndFlush(cdBranch);
    else 
      cdBranchRepos.saveAndFlush(cdBranch);	
    return this.findById(cdBranch.getBranchNo());
  }

  @Override
  public void delete(CdBranch cdBranch, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + cdBranch.getBranchNo());
    if (dbName.equals(ContentName.onDay)) {
      cdBranchReposDay.delete(cdBranch);	
      cdBranchReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBranchReposMon.delete(cdBranch);	
      cdBranchReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBranchReposHist.delete(cdBranch);
      cdBranchReposHist.flush();
    }
    else {
      cdBranchRepos.delete(cdBranch);
      cdBranchRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdBranch> cdBranch, TitaVo... titaVo) throws DBException {
    if (cdBranch == null || cdBranch.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (CdBranch t : cdBranch){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdBranch = cdBranchReposDay.saveAll(cdBranch);	
      cdBranchReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBranch = cdBranchReposMon.saveAll(cdBranch);	
      cdBranchReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBranch = cdBranchReposHist.saveAll(cdBranch);
      cdBranchReposHist.flush();
    }
    else {
      cdBranch = cdBranchRepos.saveAll(cdBranch);
      cdBranchRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdBranch> cdBranch, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (cdBranch == null || cdBranch.size() == 0)
      throw new DBException(6);

    for (CdBranch t : cdBranch) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdBranch = cdBranchReposDay.saveAll(cdBranch);	
      cdBranchReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBranch = cdBranchReposMon.saveAll(cdBranch);	
      cdBranchReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBranch = cdBranchReposHist.saveAll(cdBranch);
      cdBranchReposHist.flush();
    }
    else {
      cdBranch = cdBranchRepos.saveAll(cdBranch);
      cdBranchRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdBranch> cdBranch, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdBranch == null || cdBranch.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdBranchReposDay.deleteAll(cdBranch);	
      cdBranchReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBranchReposMon.deleteAll(cdBranch);	
      cdBranchReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBranchReposHist.deleteAll(cdBranch);
      cdBranchReposHist.flush();
    }
    else {
      cdBranchRepos.deleteAll(cdBranch);
      cdBranchRepos.flush();
    }
  }

}
