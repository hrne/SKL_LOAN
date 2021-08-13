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
import com.st1.itx.db.domain.CustRelDetail;
import com.st1.itx.db.domain.CustRelDetailId;
import com.st1.itx.db.repository.online.CustRelDetailRepository;
import com.st1.itx.db.repository.day.CustRelDetailRepositoryDay;
import com.st1.itx.db.repository.mon.CustRelDetailRepositoryMon;
import com.st1.itx.db.repository.hist.CustRelDetailRepositoryHist;
import com.st1.itx.db.service.CustRelDetailService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("custRelDetailService")
@Repository
public class CustRelDetailServiceImpl implements CustRelDetailService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(CustRelDetailServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CustRelDetailRepository custRelDetailRepos;

  @Autowired
  private CustRelDetailRepositoryDay custRelDetailReposDay;

  @Autowired
  private CustRelDetailRepositoryMon custRelDetailReposMon;

  @Autowired
  private CustRelDetailRepositoryHist custRelDetailReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(custRelDetailRepos);
    org.junit.Assert.assertNotNull(custRelDetailReposDay);
    org.junit.Assert.assertNotNull(custRelDetailReposMon);
    org.junit.Assert.assertNotNull(custRelDetailReposHist);
  }

  @Override
  public CustRelDetail findById(CustRelDetailId custRelDetailId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + custRelDetailId);
    Optional<CustRelDetail> custRelDetail = null;
    if (dbName.equals(ContentName.onDay))
      custRelDetail = custRelDetailReposDay.findById(custRelDetailId);
    else if (dbName.equals(ContentName.onMon))
      custRelDetail = custRelDetailReposMon.findById(custRelDetailId);
    else if (dbName.equals(ContentName.onHist))
      custRelDetail = custRelDetailReposHist.findById(custRelDetailId);
    else 
      custRelDetail = custRelDetailRepos.findById(custRelDetailId);
    CustRelDetail obj = custRelDetail.isPresent() ? custRelDetail.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CustRelDetail> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustRelDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustRelMainUKey", "Ukey"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = custRelDetailReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custRelDetailReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custRelDetailReposHist.findAll(pageable);
    else 
      slice = custRelDetailRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CustRelDetail> custRelMainUKeyEq(String custRelMainUKey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustRelDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("custRelMainUKeyEq " + dbName + " : " + "custRelMainUKey_0 : " + custRelMainUKey_0);
    if (dbName.equals(ContentName.onDay))
      slice = custRelDetailReposDay.findAllByCustRelMainUKeyIsOrderByCreateDateAsc(custRelMainUKey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custRelDetailReposMon.findAllByCustRelMainUKeyIsOrderByCreateDateAsc(custRelMainUKey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custRelDetailReposHist.findAllByCustRelMainUKeyIsOrderByCreateDateAsc(custRelMainUKey_0, pageable);
    else 
      slice = custRelDetailRepos.findAllByCustRelMainUKeyIsOrderByCreateDateAsc(custRelMainUKey_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CustRelDetail> relIdEq(String relId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustRelDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("relIdEq " + dbName + " : " + "relId_0 : " + relId_0);
    if (dbName.equals(ContentName.onDay))
      slice = custRelDetailReposDay.findAllByRelIdIsOrderByCreateDateAsc(relId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custRelDetailReposMon.findAllByRelIdIsOrderByCreateDateAsc(relId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custRelDetailReposHist.findAllByRelIdIsOrderByCreateDateAsc(relId_0, pageable);
    else 
      slice = custRelDetailRepos.findAllByRelIdIsOrderByCreateDateAsc(relId_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CustRelDetail relIdFirst(String relId_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("relIdFirst " + dbName + " : " + "relId_0 : " + relId_0);
    Optional<CustRelDetail> custRelDetailT = null;
    if (dbName.equals(ContentName.onDay))
      custRelDetailT = custRelDetailReposDay.findTopByRelIdIs(relId_0);
    else if (dbName.equals(ContentName.onMon))
      custRelDetailT = custRelDetailReposMon.findTopByRelIdIs(relId_0);
    else if (dbName.equals(ContentName.onHist))
      custRelDetailT = custRelDetailReposHist.findTopByRelIdIs(relId_0);
    else 
      custRelDetailT = custRelDetailRepos.findTopByRelIdIs(relId_0);
    return custRelDetailT.isPresent() ? custRelDetailT.get() : null;
  }

  @Override
  public CustRelDetail holdById(CustRelDetailId custRelDetailId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + custRelDetailId);
    Optional<CustRelDetail> custRelDetail = null;
    if (dbName.equals(ContentName.onDay))
      custRelDetail = custRelDetailReposDay.findByCustRelDetailId(custRelDetailId);
    else if (dbName.equals(ContentName.onMon))
      custRelDetail = custRelDetailReposMon.findByCustRelDetailId(custRelDetailId);
    else if (dbName.equals(ContentName.onHist))
      custRelDetail = custRelDetailReposHist.findByCustRelDetailId(custRelDetailId);
    else 
      custRelDetail = custRelDetailRepos.findByCustRelDetailId(custRelDetailId);
    return custRelDetail.isPresent() ? custRelDetail.get() : null;
  }

  @Override
  public CustRelDetail holdById(CustRelDetail custRelDetail, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + custRelDetail.getCustRelDetailId());
    Optional<CustRelDetail> custRelDetailT = null;
    if (dbName.equals(ContentName.onDay))
      custRelDetailT = custRelDetailReposDay.findByCustRelDetailId(custRelDetail.getCustRelDetailId());
    else if (dbName.equals(ContentName.onMon))
      custRelDetailT = custRelDetailReposMon.findByCustRelDetailId(custRelDetail.getCustRelDetailId());
    else if (dbName.equals(ContentName.onHist))
      custRelDetailT = custRelDetailReposHist.findByCustRelDetailId(custRelDetail.getCustRelDetailId());
    else 
      custRelDetailT = custRelDetailRepos.findByCustRelDetailId(custRelDetail.getCustRelDetailId());
    return custRelDetailT.isPresent() ? custRelDetailT.get() : null;
  }

  @Override
  public CustRelDetail insert(CustRelDetail custRelDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + custRelDetail.getCustRelDetailId());
    if (this.findById(custRelDetail.getCustRelDetailId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      custRelDetail.setCreateEmpNo(empNot);

    if(custRelDetail.getLastUpdateEmpNo() == null || custRelDetail.getLastUpdateEmpNo().isEmpty())
      custRelDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return custRelDetailReposDay.saveAndFlush(custRelDetail);	
    else if (dbName.equals(ContentName.onMon))
      return custRelDetailReposMon.saveAndFlush(custRelDetail);
    else if (dbName.equals(ContentName.onHist))
      return custRelDetailReposHist.saveAndFlush(custRelDetail);
    else 
    return custRelDetailRepos.saveAndFlush(custRelDetail);
  }

  @Override
  public CustRelDetail update(CustRelDetail custRelDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + custRelDetail.getCustRelDetailId());
    if (!empNot.isEmpty())
      custRelDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return custRelDetailReposDay.saveAndFlush(custRelDetail);	
    else if (dbName.equals(ContentName.onMon))
      return custRelDetailReposMon.saveAndFlush(custRelDetail);
    else if (dbName.equals(ContentName.onHist))
      return custRelDetailReposHist.saveAndFlush(custRelDetail);
    else 
    return custRelDetailRepos.saveAndFlush(custRelDetail);
  }

  @Override
  public CustRelDetail update2(CustRelDetail custRelDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + custRelDetail.getCustRelDetailId());
    if (!empNot.isEmpty())
      custRelDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      custRelDetailReposDay.saveAndFlush(custRelDetail);	
    else if (dbName.equals(ContentName.onMon))
      custRelDetailReposMon.saveAndFlush(custRelDetail);
    else if (dbName.equals(ContentName.onHist))
        custRelDetailReposHist.saveAndFlush(custRelDetail);
    else 
      custRelDetailRepos.saveAndFlush(custRelDetail);	
    return this.findById(custRelDetail.getCustRelDetailId());
  }

  @Override
  public void delete(CustRelDetail custRelDetail, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + custRelDetail.getCustRelDetailId());
    if (dbName.equals(ContentName.onDay)) {
      custRelDetailReposDay.delete(custRelDetail);	
      custRelDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custRelDetailReposMon.delete(custRelDetail);	
      custRelDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custRelDetailReposHist.delete(custRelDetail);
      custRelDetailReposHist.flush();
    }
    else {
      custRelDetailRepos.delete(custRelDetail);
      custRelDetailRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CustRelDetail> custRelDetail, TitaVo... titaVo) throws DBException {
    if (custRelDetail == null || custRelDetail.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (CustRelDetail t : custRelDetail){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      custRelDetail = custRelDetailReposDay.saveAll(custRelDetail);	
      custRelDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custRelDetail = custRelDetailReposMon.saveAll(custRelDetail);	
      custRelDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custRelDetail = custRelDetailReposHist.saveAll(custRelDetail);
      custRelDetailReposHist.flush();
    }
    else {
      custRelDetail = custRelDetailRepos.saveAll(custRelDetail);
      custRelDetailRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CustRelDetail> custRelDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (custRelDetail == null || custRelDetail.size() == 0)
      throw new DBException(6);

    for (CustRelDetail t : custRelDetail) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      custRelDetail = custRelDetailReposDay.saveAll(custRelDetail);	
      custRelDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custRelDetail = custRelDetailReposMon.saveAll(custRelDetail);	
      custRelDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custRelDetail = custRelDetailReposHist.saveAll(custRelDetail);
      custRelDetailReposHist.flush();
    }
    else {
      custRelDetail = custRelDetailRepos.saveAll(custRelDetail);
      custRelDetailRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CustRelDetail> custRelDetail, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (custRelDetail == null || custRelDetail.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      custRelDetailReposDay.deleteAll(custRelDetail);	
      custRelDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custRelDetailReposMon.deleteAll(custRelDetail);	
      custRelDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custRelDetailReposHist.deleteAll(custRelDetail);
      custRelDetailReposHist.flush();
    }
    else {
      custRelDetailRepos.deleteAll(custRelDetail);
      custRelDetailRepos.flush();
    }
  }

}
