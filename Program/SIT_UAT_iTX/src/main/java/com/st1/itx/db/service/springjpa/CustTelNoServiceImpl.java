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
import com.st1.itx.db.domain.CustTelNo;
import com.st1.itx.db.repository.online.CustTelNoRepository;
import com.st1.itx.db.repository.day.CustTelNoRepositoryDay;
import com.st1.itx.db.repository.mon.CustTelNoRepositoryMon;
import com.st1.itx.db.repository.hist.CustTelNoRepositoryHist;
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("custTelNoService")
@Repository
public class CustTelNoServiceImpl extends ASpringJpaParm implements CustTelNoService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CustTelNoRepository custTelNoRepos;

  @Autowired
  private CustTelNoRepositoryDay custTelNoReposDay;

  @Autowired
  private CustTelNoRepositoryMon custTelNoReposMon;

  @Autowired
  private CustTelNoRepositoryHist custTelNoReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(custTelNoRepos);
    org.junit.Assert.assertNotNull(custTelNoReposDay);
    org.junit.Assert.assertNotNull(custTelNoReposMon);
    org.junit.Assert.assertNotNull(custTelNoReposHist);
  }

  @Override
  public CustTelNo findById(String telNoUKey, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + telNoUKey);
    Optional<CustTelNo> custTelNo = null;
    if (dbName.equals(ContentName.onDay))
      custTelNo = custTelNoReposDay.findById(telNoUKey);
    else if (dbName.equals(ContentName.onMon))
      custTelNo = custTelNoReposMon.findById(telNoUKey);
    else if (dbName.equals(ContentName.onHist))
      custTelNo = custTelNoReposHist.findById(telNoUKey);
    else 
      custTelNo = custTelNoRepos.findById(telNoUKey);
    CustTelNo obj = custTelNo.isPresent() ? custTelNo.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CustTelNo> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustTelNo> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "TelNoUKey"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "TelNoUKey"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = custTelNoReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custTelNoReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custTelNoReposHist.findAll(pageable);
    else 
      slice = custTelNoRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CustTelNo> findCustUKey(String custUKey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustTelNo> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCustUKey " + dbName + " : " + "custUKey_0 : " + custUKey_0);
    if (dbName.equals(ContentName.onDay))
      slice = custTelNoReposDay.findAllByCustUKeyIsOrderByLastUpdateAsc(custUKey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custTelNoReposMon.findAllByCustUKeyIsOrderByLastUpdateAsc(custUKey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custTelNoReposHist.findAllByCustUKeyIsOrderByLastUpdateAsc(custUKey_0, pageable);
    else 
      slice = custTelNoRepos.findAllByCustUKeyIsOrderByLastUpdateAsc(custUKey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CustTelNo custUKeyFirst(String custUKey_0, String telTypeCode_1, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("custUKeyFirst " + dbName + " : " + "custUKey_0 : " + custUKey_0 + " telTypeCode_1 : " +  telTypeCode_1);
    Optional<CustTelNo> custTelNoT = null;
    if (dbName.equals(ContentName.onDay))
      custTelNoT = custTelNoReposDay.findTopByCustUKeyIsAndTelTypeCodeIsOrderByCreateDateAsc(custUKey_0, telTypeCode_1);
    else if (dbName.equals(ContentName.onMon))
      custTelNoT = custTelNoReposMon.findTopByCustUKeyIsAndTelTypeCodeIsOrderByCreateDateAsc(custUKey_0, telTypeCode_1);
    else if (dbName.equals(ContentName.onHist))
      custTelNoT = custTelNoReposHist.findTopByCustUKeyIsAndTelTypeCodeIsOrderByCreateDateAsc(custUKey_0, telTypeCode_1);
    else 
      custTelNoT = custTelNoRepos.findTopByCustUKeyIsAndTelTypeCodeIsOrderByCreateDateAsc(custUKey_0, telTypeCode_1);

    return custTelNoT.isPresent() ? custTelNoT.get() : null;
  }

  @Override
  public Slice<CustTelNo> mobileEq(String telTypeCode_0, String telNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustTelNo> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("mobileEq " + dbName + " : " + "telTypeCode_0 : " + telTypeCode_0 + " telNo_1 : " +  telNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = custTelNoReposDay.findAllByTelTypeCodeIsAndTelNoIsOrderByCustUKeyAscCreateDateAsc(telTypeCode_0, telNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custTelNoReposMon.findAllByTelTypeCodeIsAndTelNoIsOrderByCustUKeyAscCreateDateAsc(telTypeCode_0, telNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custTelNoReposHist.findAllByTelTypeCodeIsAndTelNoIsOrderByCustUKeyAscCreateDateAsc(telTypeCode_0, telNo_1, pageable);
    else 
      slice = custTelNoRepos.findAllByTelTypeCodeIsAndTelNoIsOrderByCustUKeyAscCreateDateAsc(telTypeCode_0, telNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CustTelNo holdById(String telNoUKey, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + telNoUKey);
    Optional<CustTelNo> custTelNo = null;
    if (dbName.equals(ContentName.onDay))
      custTelNo = custTelNoReposDay.findByTelNoUKey(telNoUKey);
    else if (dbName.equals(ContentName.onMon))
      custTelNo = custTelNoReposMon.findByTelNoUKey(telNoUKey);
    else if (dbName.equals(ContentName.onHist))
      custTelNo = custTelNoReposHist.findByTelNoUKey(telNoUKey);
    else 
      custTelNo = custTelNoRepos.findByTelNoUKey(telNoUKey);
    return custTelNo.isPresent() ? custTelNo.get() : null;
  }

  @Override
  public CustTelNo holdById(CustTelNo custTelNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + custTelNo.getTelNoUKey());
    Optional<CustTelNo> custTelNoT = null;
    if (dbName.equals(ContentName.onDay))
      custTelNoT = custTelNoReposDay.findByTelNoUKey(custTelNo.getTelNoUKey());
    else if (dbName.equals(ContentName.onMon))
      custTelNoT = custTelNoReposMon.findByTelNoUKey(custTelNo.getTelNoUKey());
    else if (dbName.equals(ContentName.onHist))
      custTelNoT = custTelNoReposHist.findByTelNoUKey(custTelNo.getTelNoUKey());
    else 
      custTelNoT = custTelNoRepos.findByTelNoUKey(custTelNo.getTelNoUKey());
    return custTelNoT.isPresent() ? custTelNoT.get() : null;
  }

  @Override
  public CustTelNo insert(CustTelNo custTelNo, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + custTelNo.getTelNoUKey());
    if (this.findById(custTelNo.getTelNoUKey()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      custTelNo.setCreateEmpNo(empNot);

    if(custTelNo.getLastUpdateEmpNo() == null || custTelNo.getLastUpdateEmpNo().isEmpty())
      custTelNo.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return custTelNoReposDay.saveAndFlush(custTelNo);	
    else if (dbName.equals(ContentName.onMon))
      return custTelNoReposMon.saveAndFlush(custTelNo);
    else if (dbName.equals(ContentName.onHist))
      return custTelNoReposHist.saveAndFlush(custTelNo);
    else 
    return custTelNoRepos.saveAndFlush(custTelNo);
  }

  @Override
  public CustTelNo update(CustTelNo custTelNo, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + custTelNo.getTelNoUKey());
    if (!empNot.isEmpty())
      custTelNo.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return custTelNoReposDay.saveAndFlush(custTelNo);	
    else if (dbName.equals(ContentName.onMon))
      return custTelNoReposMon.saveAndFlush(custTelNo);
    else if (dbName.equals(ContentName.onHist))
      return custTelNoReposHist.saveAndFlush(custTelNo);
    else 
    return custTelNoRepos.saveAndFlush(custTelNo);
  }

  @Override
  public CustTelNo update2(CustTelNo custTelNo, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + custTelNo.getTelNoUKey());
    if (!empNot.isEmpty())
      custTelNo.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      custTelNoReposDay.saveAndFlush(custTelNo);	
    else if (dbName.equals(ContentName.onMon))
      custTelNoReposMon.saveAndFlush(custTelNo);
    else if (dbName.equals(ContentName.onHist))
        custTelNoReposHist.saveAndFlush(custTelNo);
    else 
      custTelNoRepos.saveAndFlush(custTelNo);	
    return this.findById(custTelNo.getTelNoUKey());
  }

  @Override
  public void delete(CustTelNo custTelNo, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + custTelNo.getTelNoUKey());
    if (dbName.equals(ContentName.onDay)) {
      custTelNoReposDay.delete(custTelNo);	
      custTelNoReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custTelNoReposMon.delete(custTelNo);	
      custTelNoReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custTelNoReposHist.delete(custTelNo);
      custTelNoReposHist.flush();
    }
    else {
      custTelNoRepos.delete(custTelNo);
      custTelNoRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CustTelNo> custTelNo, TitaVo... titaVo) throws DBException {
    if (custTelNo == null || custTelNo.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (CustTelNo t : custTelNo){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      custTelNo = custTelNoReposDay.saveAll(custTelNo);	
      custTelNoReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custTelNo = custTelNoReposMon.saveAll(custTelNo);	
      custTelNoReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custTelNo = custTelNoReposHist.saveAll(custTelNo);
      custTelNoReposHist.flush();
    }
    else {
      custTelNo = custTelNoRepos.saveAll(custTelNo);
      custTelNoRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CustTelNo> custTelNo, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (custTelNo == null || custTelNo.size() == 0)
      throw new DBException(6);

    for (CustTelNo t : custTelNo) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      custTelNo = custTelNoReposDay.saveAll(custTelNo);	
      custTelNoReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custTelNo = custTelNoReposMon.saveAll(custTelNo);	
      custTelNoReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custTelNo = custTelNoReposHist.saveAll(custTelNo);
      custTelNoReposHist.flush();
    }
    else {
      custTelNo = custTelNoRepos.saveAll(custTelNo);
      custTelNoRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CustTelNo> custTelNo, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (custTelNo == null || custTelNo.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      custTelNoReposDay.deleteAll(custTelNo);	
      custTelNoReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custTelNoReposMon.deleteAll(custTelNo);	
      custTelNoReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custTelNoReposHist.deleteAll(custTelNo);
      custTelNoReposHist.flush();
    }
    else {
      custTelNoRepos.deleteAll(custTelNo);
      custTelNoRepos.flush();
    }
  }

}
