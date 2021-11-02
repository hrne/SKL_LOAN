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
import com.st1.itx.db.domain.CustDataCtrl;
import com.st1.itx.db.repository.online.CustDataCtrlRepository;
import com.st1.itx.db.repository.day.CustDataCtrlRepositoryDay;
import com.st1.itx.db.repository.mon.CustDataCtrlRepositoryMon;
import com.st1.itx.db.repository.hist.CustDataCtrlRepositoryHist;
import com.st1.itx.db.service.CustDataCtrlService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("custDataCtrlService")
@Repository
public class CustDataCtrlServiceImpl extends ASpringJpaParm implements CustDataCtrlService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CustDataCtrlRepository custDataCtrlRepos;

  @Autowired
  private CustDataCtrlRepositoryDay custDataCtrlReposDay;

  @Autowired
  private CustDataCtrlRepositoryMon custDataCtrlReposMon;

  @Autowired
  private CustDataCtrlRepositoryHist custDataCtrlReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(custDataCtrlRepos);
    org.junit.Assert.assertNotNull(custDataCtrlReposDay);
    org.junit.Assert.assertNotNull(custDataCtrlReposMon);
    org.junit.Assert.assertNotNull(custDataCtrlReposHist);
  }

  @Override
  public CustDataCtrl findById(int custNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + custNo);
    Optional<CustDataCtrl> custDataCtrl = null;
    if (dbName.equals(ContentName.onDay))
      custDataCtrl = custDataCtrlReposDay.findById(custNo);
    else if (dbName.equals(ContentName.onMon))
      custDataCtrl = custDataCtrlReposMon.findById(custNo);
    else if (dbName.equals(ContentName.onHist))
      custDataCtrl = custDataCtrlReposHist.findById(custNo);
    else 
      custDataCtrl = custDataCtrlRepos.findById(custNo);
    CustDataCtrl obj = custDataCtrl.isPresent() ? custDataCtrl.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CustDataCtrl> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustDataCtrl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = custDataCtrlReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custDataCtrlReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custDataCtrlReposHist.findAll(pageable);
    else 
      slice = custDataCtrlRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CustDataCtrl> findCustNo(int custNo_0, String enable_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustDataCtrl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCustNo " + dbName + " : " + "custNo_0 : " + custNo_0 + " enable_1 : " +  enable_1);
    if (dbName.equals(ContentName.onDay))
      slice = custDataCtrlReposDay.findAllByCustNoIsAndEnableIs(custNo_0, enable_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custDataCtrlReposMon.findAllByCustNoIsAndEnableIs(custNo_0, enable_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custDataCtrlReposHist.findAllByCustNoIsAndEnableIs(custNo_0, enable_1, pageable);
    else 
      slice = custDataCtrlRepos.findAllByCustNoIsAndEnableIs(custNo_0, enable_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CustDataCtrl> findCustUKey(String custUKey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustDataCtrl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCustUKey " + dbName + " : " + "custUKey_0 : " + custUKey_0);
    if (dbName.equals(ContentName.onDay))
      slice = custDataCtrlReposDay.findAllByCustUKeyIs(custUKey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custDataCtrlReposMon.findAllByCustUKeyIs(custUKey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custDataCtrlReposHist.findAllByCustUKeyIs(custUKey_0, pageable);
    else 
      slice = custDataCtrlRepos.findAllByCustUKeyIs(custUKey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CustDataCtrl holdById(int custNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + custNo);
    Optional<CustDataCtrl> custDataCtrl = null;
    if (dbName.equals(ContentName.onDay))
      custDataCtrl = custDataCtrlReposDay.findByCustNo(custNo);
    else if (dbName.equals(ContentName.onMon))
      custDataCtrl = custDataCtrlReposMon.findByCustNo(custNo);
    else if (dbName.equals(ContentName.onHist))
      custDataCtrl = custDataCtrlReposHist.findByCustNo(custNo);
    else 
      custDataCtrl = custDataCtrlRepos.findByCustNo(custNo);
    return custDataCtrl.isPresent() ? custDataCtrl.get() : null;
  }

  @Override
  public CustDataCtrl holdById(CustDataCtrl custDataCtrl, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + custDataCtrl.getCustNo());
    Optional<CustDataCtrl> custDataCtrlT = null;
    if (dbName.equals(ContentName.onDay))
      custDataCtrlT = custDataCtrlReposDay.findByCustNo(custDataCtrl.getCustNo());
    else if (dbName.equals(ContentName.onMon))
      custDataCtrlT = custDataCtrlReposMon.findByCustNo(custDataCtrl.getCustNo());
    else if (dbName.equals(ContentName.onHist))
      custDataCtrlT = custDataCtrlReposHist.findByCustNo(custDataCtrl.getCustNo());
    else 
      custDataCtrlT = custDataCtrlRepos.findByCustNo(custDataCtrl.getCustNo());
    return custDataCtrlT.isPresent() ? custDataCtrlT.get() : null;
  }

  @Override
  public CustDataCtrl insert(CustDataCtrl custDataCtrl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + custDataCtrl.getCustNo());
    if (this.findById(custDataCtrl.getCustNo()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      custDataCtrl.setCreateEmpNo(empNot);

    if(custDataCtrl.getLastUpdateEmpNo() == null || custDataCtrl.getLastUpdateEmpNo().isEmpty())
      custDataCtrl.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return custDataCtrlReposDay.saveAndFlush(custDataCtrl);	
    else if (dbName.equals(ContentName.onMon))
      return custDataCtrlReposMon.saveAndFlush(custDataCtrl);
    else if (dbName.equals(ContentName.onHist))
      return custDataCtrlReposHist.saveAndFlush(custDataCtrl);
    else 
    return custDataCtrlRepos.saveAndFlush(custDataCtrl);
  }

  @Override
  public CustDataCtrl update(CustDataCtrl custDataCtrl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + custDataCtrl.getCustNo());
    if (!empNot.isEmpty())
      custDataCtrl.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return custDataCtrlReposDay.saveAndFlush(custDataCtrl);	
    else if (dbName.equals(ContentName.onMon))
      return custDataCtrlReposMon.saveAndFlush(custDataCtrl);
    else if (dbName.equals(ContentName.onHist))
      return custDataCtrlReposHist.saveAndFlush(custDataCtrl);
    else 
    return custDataCtrlRepos.saveAndFlush(custDataCtrl);
  }

  @Override
  public CustDataCtrl update2(CustDataCtrl custDataCtrl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + custDataCtrl.getCustNo());
    if (!empNot.isEmpty())
      custDataCtrl.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      custDataCtrlReposDay.saveAndFlush(custDataCtrl);	
    else if (dbName.equals(ContentName.onMon))
      custDataCtrlReposMon.saveAndFlush(custDataCtrl);
    else if (dbName.equals(ContentName.onHist))
        custDataCtrlReposHist.saveAndFlush(custDataCtrl);
    else 
      custDataCtrlRepos.saveAndFlush(custDataCtrl);	
    return this.findById(custDataCtrl.getCustNo());
  }

  @Override
  public void delete(CustDataCtrl custDataCtrl, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + custDataCtrl.getCustNo());
    if (dbName.equals(ContentName.onDay)) {
      custDataCtrlReposDay.delete(custDataCtrl);	
      custDataCtrlReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custDataCtrlReposMon.delete(custDataCtrl);	
      custDataCtrlReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custDataCtrlReposHist.delete(custDataCtrl);
      custDataCtrlReposHist.flush();
    }
    else {
      custDataCtrlRepos.delete(custDataCtrl);
      custDataCtrlRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CustDataCtrl> custDataCtrl, TitaVo... titaVo) throws DBException {
    if (custDataCtrl == null || custDataCtrl.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (CustDataCtrl t : custDataCtrl){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      custDataCtrl = custDataCtrlReposDay.saveAll(custDataCtrl);	
      custDataCtrlReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custDataCtrl = custDataCtrlReposMon.saveAll(custDataCtrl);	
      custDataCtrlReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custDataCtrl = custDataCtrlReposHist.saveAll(custDataCtrl);
      custDataCtrlReposHist.flush();
    }
    else {
      custDataCtrl = custDataCtrlRepos.saveAll(custDataCtrl);
      custDataCtrlRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CustDataCtrl> custDataCtrl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (custDataCtrl == null || custDataCtrl.size() == 0)
      throw new DBException(6);

    for (CustDataCtrl t : custDataCtrl) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      custDataCtrl = custDataCtrlReposDay.saveAll(custDataCtrl);	
      custDataCtrlReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custDataCtrl = custDataCtrlReposMon.saveAll(custDataCtrl);	
      custDataCtrlReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custDataCtrl = custDataCtrlReposHist.saveAll(custDataCtrl);
      custDataCtrlReposHist.flush();
    }
    else {
      custDataCtrl = custDataCtrlRepos.saveAll(custDataCtrl);
      custDataCtrlRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CustDataCtrl> custDataCtrl, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (custDataCtrl == null || custDataCtrl.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      custDataCtrlReposDay.deleteAll(custDataCtrl);	
      custDataCtrlReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custDataCtrlReposMon.deleteAll(custDataCtrl);	
      custDataCtrlReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custDataCtrlReposHist.deleteAll(custDataCtrl);
      custDataCtrlReposHist.flush();
    }
    else {
      custDataCtrlRepos.deleteAll(custDataCtrl);
      custDataCtrlRepos.flush();
    }
  }

}
