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
import com.st1.itx.db.domain.CustRmk;
import com.st1.itx.db.domain.CustRmkId;
import com.st1.itx.db.repository.online.CustRmkRepository;
import com.st1.itx.db.repository.day.CustRmkRepositoryDay;
import com.st1.itx.db.repository.mon.CustRmkRepositoryMon;
import com.st1.itx.db.repository.hist.CustRmkRepositoryHist;
import com.st1.itx.db.service.CustRmkService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("custRmkService")
@Repository
public class CustRmkServiceImpl extends ASpringJpaParm implements CustRmkService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CustRmkRepository custRmkRepos;

  @Autowired
  private CustRmkRepositoryDay custRmkReposDay;

  @Autowired
  private CustRmkRepositoryMon custRmkReposMon;

  @Autowired
  private CustRmkRepositoryHist custRmkReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(custRmkRepos);
    org.junit.Assert.assertNotNull(custRmkReposDay);
    org.junit.Assert.assertNotNull(custRmkReposMon);
    org.junit.Assert.assertNotNull(custRmkReposHist);
  }

  @Override
  public CustRmk findById(CustRmkId custRmkId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + custRmkId);
    Optional<CustRmk> custRmk = null;
    if (dbName.equals(ContentName.onDay))
      custRmk = custRmkReposDay.findById(custRmkId);
    else if (dbName.equals(ContentName.onMon))
      custRmk = custRmkReposMon.findById(custRmkId);
    else if (dbName.equals(ContentName.onHist))
      custRmk = custRmkReposHist.findById(custRmkId);
    else 
      custRmk = custRmkRepos.findById(custRmkId);
    CustRmk obj = custRmk.isPresent() ? custRmk.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CustRmk> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustRmk> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "RmkNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "RmkNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = custRmkReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custRmkReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custRmkReposHist.findAll(pageable);
    else 
      slice = custRmkRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CustRmk> findCustNo(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustRmk> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCustNo " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = custRmkReposDay.findAllByCustNoIs(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custRmkReposMon.findAllByCustNoIs(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custRmkReposHist.findAllByCustNoIs(custNo_0, pageable);
    else 
      slice = custRmkRepos.findAllByCustNoIs(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CustRmk> findRmkCode(String rmkCode_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustRmk> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findRmkCode " + dbName + " : " + "rmkCode_0 : " + rmkCode_0);
    if (dbName.equals(ContentName.onDay))
      slice = custRmkReposDay.findAllByRmkCodeIs(rmkCode_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custRmkReposMon.findAllByRmkCodeIs(rmkCode_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custRmkReposHist.findAllByRmkCodeIs(rmkCode_0, pageable);
    else 
      slice = custRmkRepos.findAllByRmkCodeIs(rmkCode_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CustRmk maxRmkNoFirst(int custNo_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("maxRmkNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0);
    Optional<CustRmk> custRmkT = null;
    if (dbName.equals(ContentName.onDay))
      custRmkT = custRmkReposDay.findTopByCustNoIsOrderByRmkNoDesc(custNo_0);
    else if (dbName.equals(ContentName.onMon))
      custRmkT = custRmkReposMon.findTopByCustNoIsOrderByRmkNoDesc(custNo_0);
    else if (dbName.equals(ContentName.onHist))
      custRmkT = custRmkReposHist.findTopByCustNoIsOrderByRmkNoDesc(custNo_0);
    else 
      custRmkT = custRmkRepos.findTopByCustNoIsOrderByRmkNoDesc(custNo_0);

    return custRmkT.isPresent() ? custRmkT.get() : null;
  }

  @Override
  public CustRmk holdById(CustRmkId custRmkId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + custRmkId);
    Optional<CustRmk> custRmk = null;
    if (dbName.equals(ContentName.onDay))
      custRmk = custRmkReposDay.findByCustRmkId(custRmkId);
    else if (dbName.equals(ContentName.onMon))
      custRmk = custRmkReposMon.findByCustRmkId(custRmkId);
    else if (dbName.equals(ContentName.onHist))
      custRmk = custRmkReposHist.findByCustRmkId(custRmkId);
    else 
      custRmk = custRmkRepos.findByCustRmkId(custRmkId);
    return custRmk.isPresent() ? custRmk.get() : null;
  }

  @Override
  public CustRmk holdById(CustRmk custRmk, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + custRmk.getCustRmkId());
    Optional<CustRmk> custRmkT = null;
    if (dbName.equals(ContentName.onDay))
      custRmkT = custRmkReposDay.findByCustRmkId(custRmk.getCustRmkId());
    else if (dbName.equals(ContentName.onMon))
      custRmkT = custRmkReposMon.findByCustRmkId(custRmk.getCustRmkId());
    else if (dbName.equals(ContentName.onHist))
      custRmkT = custRmkReposHist.findByCustRmkId(custRmk.getCustRmkId());
    else 
      custRmkT = custRmkRepos.findByCustRmkId(custRmk.getCustRmkId());
    return custRmkT.isPresent() ? custRmkT.get() : null;
  }

  @Override
  public CustRmk insert(CustRmk custRmk, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + custRmk.getCustRmkId());
    if (this.findById(custRmk.getCustRmkId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      custRmk.setCreateEmpNo(empNot);

    if(custRmk.getLastUpdateEmpNo() == null || custRmk.getLastUpdateEmpNo().isEmpty())
      custRmk.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return custRmkReposDay.saveAndFlush(custRmk);	
    else if (dbName.equals(ContentName.onMon))
      return custRmkReposMon.saveAndFlush(custRmk);
    else if (dbName.equals(ContentName.onHist))
      return custRmkReposHist.saveAndFlush(custRmk);
    else 
    return custRmkRepos.saveAndFlush(custRmk);
  }

  @Override
  public CustRmk update(CustRmk custRmk, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + custRmk.getCustRmkId());
    if (!empNot.isEmpty())
      custRmk.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return custRmkReposDay.saveAndFlush(custRmk);	
    else if (dbName.equals(ContentName.onMon))
      return custRmkReposMon.saveAndFlush(custRmk);
    else if (dbName.equals(ContentName.onHist))
      return custRmkReposHist.saveAndFlush(custRmk);
    else 
    return custRmkRepos.saveAndFlush(custRmk);
  }

  @Override
  public CustRmk update2(CustRmk custRmk, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + custRmk.getCustRmkId());
    if (!empNot.isEmpty())
      custRmk.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      custRmkReposDay.saveAndFlush(custRmk);	
    else if (dbName.equals(ContentName.onMon))
      custRmkReposMon.saveAndFlush(custRmk);
    else if (dbName.equals(ContentName.onHist))
        custRmkReposHist.saveAndFlush(custRmk);
    else 
      custRmkRepos.saveAndFlush(custRmk);	
    return this.findById(custRmk.getCustRmkId());
  }

  @Override
  public void delete(CustRmk custRmk, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + custRmk.getCustRmkId());
    if (dbName.equals(ContentName.onDay)) {
      custRmkReposDay.delete(custRmk);	
      custRmkReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custRmkReposMon.delete(custRmk);	
      custRmkReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custRmkReposHist.delete(custRmk);
      custRmkReposHist.flush();
    }
    else {
      custRmkRepos.delete(custRmk);
      custRmkRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CustRmk> custRmk, TitaVo... titaVo) throws DBException {
    if (custRmk == null || custRmk.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (CustRmk t : custRmk){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      custRmk = custRmkReposDay.saveAll(custRmk);	
      custRmkReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custRmk = custRmkReposMon.saveAll(custRmk);	
      custRmkReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custRmk = custRmkReposHist.saveAll(custRmk);
      custRmkReposHist.flush();
    }
    else {
      custRmk = custRmkRepos.saveAll(custRmk);
      custRmkRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CustRmk> custRmk, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (custRmk == null || custRmk.size() == 0)
      throw new DBException(6);

    for (CustRmk t : custRmk) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      custRmk = custRmkReposDay.saveAll(custRmk);	
      custRmkReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custRmk = custRmkReposMon.saveAll(custRmk);	
      custRmkReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custRmk = custRmkReposHist.saveAll(custRmk);
      custRmkReposHist.flush();
    }
    else {
      custRmk = custRmkRepos.saveAll(custRmk);
      custRmkRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CustRmk> custRmk, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (custRmk == null || custRmk.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      custRmkReposDay.deleteAll(custRmk);	
      custRmkReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custRmkReposMon.deleteAll(custRmk);	
      custRmkReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custRmkReposHist.deleteAll(custRmk);
      custRmkReposHist.flush();
    }
    else {
      custRmkRepos.deleteAll(custRmk);
      custRmkRepos.flush();
    }
  }

}
