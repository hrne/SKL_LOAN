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
import com.st1.itx.db.domain.RptRelationFamily;
import com.st1.itx.db.domain.RptRelationFamilyId;
import com.st1.itx.db.repository.online.RptRelationFamilyRepository;
import com.st1.itx.db.repository.day.RptRelationFamilyRepositoryDay;
import com.st1.itx.db.repository.mon.RptRelationFamilyRepositoryMon;
import com.st1.itx.db.repository.hist.RptRelationFamilyRepositoryHist;
import com.st1.itx.db.service.RptRelationFamilyService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("rptRelationFamilyService")
@Repository
public class RptRelationFamilyServiceImpl implements RptRelationFamilyService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(RptRelationFamilyServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private RptRelationFamilyRepository rptRelationFamilyRepos;

  @Autowired
  private RptRelationFamilyRepositoryDay rptRelationFamilyReposDay;

  @Autowired
  private RptRelationFamilyRepositoryMon rptRelationFamilyReposMon;

  @Autowired
  private RptRelationFamilyRepositoryHist rptRelationFamilyReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(rptRelationFamilyRepos);
    org.junit.Assert.assertNotNull(rptRelationFamilyReposDay);
    org.junit.Assert.assertNotNull(rptRelationFamilyReposMon);
    org.junit.Assert.assertNotNull(rptRelationFamilyReposHist);
  }

  @Override
  public RptRelationFamily findById(RptRelationFamilyId rptRelationFamilyId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + rptRelationFamilyId);
    Optional<RptRelationFamily> rptRelationFamily = null;
    if (dbName.equals(ContentName.onDay))
      rptRelationFamily = rptRelationFamilyReposDay.findById(rptRelationFamilyId);
    else if (dbName.equals(ContentName.onMon))
      rptRelationFamily = rptRelationFamilyReposMon.findById(rptRelationFamilyId);
    else if (dbName.equals(ContentName.onHist))
      rptRelationFamily = rptRelationFamilyReposHist.findById(rptRelationFamilyId);
    else 
      rptRelationFamily = rptRelationFamilyRepos.findById(rptRelationFamilyId);
    RptRelationFamily obj = rptRelationFamily.isPresent() ? rptRelationFamily.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<RptRelationFamily> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<RptRelationFamily> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CusId", "CusSCD", "RlbID"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = rptRelationFamilyReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = rptRelationFamilyReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = rptRelationFamilyReposHist.findAll(pageable);
    else 
      slice = rptRelationFamilyRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public RptRelationFamily holdById(RptRelationFamilyId rptRelationFamilyId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + rptRelationFamilyId);
    Optional<RptRelationFamily> rptRelationFamily = null;
    if (dbName.equals(ContentName.onDay))
      rptRelationFamily = rptRelationFamilyReposDay.findByRptRelationFamilyId(rptRelationFamilyId);
    else if (dbName.equals(ContentName.onMon))
      rptRelationFamily = rptRelationFamilyReposMon.findByRptRelationFamilyId(rptRelationFamilyId);
    else if (dbName.equals(ContentName.onHist))
      rptRelationFamily = rptRelationFamilyReposHist.findByRptRelationFamilyId(rptRelationFamilyId);
    else 
      rptRelationFamily = rptRelationFamilyRepos.findByRptRelationFamilyId(rptRelationFamilyId);
    return rptRelationFamily.isPresent() ? rptRelationFamily.get() : null;
  }

  @Override
  public RptRelationFamily holdById(RptRelationFamily rptRelationFamily, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + rptRelationFamily.getRptRelationFamilyId());
    Optional<RptRelationFamily> rptRelationFamilyT = null;
    if (dbName.equals(ContentName.onDay))
      rptRelationFamilyT = rptRelationFamilyReposDay.findByRptRelationFamilyId(rptRelationFamily.getRptRelationFamilyId());
    else if (dbName.equals(ContentName.onMon))
      rptRelationFamilyT = rptRelationFamilyReposMon.findByRptRelationFamilyId(rptRelationFamily.getRptRelationFamilyId());
    else if (dbName.equals(ContentName.onHist))
      rptRelationFamilyT = rptRelationFamilyReposHist.findByRptRelationFamilyId(rptRelationFamily.getRptRelationFamilyId());
    else 
      rptRelationFamilyT = rptRelationFamilyRepos.findByRptRelationFamilyId(rptRelationFamily.getRptRelationFamilyId());
    return rptRelationFamilyT.isPresent() ? rptRelationFamilyT.get() : null;
  }

  @Override
  public RptRelationFamily insert(RptRelationFamily rptRelationFamily, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Insert..." + dbName + " " + rptRelationFamily.getRptRelationFamilyId());
    if (this.findById(rptRelationFamily.getRptRelationFamilyId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      rptRelationFamily.setCreateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return rptRelationFamilyReposDay.saveAndFlush(rptRelationFamily);	
    else if (dbName.equals(ContentName.onMon))
      return rptRelationFamilyReposMon.saveAndFlush(rptRelationFamily);
    else if (dbName.equals(ContentName.onHist))
      return rptRelationFamilyReposHist.saveAndFlush(rptRelationFamily);
    else 
    return rptRelationFamilyRepos.saveAndFlush(rptRelationFamily);
  }

  @Override
  public RptRelationFamily update(RptRelationFamily rptRelationFamily, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + rptRelationFamily.getRptRelationFamilyId());
    if (!empNot.isEmpty())
      rptRelationFamily.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return rptRelationFamilyReposDay.saveAndFlush(rptRelationFamily);	
    else if (dbName.equals(ContentName.onMon))
      return rptRelationFamilyReposMon.saveAndFlush(rptRelationFamily);
    else if (dbName.equals(ContentName.onHist))
      return rptRelationFamilyReposHist.saveAndFlush(rptRelationFamily);
    else 
    return rptRelationFamilyRepos.saveAndFlush(rptRelationFamily);
  }

  @Override
  public RptRelationFamily update2(RptRelationFamily rptRelationFamily, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + rptRelationFamily.getRptRelationFamilyId());
    if (!empNot.isEmpty())
      rptRelationFamily.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      rptRelationFamilyReposDay.saveAndFlush(rptRelationFamily);	
    else if (dbName.equals(ContentName.onMon))
      rptRelationFamilyReposMon.saveAndFlush(rptRelationFamily);
    else if (dbName.equals(ContentName.onHist))
        rptRelationFamilyReposHist.saveAndFlush(rptRelationFamily);
    else 
      rptRelationFamilyRepos.saveAndFlush(rptRelationFamily);	
    return this.findById(rptRelationFamily.getRptRelationFamilyId());
  }

  @Override
  public void delete(RptRelationFamily rptRelationFamily, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + rptRelationFamily.getRptRelationFamilyId());
    if (dbName.equals(ContentName.onDay)) {
      rptRelationFamilyReposDay.delete(rptRelationFamily);	
      rptRelationFamilyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      rptRelationFamilyReposMon.delete(rptRelationFamily);	
      rptRelationFamilyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      rptRelationFamilyReposHist.delete(rptRelationFamily);
      rptRelationFamilyReposHist.flush();
    }
    else {
      rptRelationFamilyRepos.delete(rptRelationFamily);
      rptRelationFamilyRepos.flush();
    }
   }

  @Override
  public void insertAll(List<RptRelationFamily> rptRelationFamily, TitaVo... titaVo) throws DBException {
    if (rptRelationFamily == null || rptRelationFamily.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}    logger.info("InsertAll...");
    for (RptRelationFamily t : rptRelationFamily) 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      rptRelationFamily = rptRelationFamilyReposDay.saveAll(rptRelationFamily);	
      rptRelationFamilyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      rptRelationFamily = rptRelationFamilyReposMon.saveAll(rptRelationFamily);	
      rptRelationFamilyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      rptRelationFamily = rptRelationFamilyReposHist.saveAll(rptRelationFamily);
      rptRelationFamilyReposHist.flush();
    }
    else {
      rptRelationFamily = rptRelationFamilyRepos.saveAll(rptRelationFamily);
      rptRelationFamilyRepos.flush();
    }
    }

  @Override
  public void updateAll(List<RptRelationFamily> rptRelationFamily, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (rptRelationFamily == null || rptRelationFamily.size() == 0)
      throw new DBException(6);

    for (RptRelationFamily t : rptRelationFamily) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      rptRelationFamily = rptRelationFamilyReposDay.saveAll(rptRelationFamily);	
      rptRelationFamilyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      rptRelationFamily = rptRelationFamilyReposMon.saveAll(rptRelationFamily);	
      rptRelationFamilyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      rptRelationFamily = rptRelationFamilyReposHist.saveAll(rptRelationFamily);
      rptRelationFamilyReposHist.flush();
    }
    else {
      rptRelationFamily = rptRelationFamilyRepos.saveAll(rptRelationFamily);
      rptRelationFamilyRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<RptRelationFamily> rptRelationFamily, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (rptRelationFamily == null || rptRelationFamily.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      rptRelationFamilyReposDay.deleteAll(rptRelationFamily);	
      rptRelationFamilyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      rptRelationFamilyReposMon.deleteAll(rptRelationFamily);	
      rptRelationFamilyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      rptRelationFamilyReposHist.deleteAll(rptRelationFamily);
      rptRelationFamilyReposHist.flush();
    }
    else {
      rptRelationFamilyRepos.deleteAll(rptRelationFamily);
      rptRelationFamilyRepos.flush();
    }
  }

}
