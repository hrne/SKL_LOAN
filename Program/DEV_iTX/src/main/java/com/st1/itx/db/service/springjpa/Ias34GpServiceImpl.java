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
import com.st1.itx.db.domain.Ias34Gp;
import com.st1.itx.db.domain.Ias34GpId;
import com.st1.itx.db.repository.online.Ias34GpRepository;
import com.st1.itx.db.repository.day.Ias34GpRepositoryDay;
import com.st1.itx.db.repository.mon.Ias34GpRepositoryMon;
import com.st1.itx.db.repository.hist.Ias34GpRepositoryHist;
import com.st1.itx.db.service.Ias34GpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("ias34GpService")
@Repository
public class Ias34GpServiceImpl implements Ias34GpService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(Ias34GpServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private Ias34GpRepository ias34GpRepos;

  @Autowired
  private Ias34GpRepositoryDay ias34GpReposDay;

  @Autowired
  private Ias34GpRepositoryMon ias34GpReposMon;

  @Autowired
  private Ias34GpRepositoryHist ias34GpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(ias34GpRepos);
    org.junit.Assert.assertNotNull(ias34GpReposDay);
    org.junit.Assert.assertNotNull(ias34GpReposMon);
    org.junit.Assert.assertNotNull(ias34GpReposHist);
  }

  @Override
  public Ias34Gp findById(Ias34GpId ias34GpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + ias34GpId);
    Optional<Ias34Gp> ias34Gp = null;
    if (dbName.equals(ContentName.onDay))
      ias34Gp = ias34GpReposDay.findById(ias34GpId);
    else if (dbName.equals(ContentName.onMon))
      ias34Gp = ias34GpReposMon.findById(ias34GpId);
    else if (dbName.equals(ContentName.onHist))
      ias34Gp = ias34GpReposHist.findById(ias34GpId);
    else 
      ias34Gp = ias34GpRepos.findById(ias34GpId);
    Ias34Gp obj = ias34Gp.isPresent() ? ias34Gp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<Ias34Gp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<Ias34Gp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "AgreeNo", "AgreeFg", "FacmNo", "BormNo"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = ias34GpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = ias34GpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = ias34GpReposHist.findAll(pageable);
    else 
      slice = ias34GpRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Ias34Gp holdById(Ias34GpId ias34GpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + ias34GpId);
    Optional<Ias34Gp> ias34Gp = null;
    if (dbName.equals(ContentName.onDay))
      ias34Gp = ias34GpReposDay.findByIas34GpId(ias34GpId);
    else if (dbName.equals(ContentName.onMon))
      ias34Gp = ias34GpReposMon.findByIas34GpId(ias34GpId);
    else if (dbName.equals(ContentName.onHist))
      ias34Gp = ias34GpReposHist.findByIas34GpId(ias34GpId);
    else 
      ias34Gp = ias34GpRepos.findByIas34GpId(ias34GpId);
    return ias34Gp.isPresent() ? ias34Gp.get() : null;
  }

  @Override
  public Ias34Gp holdById(Ias34Gp ias34Gp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + ias34Gp.getIas34GpId());
    Optional<Ias34Gp> ias34GpT = null;
    if (dbName.equals(ContentName.onDay))
      ias34GpT = ias34GpReposDay.findByIas34GpId(ias34Gp.getIas34GpId());
    else if (dbName.equals(ContentName.onMon))
      ias34GpT = ias34GpReposMon.findByIas34GpId(ias34Gp.getIas34GpId());
    else if (dbName.equals(ContentName.onHist))
      ias34GpT = ias34GpReposHist.findByIas34GpId(ias34Gp.getIas34GpId());
    else 
      ias34GpT = ias34GpRepos.findByIas34GpId(ias34Gp.getIas34GpId());
    return ias34GpT.isPresent() ? ias34GpT.get() : null;
  }

  @Override
  public Ias34Gp insert(Ias34Gp ias34Gp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + ias34Gp.getIas34GpId());
    if (this.findById(ias34Gp.getIas34GpId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      ias34Gp.setCreateEmpNo(empNot);

    if(ias34Gp.getLastUpdateEmpNo() == null || ias34Gp.getLastUpdateEmpNo().isEmpty())
      ias34Gp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return ias34GpReposDay.saveAndFlush(ias34Gp);	
    else if (dbName.equals(ContentName.onMon))
      return ias34GpReposMon.saveAndFlush(ias34Gp);
    else if (dbName.equals(ContentName.onHist))
      return ias34GpReposHist.saveAndFlush(ias34Gp);
    else 
    return ias34GpRepos.saveAndFlush(ias34Gp);
  }

  @Override
  public Ias34Gp update(Ias34Gp ias34Gp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + ias34Gp.getIas34GpId());
    if (!empNot.isEmpty())
      ias34Gp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return ias34GpReposDay.saveAndFlush(ias34Gp);	
    else if (dbName.equals(ContentName.onMon))
      return ias34GpReposMon.saveAndFlush(ias34Gp);
    else if (dbName.equals(ContentName.onHist))
      return ias34GpReposHist.saveAndFlush(ias34Gp);
    else 
    return ias34GpRepos.saveAndFlush(ias34Gp);
  }

  @Override
  public Ias34Gp update2(Ias34Gp ias34Gp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + ias34Gp.getIas34GpId());
    if (!empNot.isEmpty())
      ias34Gp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      ias34GpReposDay.saveAndFlush(ias34Gp);	
    else if (dbName.equals(ContentName.onMon))
      ias34GpReposMon.saveAndFlush(ias34Gp);
    else if (dbName.equals(ContentName.onHist))
        ias34GpReposHist.saveAndFlush(ias34Gp);
    else 
      ias34GpRepos.saveAndFlush(ias34Gp);	
    return this.findById(ias34Gp.getIas34GpId());
  }

  @Override
  public void delete(Ias34Gp ias34Gp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + ias34Gp.getIas34GpId());
    if (dbName.equals(ContentName.onDay)) {
      ias34GpReposDay.delete(ias34Gp);	
      ias34GpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias34GpReposMon.delete(ias34Gp);	
      ias34GpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias34GpReposHist.delete(ias34Gp);
      ias34GpReposHist.flush();
    }
    else {
      ias34GpRepos.delete(ias34Gp);
      ias34GpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<Ias34Gp> ias34Gp, TitaVo... titaVo) throws DBException {
    if (ias34Gp == null || ias34Gp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (Ias34Gp t : ias34Gp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      ias34Gp = ias34GpReposDay.saveAll(ias34Gp);	
      ias34GpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias34Gp = ias34GpReposMon.saveAll(ias34Gp);	
      ias34GpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias34Gp = ias34GpReposHist.saveAll(ias34Gp);
      ias34GpReposHist.flush();
    }
    else {
      ias34Gp = ias34GpRepos.saveAll(ias34Gp);
      ias34GpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<Ias34Gp> ias34Gp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (ias34Gp == null || ias34Gp.size() == 0)
      throw new DBException(6);

    for (Ias34Gp t : ias34Gp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      ias34Gp = ias34GpReposDay.saveAll(ias34Gp);	
      ias34GpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias34Gp = ias34GpReposMon.saveAll(ias34Gp);	
      ias34GpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias34Gp = ias34GpReposHist.saveAll(ias34Gp);
      ias34GpReposHist.flush();
    }
    else {
      ias34Gp = ias34GpRepos.saveAll(ias34Gp);
      ias34GpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<Ias34Gp> ias34Gp, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (ias34Gp == null || ias34Gp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      ias34GpReposDay.deleteAll(ias34Gp);	
      ias34GpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias34GpReposMon.deleteAll(ias34Gp);	
      ias34GpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias34GpReposHist.deleteAll(ias34Gp);
      ias34GpReposHist.flush();
    }
    else {
      ias34GpRepos.deleteAll(ias34Gp);
      ias34GpRepos.flush();
    }
  }

  @Override
  public void Usp_L7_Ias34Gp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      ias34GpReposDay.uspL7Ias34gpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      ias34GpReposMon.uspL7Ias34gpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      ias34GpReposHist.uspL7Ias34gpUpd(TBSDYF, EmpNo);
   else
      ias34GpRepos.uspL7Ias34gpUpd(TBSDYF, EmpNo);
  }

}
