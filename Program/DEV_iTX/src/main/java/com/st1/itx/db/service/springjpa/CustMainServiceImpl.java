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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.repository.online.CustMainRepository;
import com.st1.itx.db.repository.day.CustMainRepositoryDay;
import com.st1.itx.db.repository.mon.CustMainRepositoryMon;
import com.st1.itx.db.repository.hist.CustMainRepositoryHist;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("custMainService")
@Repository
public class CustMainServiceImpl extends ASpringJpaParm implements CustMainService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CustMainRepository custMainRepos;

  @Autowired
  private CustMainRepositoryDay custMainReposDay;

  @Autowired
  private CustMainRepositoryMon custMainReposMon;

  @Autowired
  private CustMainRepositoryHist custMainReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(custMainRepos);
    org.junit.Assert.assertNotNull(custMainReposDay);
    org.junit.Assert.assertNotNull(custMainReposMon);
    org.junit.Assert.assertNotNull(custMainReposHist);
  }

  @Override
  public CustMain findById(String custUKey, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + custUKey);
    Optional<CustMain> custMain = null;
    if (dbName.equals(ContentName.onDay))
      custMain = custMainReposDay.findById(custUKey);
    else if (dbName.equals(ContentName.onMon))
      custMain = custMainReposMon.findById(custUKey);
    else if (dbName.equals(ContentName.onHist))
      custMain = custMainReposHist.findById(custUKey);
    else 
      custMain = custMainRepos.findById(custUKey);
    CustMain obj = custMain.isPresent() ? custMain.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CustMain> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustUKey"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = custMainReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custMainReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custMainReposHist.findAll(pageable);
    else 
      slice = custMainRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CustMain> custIdLike(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custIdLike " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = custMainReposDay.findAllByCustIdLikeOrderByCustIdAsc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custMainReposMon.findAllByCustIdLikeOrderByCustIdAsc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custMainReposHist.findAllByCustIdLikeOrderByCustIdAsc(custId_0, pageable);
    else 
      slice = custMainRepos.findAllByCustIdLikeOrderByCustIdAsc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CustMain custIdFirst(String custId_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("custIdFirst " + dbName + " : " + "custId_0 : " + custId_0);
    Optional<CustMain> custMainT = null;
    if (dbName.equals(ContentName.onDay))
      custMainT = custMainReposDay.findTopByCustIdIs(custId_0);
    else if (dbName.equals(ContentName.onMon))
      custMainT = custMainReposMon.findTopByCustIdIs(custId_0);
    else if (dbName.equals(ContentName.onHist))
      custMainT = custMainReposHist.findTopByCustIdIs(custId_0);
    else 
      custMainT = custMainRepos.findTopByCustIdIs(custId_0);

    return custMainT.isPresent() ? custMainT.get() : null;
  }

  @Override
  public CustMain custNoFirst(int custNo_0, int custNo_1, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("custNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " custNo_1 : " +  custNo_1);
    Optional<CustMain> custMainT = null;
    if (dbName.equals(ContentName.onDay))
      custMainT = custMainReposDay.findTopByCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoDesc(custNo_0, custNo_1);
    else if (dbName.equals(ContentName.onMon))
      custMainT = custMainReposMon.findTopByCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoDesc(custNo_0, custNo_1);
    else if (dbName.equals(ContentName.onHist))
      custMainT = custMainReposHist.findTopByCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoDesc(custNo_0, custNo_1);
    else 
      custMainT = custMainRepos.findTopByCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoDesc(custNo_0, custNo_1);

    return custMainT.isPresent() ? custMainT.get() : null;
  }

  @Override
  public Slice<CustMain> custNoRange(int custNo_0, int custNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custNoRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " custNo_1 : " +  custNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = custMainReposDay.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAsc(custNo_0, custNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custMainReposMon.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAsc(custNo_0, custNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custMainReposHist.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAsc(custNo_0, custNo_1, pageable);
    else 
      slice = custMainRepos.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAsc(custNo_0, custNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CustMain> custNameEq(String custName_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custNameEq " + dbName + " : " + "custName_0 : " + custName_0);
    if (dbName.equals(ContentName.onDay))
      slice = custMainReposDay.findAllByCustNameIs(custName_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custMainReposMon.findAllByCustNameIs(custName_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custMainReposHist.findAllByCustNameIs(custName_0, pageable);
    else 
      slice = custMainRepos.findAllByCustNameIs(custName_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CustMain> custNameLike(String custName_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custNameLike " + dbName + " : " + "custName_0 : " + custName_0);
    if (dbName.equals(ContentName.onDay))
      slice = custMainReposDay.findAllByCustNameLikeOrderByCustNoAsc(custName_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custMainReposMon.findAllByCustNameLikeOrderByCustNoAsc(custName_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custMainReposHist.findAllByCustNameLikeOrderByCustNoAsc(custName_0, pageable);
    else 
      slice = custMainRepos.findAllByCustNameLikeOrderByCustNoAsc(custName_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CustMain> empNoEq(String empNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("empNoEq " + dbName + " : " + "empNo_0 : " + empNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = custMainReposDay.findAllByEmpNoIs(empNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custMainReposMon.findAllByEmpNoIs(empNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custMainReposHist.findAllByEmpNoIs(empNo_0, pageable);
    else 
      slice = custMainRepos.findAllByEmpNoIs(empNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CustMain empNoFirst(String empNo_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("empNoFirst " + dbName + " : " + "empNo_0 : " + empNo_0);
    Optional<CustMain> custMainT = null;
    if (dbName.equals(ContentName.onDay))
      custMainT = custMainReposDay.findTopByEmpNoGreaterThanEqualOrderByEmpNoDesc(empNo_0);
    else if (dbName.equals(ContentName.onMon))
      custMainT = custMainReposMon.findTopByEmpNoGreaterThanEqualOrderByEmpNoDesc(empNo_0);
    else if (dbName.equals(ContentName.onHist))
      custMainT = custMainReposHist.findTopByEmpNoGreaterThanEqualOrderByEmpNoDesc(empNo_0);
    else 
      custMainT = custMainRepos.findTopByEmpNoGreaterThanEqualOrderByEmpNoDesc(empNo_0);

    return custMainT.isPresent() ? custMainT.get() : null;
  }

  @Override
  public CustMain holdById(String custUKey, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + custUKey);
    Optional<CustMain> custMain = null;
    if (dbName.equals(ContentName.onDay))
      custMain = custMainReposDay.findByCustUKey(custUKey);
    else if (dbName.equals(ContentName.onMon))
      custMain = custMainReposMon.findByCustUKey(custUKey);
    else if (dbName.equals(ContentName.onHist))
      custMain = custMainReposHist.findByCustUKey(custUKey);
    else 
      custMain = custMainRepos.findByCustUKey(custUKey);
    return custMain.isPresent() ? custMain.get() : null;
  }

  @Override
  public CustMain holdById(CustMain custMain, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + custMain.getCustUKey());
    Optional<CustMain> custMainT = null;
    if (dbName.equals(ContentName.onDay))
      custMainT = custMainReposDay.findByCustUKey(custMain.getCustUKey());
    else if (dbName.equals(ContentName.onMon))
      custMainT = custMainReposMon.findByCustUKey(custMain.getCustUKey());
    else if (dbName.equals(ContentName.onHist))
      custMainT = custMainReposHist.findByCustUKey(custMain.getCustUKey());
    else 
      custMainT = custMainRepos.findByCustUKey(custMain.getCustUKey());
    return custMainT.isPresent() ? custMainT.get() : null;
  }

  @Override
  public CustMain insert(CustMain custMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + custMain.getCustUKey());
    if (this.findById(custMain.getCustUKey()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      custMain.setCreateEmpNo(empNot);

    if(custMain.getLastUpdateEmpNo() == null || custMain.getLastUpdateEmpNo().isEmpty())
      custMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return custMainReposDay.saveAndFlush(custMain);	
    else if (dbName.equals(ContentName.onMon))
      return custMainReposMon.saveAndFlush(custMain);
    else if (dbName.equals(ContentName.onHist))
      return custMainReposHist.saveAndFlush(custMain);
    else 
    return custMainRepos.saveAndFlush(custMain);
  }

  @Override
  public CustMain update(CustMain custMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + custMain.getCustUKey());
    if (!empNot.isEmpty())
      custMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return custMainReposDay.saveAndFlush(custMain);	
    else if (dbName.equals(ContentName.onMon))
      return custMainReposMon.saveAndFlush(custMain);
    else if (dbName.equals(ContentName.onHist))
      return custMainReposHist.saveAndFlush(custMain);
    else 
    return custMainRepos.saveAndFlush(custMain);
  }

  @Override
  public CustMain update2(CustMain custMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + custMain.getCustUKey());
    if (!empNot.isEmpty())
      custMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      custMainReposDay.saveAndFlush(custMain);	
    else if (dbName.equals(ContentName.onMon))
      custMainReposMon.saveAndFlush(custMain);
    else if (dbName.equals(ContentName.onHist))
        custMainReposHist.saveAndFlush(custMain);
    else 
      custMainRepos.saveAndFlush(custMain);	
    return this.findById(custMain.getCustUKey());
  }

  @Override
  public void delete(CustMain custMain, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + custMain.getCustUKey());
    if (dbName.equals(ContentName.onDay)) {
      custMainReposDay.delete(custMain);	
      custMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custMainReposMon.delete(custMain);	
      custMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custMainReposHist.delete(custMain);
      custMainReposHist.flush();
    }
    else {
      custMainRepos.delete(custMain);
      custMainRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CustMain> custMain, TitaVo... titaVo) throws DBException {
    if (custMain == null || custMain.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (CustMain t : custMain){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      custMain = custMainReposDay.saveAll(custMain);	
      custMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custMain = custMainReposMon.saveAll(custMain);	
      custMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custMain = custMainReposHist.saveAll(custMain);
      custMainReposHist.flush();
    }
    else {
      custMain = custMainRepos.saveAll(custMain);
      custMainRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CustMain> custMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (custMain == null || custMain.size() == 0)
      throw new DBException(6);

    for (CustMain t : custMain) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      custMain = custMainReposDay.saveAll(custMain);	
      custMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custMain = custMainReposMon.saveAll(custMain);	
      custMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custMain = custMainReposHist.saveAll(custMain);
      custMainReposHist.flush();
    }
    else {
      custMain = custMainRepos.saveAll(custMain);
      custMainRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CustMain> custMain, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (custMain == null || custMain.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      custMainReposDay.deleteAll(custMain);	
      custMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custMainReposMon.deleteAll(custMain);	
      custMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custMainReposHist.deleteAll(custMain);
      custMainReposHist.flush();
    }
    else {
      custMainRepos.deleteAll(custMain);
      custMainRepos.flush();
    }
  }

}
