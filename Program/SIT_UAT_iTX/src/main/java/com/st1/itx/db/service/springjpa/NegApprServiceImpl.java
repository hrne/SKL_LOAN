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
import com.st1.itx.db.domain.NegAppr;
import com.st1.itx.db.domain.NegApprId;
import com.st1.itx.db.repository.online.NegApprRepository;
import com.st1.itx.db.repository.day.NegApprRepositoryDay;
import com.st1.itx.db.repository.mon.NegApprRepositoryMon;
import com.st1.itx.db.repository.hist.NegApprRepositoryHist;
import com.st1.itx.db.service.NegApprService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("negApprService")
@Repository
public class NegApprServiceImpl extends ASpringJpaParm implements NegApprService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private NegApprRepository negApprRepos;

  @Autowired
  private NegApprRepositoryDay negApprReposDay;

  @Autowired
  private NegApprRepositoryMon negApprReposMon;

  @Autowired
  private NegApprRepositoryHist negApprReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(negApprRepos);
    org.junit.Assert.assertNotNull(negApprReposDay);
    org.junit.Assert.assertNotNull(negApprReposMon);
    org.junit.Assert.assertNotNull(negApprReposHist);
  }

  @Override
  public NegAppr findById(NegApprId negApprId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + negApprId);
    Optional<NegAppr> negAppr = null;
    if (dbName.equals(ContentName.onDay))
      negAppr = negApprReposDay.findById(negApprId);
    else if (dbName.equals(ContentName.onMon))
      negAppr = negApprReposMon.findById(negApprId);
    else if (dbName.equals(ContentName.onHist))
      negAppr = negApprReposHist.findById(negApprId);
    else 
      negAppr = negApprRepos.findById(negApprId);
    NegAppr obj = negAppr.isPresent() ? negAppr.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<NegAppr> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "YyyyMm", "KindCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "YyyyMm", "KindCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = negApprReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negApprReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negApprReposHist.findAll(pageable);
    else 
      slice = negApprRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegAppr> yyyyMmBetween(int yyyyMm_0, int yyyyMm_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("yyyyMmBetween " + dbName + " : " + "yyyyMm_0 : " + yyyyMm_0 + " yyyyMm_1 : " +  yyyyMm_1);
    if (dbName.equals(ContentName.onDay))
      slice = negApprReposDay.findAllByYyyyMmGreaterThanEqualAndYyyyMmLessThanEqualOrderByYyyyMmAscKindCodeAsc(yyyyMm_0, yyyyMm_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negApprReposMon.findAllByYyyyMmGreaterThanEqualAndYyyyMmLessThanEqualOrderByYyyyMmAscKindCodeAsc(yyyyMm_0, yyyyMm_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negApprReposHist.findAllByYyyyMmGreaterThanEqualAndYyyyMmLessThanEqualOrderByYyyyMmAscKindCodeAsc(yyyyMm_0, yyyyMm_1, pageable);
    else 
      slice = negApprRepos.findAllByYyyyMmGreaterThanEqualAndYyyyMmLessThanEqualOrderByYyyyMmAscKindCodeAsc(yyyyMm_0, yyyyMm_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegAppr> yyyyMmEq(int yyyyMm_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("yyyyMmEq " + dbName + " : " + "yyyyMm_0 : " + yyyyMm_0);
    if (dbName.equals(ContentName.onDay))
      slice = negApprReposDay.findAllByYyyyMmIsOrderByYyyyMmAscKindCodeAsc(yyyyMm_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negApprReposMon.findAllByYyyyMmIsOrderByYyyyMmAscKindCodeAsc(yyyyMm_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negApprReposHist.findAllByYyyyMmIsOrderByYyyyMmAscKindCodeAsc(yyyyMm_0, pageable);
    else 
      slice = negApprRepos.findAllByYyyyMmIsOrderByYyyyMmAscKindCodeAsc(yyyyMm_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegAppr> acDateEq(int exportDate_0, int apprAcDate_1, int bringUpDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acDateEq " + dbName + " : " + "exportDate_0 : " + exportDate_0 + " apprAcDate_1 : " +  apprAcDate_1 + " bringUpDate_2 : " +  bringUpDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = negApprReposDay.findAllByExportDateIsOrApprAcDateIsOrBringUpDateIsOrderByYyyyMmAscKindCodeAsc(exportDate_0, apprAcDate_1, bringUpDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negApprReposMon.findAllByExportDateIsOrApprAcDateIsOrBringUpDateIsOrderByYyyyMmAscKindCodeAsc(exportDate_0, apprAcDate_1, bringUpDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negApprReposHist.findAllByExportDateIsOrApprAcDateIsOrBringUpDateIsOrderByYyyyMmAscKindCodeAsc(exportDate_0, apprAcDate_1, bringUpDate_2, pageable);
    else 
      slice = negApprRepos.findAllByExportDateIsOrApprAcDateIsOrBringUpDateIsOrderByYyyyMmAscKindCodeAsc(exportDate_0, apprAcDate_1, bringUpDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegAppr> bringUpDateEq(int bringUpDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("bringUpDateEq " + dbName + " : " + "bringUpDate_0 : " + bringUpDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = negApprReposDay.findAllByBringUpDateIsOrderByYyyyMmAscKindCodeAsc(bringUpDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negApprReposMon.findAllByBringUpDateIsOrderByYyyyMmAscKindCodeAsc(bringUpDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negApprReposHist.findAllByBringUpDateIsOrderByYyyyMmAscKindCodeAsc(bringUpDate_0, pageable);
    else 
      slice = negApprRepos.findAllByBringUpDateIsOrderByYyyyMmAscKindCodeAsc(bringUpDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public NegAppr holdById(NegApprId negApprId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + negApprId);
    Optional<NegAppr> negAppr = null;
    if (dbName.equals(ContentName.onDay))
      negAppr = negApprReposDay.findByNegApprId(negApprId);
    else if (dbName.equals(ContentName.onMon))
      negAppr = negApprReposMon.findByNegApprId(negApprId);
    else if (dbName.equals(ContentName.onHist))
      negAppr = negApprReposHist.findByNegApprId(negApprId);
    else 
      negAppr = negApprRepos.findByNegApprId(negApprId);
    return negAppr.isPresent() ? negAppr.get() : null;
  }

  @Override
  public NegAppr holdById(NegAppr negAppr, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + negAppr.getNegApprId());
    Optional<NegAppr> negApprT = null;
    if (dbName.equals(ContentName.onDay))
      negApprT = negApprReposDay.findByNegApprId(negAppr.getNegApprId());
    else if (dbName.equals(ContentName.onMon))
      negApprT = negApprReposMon.findByNegApprId(negAppr.getNegApprId());
    else if (dbName.equals(ContentName.onHist))
      negApprT = negApprReposHist.findByNegApprId(negAppr.getNegApprId());
    else 
      negApprT = negApprRepos.findByNegApprId(negAppr.getNegApprId());
    return negApprT.isPresent() ? negApprT.get() : null;
  }

  @Override
  public NegAppr insert(NegAppr negAppr, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + negAppr.getNegApprId());
    if (this.findById(negAppr.getNegApprId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      negAppr.setCreateEmpNo(empNot);

    if(negAppr.getLastUpdateEmpNo() == null || negAppr.getLastUpdateEmpNo().isEmpty())
      negAppr.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return negApprReposDay.saveAndFlush(negAppr);	
    else if (dbName.equals(ContentName.onMon))
      return negApprReposMon.saveAndFlush(negAppr);
    else if (dbName.equals(ContentName.onHist))
      return negApprReposHist.saveAndFlush(negAppr);
    else 
    return negApprRepos.saveAndFlush(negAppr);
  }

  @Override
  public NegAppr update(NegAppr negAppr, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + negAppr.getNegApprId());
    if (!empNot.isEmpty())
      negAppr.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return negApprReposDay.saveAndFlush(negAppr);	
    else if (dbName.equals(ContentName.onMon))
      return negApprReposMon.saveAndFlush(negAppr);
    else if (dbName.equals(ContentName.onHist))
      return negApprReposHist.saveAndFlush(negAppr);
    else 
    return negApprRepos.saveAndFlush(negAppr);
  }

  @Override
  public NegAppr update2(NegAppr negAppr, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + negAppr.getNegApprId());
    if (!empNot.isEmpty())
      negAppr.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      negApprReposDay.saveAndFlush(negAppr);	
    else if (dbName.equals(ContentName.onMon))
      negApprReposMon.saveAndFlush(negAppr);
    else if (dbName.equals(ContentName.onHist))
        negApprReposHist.saveAndFlush(negAppr);
    else 
      negApprRepos.saveAndFlush(negAppr);	
    return this.findById(negAppr.getNegApprId());
  }

  @Override
  public void delete(NegAppr negAppr, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + negAppr.getNegApprId());
    if (dbName.equals(ContentName.onDay)) {
      negApprReposDay.delete(negAppr);	
      negApprReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negApprReposMon.delete(negAppr);	
      negApprReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negApprReposHist.delete(negAppr);
      negApprReposHist.flush();
    }
    else {
      negApprRepos.delete(negAppr);
      negApprRepos.flush();
    }
   }

  @Override
  public void insertAll(List<NegAppr> negAppr, TitaVo... titaVo) throws DBException {
    if (negAppr == null || negAppr.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (NegAppr t : negAppr){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      negAppr = negApprReposDay.saveAll(negAppr);	
      negApprReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negAppr = negApprReposMon.saveAll(negAppr);	
      negApprReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negAppr = negApprReposHist.saveAll(negAppr);
      negApprReposHist.flush();
    }
    else {
      negAppr = negApprRepos.saveAll(negAppr);
      negApprRepos.flush();
    }
    }

  @Override
  public void updateAll(List<NegAppr> negAppr, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (negAppr == null || negAppr.size() == 0)
      throw new DBException(6);

    for (NegAppr t : negAppr) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      negAppr = negApprReposDay.saveAll(negAppr);	
      negApprReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negAppr = negApprReposMon.saveAll(negAppr);	
      negApprReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negAppr = negApprReposHist.saveAll(negAppr);
      negApprReposHist.flush();
    }
    else {
      negAppr = negApprRepos.saveAll(negAppr);
      negApprRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<NegAppr> negAppr, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (negAppr == null || negAppr.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      negApprReposDay.deleteAll(negAppr);	
      negApprReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negApprReposMon.deleteAll(negAppr);	
      negApprReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negApprReposHist.deleteAll(negAppr);
      negApprReposHist.flush();
    }
    else {
      negApprRepos.deleteAll(negAppr);
      negApprRepos.flush();
    }
  }

}
