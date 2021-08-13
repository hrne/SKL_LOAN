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
import com.st1.itx.db.domain.CustRelMain;
import com.st1.itx.db.repository.online.CustRelMainRepository;
import com.st1.itx.db.repository.day.CustRelMainRepositoryDay;
import com.st1.itx.db.repository.mon.CustRelMainRepositoryMon;
import com.st1.itx.db.repository.hist.CustRelMainRepositoryHist;
import com.st1.itx.db.service.CustRelMainService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("custRelMainService")
@Repository
public class CustRelMainServiceImpl implements CustRelMainService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(CustRelMainServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CustRelMainRepository custRelMainRepos;

  @Autowired
  private CustRelMainRepositoryDay custRelMainReposDay;

  @Autowired
  private CustRelMainRepositoryMon custRelMainReposMon;

  @Autowired
  private CustRelMainRepositoryHist custRelMainReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(custRelMainRepos);
    org.junit.Assert.assertNotNull(custRelMainReposDay);
    org.junit.Assert.assertNotNull(custRelMainReposMon);
    org.junit.Assert.assertNotNull(custRelMainReposHist);
  }

  @Override
  public CustRelMain findById(String ukey, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + ukey);
    Optional<CustRelMain> custRelMain = null;
    if (dbName.equals(ContentName.onDay))
      custRelMain = custRelMainReposDay.findById(ukey);
    else if (dbName.equals(ContentName.onMon))
      custRelMain = custRelMainReposMon.findById(ukey);
    else if (dbName.equals(ContentName.onHist))
      custRelMain = custRelMainReposHist.findById(ukey);
    else 
      custRelMain = custRelMainRepos.findById(ukey);
    CustRelMain obj = custRelMain.isPresent() ? custRelMain.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CustRelMain> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustRelMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = custRelMainReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custRelMainReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custRelMainReposHist.findAll(pageable);
    else 
      slice = custRelMainRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CustRelMain custRelIdFirst(String custRelId_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("custRelIdFirst " + dbName + " : " + "custRelId_0 : " + custRelId_0);
    Optional<CustRelMain> custRelMainT = null;
    if (dbName.equals(ContentName.onDay))
      custRelMainT = custRelMainReposDay.findTopByCustRelIdIs(custRelId_0);
    else if (dbName.equals(ContentName.onMon))
      custRelMainT = custRelMainReposMon.findTopByCustRelIdIs(custRelId_0);
    else if (dbName.equals(ContentName.onHist))
      custRelMainT = custRelMainReposHist.findTopByCustRelIdIs(custRelId_0);
    else 
      custRelMainT = custRelMainRepos.findTopByCustRelIdIs(custRelId_0);
    return custRelMainT.isPresent() ? custRelMainT.get() : null;
  }

  @Override
  public CustRelMain custRelNameFirst(String custRelName_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("custRelNameFirst " + dbName + " : " + "custRelName_0 : " + custRelName_0);
    Optional<CustRelMain> custRelMainT = null;
    if (dbName.equals(ContentName.onDay))
      custRelMainT = custRelMainReposDay.findTopByCustRelNameIs(custRelName_0);
    else if (dbName.equals(ContentName.onMon))
      custRelMainT = custRelMainReposMon.findTopByCustRelNameIs(custRelName_0);
    else if (dbName.equals(ContentName.onHist))
      custRelMainT = custRelMainReposHist.findTopByCustRelNameIs(custRelName_0);
    else 
      custRelMainT = custRelMainRepos.findTopByCustRelNameIs(custRelName_0);
    return custRelMainT.isPresent() ? custRelMainT.get() : null;
  }

  @Override
  public CustRelMain holdById(String ukey, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + ukey);
    Optional<CustRelMain> custRelMain = null;
    if (dbName.equals(ContentName.onDay))
      custRelMain = custRelMainReposDay.findByUkey(ukey);
    else if (dbName.equals(ContentName.onMon))
      custRelMain = custRelMainReposMon.findByUkey(ukey);
    else if (dbName.equals(ContentName.onHist))
      custRelMain = custRelMainReposHist.findByUkey(ukey);
    else 
      custRelMain = custRelMainRepos.findByUkey(ukey);
    return custRelMain.isPresent() ? custRelMain.get() : null;
  }

  @Override
  public CustRelMain holdById(CustRelMain custRelMain, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + custRelMain.getUkey());
    Optional<CustRelMain> custRelMainT = null;
    if (dbName.equals(ContentName.onDay))
      custRelMainT = custRelMainReposDay.findByUkey(custRelMain.getUkey());
    else if (dbName.equals(ContentName.onMon))
      custRelMainT = custRelMainReposMon.findByUkey(custRelMain.getUkey());
    else if (dbName.equals(ContentName.onHist))
      custRelMainT = custRelMainReposHist.findByUkey(custRelMain.getUkey());
    else 
      custRelMainT = custRelMainRepos.findByUkey(custRelMain.getUkey());
    return custRelMainT.isPresent() ? custRelMainT.get() : null;
  }

  @Override
  public CustRelMain insert(CustRelMain custRelMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + custRelMain.getUkey());
    if (this.findById(custRelMain.getUkey()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      custRelMain.setCreateEmpNo(empNot);

    if(custRelMain.getLastUpdateEmpNo() == null || custRelMain.getLastUpdateEmpNo().isEmpty())
      custRelMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return custRelMainReposDay.saveAndFlush(custRelMain);	
    else if (dbName.equals(ContentName.onMon))
      return custRelMainReposMon.saveAndFlush(custRelMain);
    else if (dbName.equals(ContentName.onHist))
      return custRelMainReposHist.saveAndFlush(custRelMain);
    else 
    return custRelMainRepos.saveAndFlush(custRelMain);
  }

  @Override
  public CustRelMain update(CustRelMain custRelMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + custRelMain.getUkey());
    if (!empNot.isEmpty())
      custRelMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return custRelMainReposDay.saveAndFlush(custRelMain);	
    else if (dbName.equals(ContentName.onMon))
      return custRelMainReposMon.saveAndFlush(custRelMain);
    else if (dbName.equals(ContentName.onHist))
      return custRelMainReposHist.saveAndFlush(custRelMain);
    else 
    return custRelMainRepos.saveAndFlush(custRelMain);
  }

  @Override
  public CustRelMain update2(CustRelMain custRelMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + custRelMain.getUkey());
    if (!empNot.isEmpty())
      custRelMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      custRelMainReposDay.saveAndFlush(custRelMain);	
    else if (dbName.equals(ContentName.onMon))
      custRelMainReposMon.saveAndFlush(custRelMain);
    else if (dbName.equals(ContentName.onHist))
        custRelMainReposHist.saveAndFlush(custRelMain);
    else 
      custRelMainRepos.saveAndFlush(custRelMain);	
    return this.findById(custRelMain.getUkey());
  }

  @Override
  public void delete(CustRelMain custRelMain, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + custRelMain.getUkey());
    if (dbName.equals(ContentName.onDay)) {
      custRelMainReposDay.delete(custRelMain);	
      custRelMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custRelMainReposMon.delete(custRelMain);	
      custRelMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custRelMainReposHist.delete(custRelMain);
      custRelMainReposHist.flush();
    }
    else {
      custRelMainRepos.delete(custRelMain);
      custRelMainRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CustRelMain> custRelMain, TitaVo... titaVo) throws DBException {
    if (custRelMain == null || custRelMain.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (CustRelMain t : custRelMain){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      custRelMain = custRelMainReposDay.saveAll(custRelMain);	
      custRelMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custRelMain = custRelMainReposMon.saveAll(custRelMain);	
      custRelMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custRelMain = custRelMainReposHist.saveAll(custRelMain);
      custRelMainReposHist.flush();
    }
    else {
      custRelMain = custRelMainRepos.saveAll(custRelMain);
      custRelMainRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CustRelMain> custRelMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (custRelMain == null || custRelMain.size() == 0)
      throw new DBException(6);

    for (CustRelMain t : custRelMain) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      custRelMain = custRelMainReposDay.saveAll(custRelMain);	
      custRelMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custRelMain = custRelMainReposMon.saveAll(custRelMain);	
      custRelMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custRelMain = custRelMainReposHist.saveAll(custRelMain);
      custRelMainReposHist.flush();
    }
    else {
      custRelMain = custRelMainRepos.saveAll(custRelMain);
      custRelMainRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CustRelMain> custRelMain, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (custRelMain == null || custRelMain.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      custRelMainReposDay.deleteAll(custRelMain);	
      custRelMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custRelMainReposMon.deleteAll(custRelMain);	
      custRelMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custRelMainReposHist.deleteAll(custRelMain);
      custRelMainReposHist.flush();
    }
    else {
      custRelMainRepos.deleteAll(custRelMain);
      custRelMainRepos.flush();
    }
  }

}
