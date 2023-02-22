package com.st1.itx.db.service.springjpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

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
import com.st1.itx.db.domain.CdComm;
import com.st1.itx.db.domain.CdCommId;
import com.st1.itx.db.repository.online.CdCommRepository;
import com.st1.itx.db.repository.day.CdCommRepositoryDay;
import com.st1.itx.db.repository.mon.CdCommRepositoryMon;
import com.st1.itx.db.repository.hist.CdCommRepositoryHist;
import com.st1.itx.db.service.CdCommService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdCommService")
@Repository
public class CdCommServiceImpl extends ASpringJpaParm implements CdCommService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdCommRepository cdCommRepos;

  @Autowired
  private CdCommRepositoryDay cdCommReposDay;

  @Autowired
  private CdCommRepositoryMon cdCommReposMon;

  @Autowired
  private CdCommRepositoryHist cdCommReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdCommRepos);
    org.junit.Assert.assertNotNull(cdCommReposDay);
    org.junit.Assert.assertNotNull(cdCommReposMon);
    org.junit.Assert.assertNotNull(cdCommReposHist);
  }

  @Override
  public CdComm findById(CdCommId cdCommId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + cdCommId);
    Optional<CdComm> cdComm = null;
    if (dbName.equals(ContentName.onDay))
      cdComm = cdCommReposDay.findById(cdCommId);
    else if (dbName.equals(ContentName.onMon))
      cdComm = cdCommReposMon.findById(cdCommId);
    else if (dbName.equals(ContentName.onHist))
      cdComm = cdCommReposHist.findById(cdCommId);
    else 
      cdComm = cdCommRepos.findById(cdCommId);
    CdComm obj = cdComm.isPresent() ? cdComm.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdComm> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdComm> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CdType", "CdItem", "EffectDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CdType", "CdItem", "EffectDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdCommReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCommReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCommReposHist.findAll(pageable);
    else 
      slice = cdCommRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdComm CdTypeDescFirst(String cdType_0, String cdItem_1, int effectDate_2, int effectDate_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("CdTypeDescFirst " + dbName + " : " + "cdType_0 : " + cdType_0 + " cdItem_1 : " +  cdItem_1 + " effectDate_2 : " +  effectDate_2 + " effectDate_3 : " +  effectDate_3);
    Optional<CdComm> cdCommT = null;
    if (dbName.equals(ContentName.onDay))
      cdCommT = cdCommReposDay.findTopByCdTypeIsAndCdItemIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateDesc(cdType_0, cdItem_1, effectDate_2, effectDate_3);
    else if (dbName.equals(ContentName.onMon))
      cdCommT = cdCommReposMon.findTopByCdTypeIsAndCdItemIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateDesc(cdType_0, cdItem_1, effectDate_2, effectDate_3);
    else if (dbName.equals(ContentName.onHist))
      cdCommT = cdCommReposHist.findTopByCdTypeIsAndCdItemIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateDesc(cdType_0, cdItem_1, effectDate_2, effectDate_3);
    else 
      cdCommT = cdCommRepos.findTopByCdTypeIsAndCdItemIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateDesc(cdType_0, cdItem_1, effectDate_2, effectDate_3);

    return cdCommT.isPresent() ? cdCommT.get() : null;
  }

  @Override
  public CdComm CdTypeAscFirst(String cdType_0, String cdItem_1, int effectDate_2, int effectDate_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("CdTypeAscFirst " + dbName + " : " + "cdType_0 : " + cdType_0 + " cdItem_1 : " +  cdItem_1 + " effectDate_2 : " +  effectDate_2 + " effectDate_3 : " +  effectDate_3);
    Optional<CdComm> cdCommT = null;
    if (dbName.equals(ContentName.onDay))
      cdCommT = cdCommReposDay.findTopByCdTypeIsAndCdItemIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateAsc(cdType_0, cdItem_1, effectDate_2, effectDate_3);
    else if (dbName.equals(ContentName.onMon))
      cdCommT = cdCommReposMon.findTopByCdTypeIsAndCdItemIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateAsc(cdType_0, cdItem_1, effectDate_2, effectDate_3);
    else if (dbName.equals(ContentName.onHist))
      cdCommT = cdCommReposHist.findTopByCdTypeIsAndCdItemIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateAsc(cdType_0, cdItem_1, effectDate_2, effectDate_3);
    else 
      cdCommT = cdCommRepos.findTopByCdTypeIsAndCdItemIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateAsc(cdType_0, cdItem_1, effectDate_2, effectDate_3);

    return cdCommT.isPresent() ? cdCommT.get() : null;
  }

  @Override
  public CdComm holdById(CdCommId cdCommId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdCommId);
    Optional<CdComm> cdComm = null;
    if (dbName.equals(ContentName.onDay))
      cdComm = cdCommReposDay.findByCdCommId(cdCommId);
    else if (dbName.equals(ContentName.onMon))
      cdComm = cdCommReposMon.findByCdCommId(cdCommId);
    else if (dbName.equals(ContentName.onHist))
      cdComm = cdCommReposHist.findByCdCommId(cdCommId);
    else 
      cdComm = cdCommRepos.findByCdCommId(cdCommId);
    return cdComm.isPresent() ? cdComm.get() : null;
  }

  @Override
  public CdComm holdById(CdComm cdComm, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdComm.getCdCommId());
    Optional<CdComm> cdCommT = null;
    if (dbName.equals(ContentName.onDay))
      cdCommT = cdCommReposDay.findByCdCommId(cdComm.getCdCommId());
    else if (dbName.equals(ContentName.onMon))
      cdCommT = cdCommReposMon.findByCdCommId(cdComm.getCdCommId());
    else if (dbName.equals(ContentName.onHist))
      cdCommT = cdCommReposHist.findByCdCommId(cdComm.getCdCommId());
    else 
      cdCommT = cdCommRepos.findByCdCommId(cdComm.getCdCommId());
    return cdCommT.isPresent() ? cdCommT.get() : null;
  }

  @Override
  public CdComm insert(CdComm cdComm, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + cdComm.getCdCommId());
    if (this.findById(cdComm.getCdCommId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdComm.setCreateEmpNo(empNot);

    if(cdComm.getLastUpdateEmpNo() == null || cdComm.getLastUpdateEmpNo().isEmpty())
      cdComm.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdCommReposDay.saveAndFlush(cdComm);	
    else if (dbName.equals(ContentName.onMon))
      return cdCommReposMon.saveAndFlush(cdComm);
    else if (dbName.equals(ContentName.onHist))
      return cdCommReposHist.saveAndFlush(cdComm);
    else 
    return cdCommRepos.saveAndFlush(cdComm);
  }

  @Override
  public CdComm update(CdComm cdComm, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdComm.getCdCommId());
    if (!empNot.isEmpty())
      cdComm.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdCommReposDay.saveAndFlush(cdComm);	
    else if (dbName.equals(ContentName.onMon))
      return cdCommReposMon.saveAndFlush(cdComm);
    else if (dbName.equals(ContentName.onHist))
      return cdCommReposHist.saveAndFlush(cdComm);
    else 
    return cdCommRepos.saveAndFlush(cdComm);
  }

  @Override
  public CdComm update2(CdComm cdComm, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdComm.getCdCommId());
    if (!empNot.isEmpty())
      cdComm.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdCommReposDay.saveAndFlush(cdComm);	
    else if (dbName.equals(ContentName.onMon))
      cdCommReposMon.saveAndFlush(cdComm);
    else if (dbName.equals(ContentName.onHist))
        cdCommReposHist.saveAndFlush(cdComm);
    else 
      cdCommRepos.saveAndFlush(cdComm);	
    return this.findById(cdComm.getCdCommId());
  }

  @Override
  public void delete(CdComm cdComm, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdComm.getCdCommId());
    if (dbName.equals(ContentName.onDay)) {
      cdCommReposDay.delete(cdComm);	
      cdCommReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdCommReposMon.delete(cdComm);	
      cdCommReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdCommReposHist.delete(cdComm);
      cdCommReposHist.flush();
    }
    else {
      cdCommRepos.delete(cdComm);
      cdCommRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdComm> cdComm, TitaVo... titaVo) throws DBException {
    if (cdComm == null || cdComm.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CdComm t : cdComm){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdComm = cdCommReposDay.saveAll(cdComm);	
      cdCommReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdComm = cdCommReposMon.saveAll(cdComm);	
      cdCommReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdComm = cdCommReposHist.saveAll(cdComm);
      cdCommReposHist.flush();
    }
    else {
      cdComm = cdCommRepos.saveAll(cdComm);
      cdCommRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdComm> cdComm, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (cdComm == null || cdComm.size() == 0)
      throw new DBException(6);

    for (CdComm t : cdComm) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdComm = cdCommReposDay.saveAll(cdComm);	
      cdCommReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdComm = cdCommReposMon.saveAll(cdComm);	
      cdCommReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdComm = cdCommReposHist.saveAll(cdComm);
      cdCommReposHist.flush();
    }
    else {
      cdComm = cdCommRepos.saveAll(cdComm);
      cdCommRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdComm> cdComm, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdComm == null || cdComm.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdCommReposDay.deleteAll(cdComm);	
      cdCommReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdCommReposMon.deleteAll(cdComm);	
      cdCommReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdCommReposHist.deleteAll(cdComm);
      cdCommReposHist.flush();
    }
    else {
      cdCommRepos.deleteAll(cdComm);
      cdCommRepos.flush();
    }
  }

}
