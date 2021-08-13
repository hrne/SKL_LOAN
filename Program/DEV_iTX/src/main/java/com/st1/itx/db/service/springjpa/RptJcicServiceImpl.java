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
import com.st1.itx.db.domain.RptJcic;
import com.st1.itx.db.domain.RptJcicId;
import com.st1.itx.db.repository.online.RptJcicRepository;
import com.st1.itx.db.repository.day.RptJcicRepositoryDay;
import com.st1.itx.db.repository.mon.RptJcicRepositoryMon;
import com.st1.itx.db.repository.hist.RptJcicRepositoryHist;
import com.st1.itx.db.service.RptJcicService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("rptJcicService")
@Repository
public class RptJcicServiceImpl implements RptJcicService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(RptJcicServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private RptJcicRepository rptJcicRepos;

  @Autowired
  private RptJcicRepositoryDay rptJcicReposDay;

  @Autowired
  private RptJcicRepositoryMon rptJcicReposMon;

  @Autowired
  private RptJcicRepositoryHist rptJcicReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(rptJcicRepos);
    org.junit.Assert.assertNotNull(rptJcicReposDay);
    org.junit.Assert.assertNotNull(rptJcicReposMon);
    org.junit.Assert.assertNotNull(rptJcicReposHist);
  }

  @Override
  public RptJcic findById(RptJcicId rptJcicId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + rptJcicId);
    Optional<RptJcic> rptJcic = null;
    if (dbName.equals(ContentName.onDay))
      rptJcic = rptJcicReposDay.findById(rptJcicId);
    else if (dbName.equals(ContentName.onMon))
      rptJcic = rptJcicReposMon.findById(rptJcicId);
    else if (dbName.equals(ContentName.onHist))
      rptJcic = rptJcicReposHist.findById(rptJcicId);
    else 
      rptJcic = rptJcicRepos.findById(rptJcicId);
    RptJcic obj = rptJcic.isPresent() ? rptJcic.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<RptJcic> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<RptJcic> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "BranchNo", "CustNo", "FacmNo"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = rptJcicReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = rptJcicReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = rptJcicReposHist.findAll(pageable);
    else 
      slice = rptJcicRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public RptJcic holdById(RptJcicId rptJcicId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + rptJcicId);
    Optional<RptJcic> rptJcic = null;
    if (dbName.equals(ContentName.onDay))
      rptJcic = rptJcicReposDay.findByRptJcicId(rptJcicId);
    else if (dbName.equals(ContentName.onMon))
      rptJcic = rptJcicReposMon.findByRptJcicId(rptJcicId);
    else if (dbName.equals(ContentName.onHist))
      rptJcic = rptJcicReposHist.findByRptJcicId(rptJcicId);
    else 
      rptJcic = rptJcicRepos.findByRptJcicId(rptJcicId);
    return rptJcic.isPresent() ? rptJcic.get() : null;
  }

  @Override
  public RptJcic holdById(RptJcic rptJcic, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + rptJcic.getRptJcicId());
    Optional<RptJcic> rptJcicT = null;
    if (dbName.equals(ContentName.onDay))
      rptJcicT = rptJcicReposDay.findByRptJcicId(rptJcic.getRptJcicId());
    else if (dbName.equals(ContentName.onMon))
      rptJcicT = rptJcicReposMon.findByRptJcicId(rptJcic.getRptJcicId());
    else if (dbName.equals(ContentName.onHist))
      rptJcicT = rptJcicReposHist.findByRptJcicId(rptJcic.getRptJcicId());
    else 
      rptJcicT = rptJcicRepos.findByRptJcicId(rptJcic.getRptJcicId());
    return rptJcicT.isPresent() ? rptJcicT.get() : null;
  }

  @Override
  public RptJcic insert(RptJcic rptJcic, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Insert..." + dbName + " " + rptJcic.getRptJcicId());
    if (this.findById(rptJcic.getRptJcicId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      rptJcic.setCreateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return rptJcicReposDay.saveAndFlush(rptJcic);	
    else if (dbName.equals(ContentName.onMon))
      return rptJcicReposMon.saveAndFlush(rptJcic);
    else if (dbName.equals(ContentName.onHist))
      return rptJcicReposHist.saveAndFlush(rptJcic);
    else 
    return rptJcicRepos.saveAndFlush(rptJcic);
  }

  @Override
  public RptJcic update(RptJcic rptJcic, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + rptJcic.getRptJcicId());
    if (!empNot.isEmpty())
      rptJcic.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return rptJcicReposDay.saveAndFlush(rptJcic);	
    else if (dbName.equals(ContentName.onMon))
      return rptJcicReposMon.saveAndFlush(rptJcic);
    else if (dbName.equals(ContentName.onHist))
      return rptJcicReposHist.saveAndFlush(rptJcic);
    else 
    return rptJcicRepos.saveAndFlush(rptJcic);
  }

  @Override
  public RptJcic update2(RptJcic rptJcic, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + rptJcic.getRptJcicId());
    if (!empNot.isEmpty())
      rptJcic.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      rptJcicReposDay.saveAndFlush(rptJcic);	
    else if (dbName.equals(ContentName.onMon))
      rptJcicReposMon.saveAndFlush(rptJcic);
    else if (dbName.equals(ContentName.onHist))
        rptJcicReposHist.saveAndFlush(rptJcic);
    else 
      rptJcicRepos.saveAndFlush(rptJcic);	
    return this.findById(rptJcic.getRptJcicId());
  }

  @Override
  public void delete(RptJcic rptJcic, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + rptJcic.getRptJcicId());
    if (dbName.equals(ContentName.onDay)) {
      rptJcicReposDay.delete(rptJcic);	
      rptJcicReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      rptJcicReposMon.delete(rptJcic);	
      rptJcicReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      rptJcicReposHist.delete(rptJcic);
      rptJcicReposHist.flush();
    }
    else {
      rptJcicRepos.delete(rptJcic);
      rptJcicRepos.flush();
    }
   }

  @Override
  public void insertAll(List<RptJcic> rptJcic, TitaVo... titaVo) throws DBException {
    if (rptJcic == null || rptJcic.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}    logger.info("InsertAll...");
    for (RptJcic t : rptJcic) 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      rptJcic = rptJcicReposDay.saveAll(rptJcic);	
      rptJcicReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      rptJcic = rptJcicReposMon.saveAll(rptJcic);	
      rptJcicReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      rptJcic = rptJcicReposHist.saveAll(rptJcic);
      rptJcicReposHist.flush();
    }
    else {
      rptJcic = rptJcicRepos.saveAll(rptJcic);
      rptJcicRepos.flush();
    }
    }

  @Override
  public void updateAll(List<RptJcic> rptJcic, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (rptJcic == null || rptJcic.size() == 0)
      throw new DBException(6);

    for (RptJcic t : rptJcic) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      rptJcic = rptJcicReposDay.saveAll(rptJcic);	
      rptJcicReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      rptJcic = rptJcicReposMon.saveAll(rptJcic);	
      rptJcicReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      rptJcic = rptJcicReposHist.saveAll(rptJcic);
      rptJcicReposHist.flush();
    }
    else {
      rptJcic = rptJcicRepos.saveAll(rptJcic);
      rptJcicRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<RptJcic> rptJcic, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (rptJcic == null || rptJcic.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      rptJcicReposDay.deleteAll(rptJcic);	
      rptJcicReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      rptJcicReposMon.deleteAll(rptJcic);	
      rptJcicReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      rptJcicReposHist.deleteAll(rptJcic);
      rptJcicReposHist.flush();
    }
    else {
      rptJcicRepos.deleteAll(rptJcic);
      rptJcicRepos.flush();
    }
  }

}
