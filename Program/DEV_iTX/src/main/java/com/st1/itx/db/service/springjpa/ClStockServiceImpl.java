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
import com.st1.itx.db.domain.ClStock;
import com.st1.itx.db.domain.ClStockId;
import com.st1.itx.db.repository.online.ClStockRepository;
import com.st1.itx.db.repository.day.ClStockRepositoryDay;
import com.st1.itx.db.repository.mon.ClStockRepositoryMon;
import com.st1.itx.db.repository.hist.ClStockRepositoryHist;
import com.st1.itx.db.service.ClStockService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clStockService")
@Repository
public class ClStockServiceImpl extends ASpringJpaParm implements ClStockService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private ClStockRepository clStockRepos;

  @Autowired
  private ClStockRepositoryDay clStockReposDay;

  @Autowired
  private ClStockRepositoryMon clStockReposMon;

  @Autowired
  private ClStockRepositoryHist clStockReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(clStockRepos);
    org.junit.Assert.assertNotNull(clStockReposDay);
    org.junit.Assert.assertNotNull(clStockReposMon);
    org.junit.Assert.assertNotNull(clStockReposHist);
  }

  @Override
  public ClStock findById(ClStockId clStockId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + clStockId);
    Optional<ClStock> clStock = null;
    if (dbName.equals(ContentName.onDay))
      clStock = clStockReposDay.findById(clStockId);
    else if (dbName.equals(ContentName.onMon))
      clStock = clStockReposMon.findById(clStockId);
    else if (dbName.equals(ContentName.onHist))
      clStock = clStockReposHist.findById(clStockId);
    else 
      clStock = clStockRepos.findById(clStockId);
    ClStock obj = clStock.isPresent() ? clStock.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<ClStock> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClStock> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = clStockReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clStockReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clStockReposHist.findAll(pageable);
    else 
      slice = clStockRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClStock> findClCode1(int clCode1_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClStock> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findClCode1 " + dbName + " : " + "clCode1_0 : " + clCode1_0);
    if (dbName.equals(ContentName.onDay))
      slice = clStockReposDay.findAllByClCode1Is(clCode1_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clStockReposMon.findAllByClCode1Is(clCode1_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clStockReposHist.findAllByClCode1Is(clCode1_0, pageable);
    else 
      slice = clStockRepos.findAllByClCode1Is(clCode1_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClStock> findClCode2(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClStock> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findClCode2 " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1);
    if (dbName.equals(ContentName.onDay))
      slice = clStockReposDay.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clStockReposMon.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clStockReposHist.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);
    else 
      slice = clStockRepos.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClStock> findUnique(String stockCode_0, String ownerCustUKey_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClStock> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findUnique " + dbName + " : " + "stockCode_0 : " + stockCode_0 + " ownerCustUKey_1 : " +  ownerCustUKey_1);
    if (dbName.equals(ContentName.onDay))
      slice = clStockReposDay.findAllByStockCodeIsAndOwnerCustUKeyIs(stockCode_0, ownerCustUKey_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clStockReposMon.findAllByStockCodeIsAndOwnerCustUKeyIs(stockCode_0, ownerCustUKey_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clStockReposHist.findAllByStockCodeIsAndOwnerCustUKeyIs(stockCode_0, ownerCustUKey_1, pageable);
    else 
      slice = clStockRepos.findAllByStockCodeIsAndOwnerCustUKeyIs(stockCode_0, ownerCustUKey_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ClStock holdById(ClStockId clStockId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clStockId);
    Optional<ClStock> clStock = null;
    if (dbName.equals(ContentName.onDay))
      clStock = clStockReposDay.findByClStockId(clStockId);
    else if (dbName.equals(ContentName.onMon))
      clStock = clStockReposMon.findByClStockId(clStockId);
    else if (dbName.equals(ContentName.onHist))
      clStock = clStockReposHist.findByClStockId(clStockId);
    else 
      clStock = clStockRepos.findByClStockId(clStockId);
    return clStock.isPresent() ? clStock.get() : null;
  }

  @Override
  public ClStock holdById(ClStock clStock, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clStock.getClStockId());
    Optional<ClStock> clStockT = null;
    if (dbName.equals(ContentName.onDay))
      clStockT = clStockReposDay.findByClStockId(clStock.getClStockId());
    else if (dbName.equals(ContentName.onMon))
      clStockT = clStockReposMon.findByClStockId(clStock.getClStockId());
    else if (dbName.equals(ContentName.onHist))
      clStockT = clStockReposHist.findByClStockId(clStock.getClStockId());
    else 
      clStockT = clStockRepos.findByClStockId(clStock.getClStockId());
    return clStockT.isPresent() ? clStockT.get() : null;
  }

  @Override
  public ClStock insert(ClStock clStock, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + clStock.getClStockId());
    if (this.findById(clStock.getClStockId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      clStock.setCreateEmpNo(empNot);

    if(clStock.getLastUpdateEmpNo() == null || clStock.getLastUpdateEmpNo().isEmpty())
      clStock.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clStockReposDay.saveAndFlush(clStock);	
    else if (dbName.equals(ContentName.onMon))
      return clStockReposMon.saveAndFlush(clStock);
    else if (dbName.equals(ContentName.onHist))
      return clStockReposHist.saveAndFlush(clStock);
    else 
    return clStockRepos.saveAndFlush(clStock);
  }

  @Override
  public ClStock update(ClStock clStock, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + clStock.getClStockId());
    if (!empNot.isEmpty())
      clStock.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clStockReposDay.saveAndFlush(clStock);	
    else if (dbName.equals(ContentName.onMon))
      return clStockReposMon.saveAndFlush(clStock);
    else if (dbName.equals(ContentName.onHist))
      return clStockReposHist.saveAndFlush(clStock);
    else 
    return clStockRepos.saveAndFlush(clStock);
  }

  @Override
  public ClStock update2(ClStock clStock, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + clStock.getClStockId());
    if (!empNot.isEmpty())
      clStock.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      clStockReposDay.saveAndFlush(clStock);	
    else if (dbName.equals(ContentName.onMon))
      clStockReposMon.saveAndFlush(clStock);
    else if (dbName.equals(ContentName.onHist))
        clStockReposHist.saveAndFlush(clStock);
    else 
      clStockRepos.saveAndFlush(clStock);	
    return this.findById(clStock.getClStockId());
  }

  @Override
  public void delete(ClStock clStock, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + clStock.getClStockId());
    if (dbName.equals(ContentName.onDay)) {
      clStockReposDay.delete(clStock);	
      clStockReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clStockReposMon.delete(clStock);	
      clStockReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clStockReposHist.delete(clStock);
      clStockReposHist.flush();
    }
    else {
      clStockRepos.delete(clStock);
      clStockRepos.flush();
    }
   }

  @Override
  public void insertAll(List<ClStock> clStock, TitaVo... titaVo) throws DBException {
    if (clStock == null || clStock.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (ClStock t : clStock){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      clStock = clStockReposDay.saveAll(clStock);	
      clStockReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clStock = clStockReposMon.saveAll(clStock);	
      clStockReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clStock = clStockReposHist.saveAll(clStock);
      clStockReposHist.flush();
    }
    else {
      clStock = clStockRepos.saveAll(clStock);
      clStockRepos.flush();
    }
    }

  @Override
  public void updateAll(List<ClStock> clStock, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (clStock == null || clStock.size() == 0)
      throw new DBException(6);

    for (ClStock t : clStock) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      clStock = clStockReposDay.saveAll(clStock);	
      clStockReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clStock = clStockReposMon.saveAll(clStock);	
      clStockReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clStock = clStockReposHist.saveAll(clStock);
      clStockReposHist.flush();
    }
    else {
      clStock = clStockRepos.saveAll(clStock);
      clStockRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<ClStock> clStock, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (clStock == null || clStock.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      clStockReposDay.deleteAll(clStock);	
      clStockReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clStockReposMon.deleteAll(clStock);	
      clStockReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clStockReposHist.deleteAll(clStock);
      clStockReposHist.flush();
    }
    else {
      clStockRepos.deleteAll(clStock);
      clStockRepos.flush();
    }
  }

}
