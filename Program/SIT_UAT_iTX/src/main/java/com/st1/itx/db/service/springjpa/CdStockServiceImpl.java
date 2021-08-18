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
import com.st1.itx.db.domain.CdStock;
import com.st1.itx.db.repository.online.CdStockRepository;
import com.st1.itx.db.repository.day.CdStockRepositoryDay;
import com.st1.itx.db.repository.mon.CdStockRepositoryMon;
import com.st1.itx.db.repository.hist.CdStockRepositoryHist;
import com.st1.itx.db.service.CdStockService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdStockService")
@Repository
public class CdStockServiceImpl extends ASpringJpaParm implements CdStockService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdStockRepository cdStockRepos;

  @Autowired
  private CdStockRepositoryDay cdStockReposDay;

  @Autowired
  private CdStockRepositoryMon cdStockReposMon;

  @Autowired
  private CdStockRepositoryHist cdStockReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdStockRepos);
    org.junit.Assert.assertNotNull(cdStockReposDay);
    org.junit.Assert.assertNotNull(cdStockReposMon);
    org.junit.Assert.assertNotNull(cdStockReposHist);
  }

  @Override
  public CdStock findById(String stockCode, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + stockCode);
    Optional<CdStock> cdStock = null;
    if (dbName.equals(ContentName.onDay))
      cdStock = cdStockReposDay.findById(stockCode);
    else if (dbName.equals(ContentName.onMon))
      cdStock = cdStockReposMon.findById(stockCode);
    else if (dbName.equals(ContentName.onHist))
      cdStock = cdStockReposHist.findById(stockCode);
    else 
      cdStock = cdStockRepos.findById(stockCode);
    CdStock obj = cdStock.isPresent() ? cdStock.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdStock> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdStock> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "StockCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "StockCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdStockReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdStockReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdStockReposHist.findAll(pageable);
    else 
      slice = cdStockRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdStock holdById(String stockCode, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + stockCode);
    Optional<CdStock> cdStock = null;
    if (dbName.equals(ContentName.onDay))
      cdStock = cdStockReposDay.findByStockCode(stockCode);
    else if (dbName.equals(ContentName.onMon))
      cdStock = cdStockReposMon.findByStockCode(stockCode);
    else if (dbName.equals(ContentName.onHist))
      cdStock = cdStockReposHist.findByStockCode(stockCode);
    else 
      cdStock = cdStockRepos.findByStockCode(stockCode);
    return cdStock.isPresent() ? cdStock.get() : null;
  }

  @Override
  public CdStock holdById(CdStock cdStock, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdStock.getStockCode());
    Optional<CdStock> cdStockT = null;
    if (dbName.equals(ContentName.onDay))
      cdStockT = cdStockReposDay.findByStockCode(cdStock.getStockCode());
    else if (dbName.equals(ContentName.onMon))
      cdStockT = cdStockReposMon.findByStockCode(cdStock.getStockCode());
    else if (dbName.equals(ContentName.onHist))
      cdStockT = cdStockReposHist.findByStockCode(cdStock.getStockCode());
    else 
      cdStockT = cdStockRepos.findByStockCode(cdStock.getStockCode());
    return cdStockT.isPresent() ? cdStockT.get() : null;
  }

  @Override
  public CdStock insert(CdStock cdStock, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + cdStock.getStockCode());
    if (this.findById(cdStock.getStockCode()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdStock.setCreateEmpNo(empNot);

    if(cdStock.getLastUpdateEmpNo() == null || cdStock.getLastUpdateEmpNo().isEmpty())
      cdStock.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdStockReposDay.saveAndFlush(cdStock);	
    else if (dbName.equals(ContentName.onMon))
      return cdStockReposMon.saveAndFlush(cdStock);
    else if (dbName.equals(ContentName.onHist))
      return cdStockReposHist.saveAndFlush(cdStock);
    else 
    return cdStockRepos.saveAndFlush(cdStock);
  }

  @Override
  public CdStock update(CdStock cdStock, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + cdStock.getStockCode());
    if (!empNot.isEmpty())
      cdStock.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdStockReposDay.saveAndFlush(cdStock);	
    else if (dbName.equals(ContentName.onMon))
      return cdStockReposMon.saveAndFlush(cdStock);
    else if (dbName.equals(ContentName.onHist))
      return cdStockReposHist.saveAndFlush(cdStock);
    else 
    return cdStockRepos.saveAndFlush(cdStock);
  }

  @Override
  public CdStock update2(CdStock cdStock, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + cdStock.getStockCode());
    if (!empNot.isEmpty())
      cdStock.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdStockReposDay.saveAndFlush(cdStock);	
    else if (dbName.equals(ContentName.onMon))
      cdStockReposMon.saveAndFlush(cdStock);
    else if (dbName.equals(ContentName.onHist))
        cdStockReposHist.saveAndFlush(cdStock);
    else 
      cdStockRepos.saveAndFlush(cdStock);	
    return this.findById(cdStock.getStockCode());
  }

  @Override
  public void delete(CdStock cdStock, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdStock.getStockCode());
    if (dbName.equals(ContentName.onDay)) {
      cdStockReposDay.delete(cdStock);	
      cdStockReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdStockReposMon.delete(cdStock);	
      cdStockReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdStockReposHist.delete(cdStock);
      cdStockReposHist.flush();
    }
    else {
      cdStockRepos.delete(cdStock);
      cdStockRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdStock> cdStock, TitaVo... titaVo) throws DBException {
    if (cdStock == null || cdStock.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (CdStock t : cdStock){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdStock = cdStockReposDay.saveAll(cdStock);	
      cdStockReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdStock = cdStockReposMon.saveAll(cdStock);	
      cdStockReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdStock = cdStockReposHist.saveAll(cdStock);
      cdStockReposHist.flush();
    }
    else {
      cdStock = cdStockRepos.saveAll(cdStock);
      cdStockRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdStock> cdStock, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (cdStock == null || cdStock.size() == 0)
      throw new DBException(6);

    for (CdStock t : cdStock) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdStock = cdStockReposDay.saveAll(cdStock);	
      cdStockReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdStock = cdStockReposMon.saveAll(cdStock);	
      cdStockReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdStock = cdStockReposHist.saveAll(cdStock);
      cdStockReposHist.flush();
    }
    else {
      cdStock = cdStockRepos.saveAll(cdStock);
      cdStockRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdStock> cdStock, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdStock == null || cdStock.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdStockReposDay.deleteAll(cdStock);	
      cdStockReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdStockReposMon.deleteAll(cdStock);	
      cdStockReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdStockReposHist.deleteAll(cdStock);
      cdStockReposHist.flush();
    }
    else {
      cdStockRepos.deleteAll(cdStock);
      cdStockRepos.flush();
    }
  }

}
