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
import com.st1.itx.db.domain.CdInsurer;
import com.st1.itx.db.domain.CdInsurerId;
import com.st1.itx.db.repository.online.CdInsurerRepository;
import com.st1.itx.db.repository.day.CdInsurerRepositoryDay;
import com.st1.itx.db.repository.mon.CdInsurerRepositoryMon;
import com.st1.itx.db.repository.hist.CdInsurerRepositoryHist;
import com.st1.itx.db.service.CdInsurerService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdInsurerService")
@Repository
public class CdInsurerServiceImpl implements CdInsurerService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(CdInsurerServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdInsurerRepository cdInsurerRepos;

  @Autowired
  private CdInsurerRepositoryDay cdInsurerReposDay;

  @Autowired
  private CdInsurerRepositoryMon cdInsurerReposMon;

  @Autowired
  private CdInsurerRepositoryHist cdInsurerReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdInsurerRepos);
    org.junit.Assert.assertNotNull(cdInsurerReposDay);
    org.junit.Assert.assertNotNull(cdInsurerReposMon);
    org.junit.Assert.assertNotNull(cdInsurerReposHist);
  }

  @Override
  public CdInsurer findById(CdInsurerId cdInsurerId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + cdInsurerId);
    Optional<CdInsurer> cdInsurer = null;
    if (dbName.equals(ContentName.onDay))
      cdInsurer = cdInsurerReposDay.findById(cdInsurerId);
    else if (dbName.equals(ContentName.onMon))
      cdInsurer = cdInsurerReposMon.findById(cdInsurerId);
    else if (dbName.equals(ContentName.onHist))
      cdInsurer = cdInsurerReposHist.findById(cdInsurerId);
    else 
      cdInsurer = cdInsurerRepos.findById(cdInsurerId);
    CdInsurer obj = cdInsurer.isPresent() ? cdInsurer.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdInsurer> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdInsurer> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "InsurerType", "InsurerCode"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdInsurerReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdInsurerReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdInsurerReposHist.findAll(pageable);
    else 
      slice = cdInsurerRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdInsurer> insurerItemLike(String insurerItem_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdInsurer> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("insurerItemLike " + dbName + " : " + "insurerItem_0 : " + insurerItem_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdInsurerReposDay.findAllByInsurerItemLike(insurerItem_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdInsurerReposMon.findAllByInsurerItemLike(insurerItem_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdInsurerReposHist.findAllByInsurerItemLike(insurerItem_0, pageable);
    else 
      slice = cdInsurerRepos.findAllByInsurerItemLike(insurerItem_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdInsurer> insurerTypeRange(String insurerType_0, String insurerType_1, String insurerCode_2, String insurerCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdInsurer> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("insurerTypeRange " + dbName + " : " + "insurerType_0 : " + insurerType_0 + " insurerType_1 : " +  insurerType_1 + " insurerCode_2 : " +  insurerCode_2 + " insurerCode_3 : " +  insurerCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = cdInsurerReposDay.findAllByInsurerTypeGreaterThanEqualAndInsurerTypeLessThanEqualAndInsurerCodeGreaterThanEqualAndInsurerCodeLessThanEqualOrderByInsurerTypeAscInsurerCodeAsc(insurerType_0, insurerType_1, insurerCode_2, insurerCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdInsurerReposMon.findAllByInsurerTypeGreaterThanEqualAndInsurerTypeLessThanEqualAndInsurerCodeGreaterThanEqualAndInsurerCodeLessThanEqualOrderByInsurerTypeAscInsurerCodeAsc(insurerType_0, insurerType_1, insurerCode_2, insurerCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdInsurerReposHist.findAllByInsurerTypeGreaterThanEqualAndInsurerTypeLessThanEqualAndInsurerCodeGreaterThanEqualAndInsurerCodeLessThanEqualOrderByInsurerTypeAscInsurerCodeAsc(insurerType_0, insurerType_1, insurerCode_2, insurerCode_3, pageable);
    else 
      slice = cdInsurerRepos.findAllByInsurerTypeGreaterThanEqualAndInsurerTypeLessThanEqualAndInsurerCodeGreaterThanEqualAndInsurerCodeLessThanEqualOrderByInsurerTypeAscInsurerCodeAsc(insurerType_0, insurerType_1, insurerCode_2, insurerCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdInsurer holdById(CdInsurerId cdInsurerId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + cdInsurerId);
    Optional<CdInsurer> cdInsurer = null;
    if (dbName.equals(ContentName.onDay))
      cdInsurer = cdInsurerReposDay.findByCdInsurerId(cdInsurerId);
    else if (dbName.equals(ContentName.onMon))
      cdInsurer = cdInsurerReposMon.findByCdInsurerId(cdInsurerId);
    else if (dbName.equals(ContentName.onHist))
      cdInsurer = cdInsurerReposHist.findByCdInsurerId(cdInsurerId);
    else 
      cdInsurer = cdInsurerRepos.findByCdInsurerId(cdInsurerId);
    return cdInsurer.isPresent() ? cdInsurer.get() : null;
  }

  @Override
  public CdInsurer holdById(CdInsurer cdInsurer, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + cdInsurer.getCdInsurerId());
    Optional<CdInsurer> cdInsurerT = null;
    if (dbName.equals(ContentName.onDay))
      cdInsurerT = cdInsurerReposDay.findByCdInsurerId(cdInsurer.getCdInsurerId());
    else if (dbName.equals(ContentName.onMon))
      cdInsurerT = cdInsurerReposMon.findByCdInsurerId(cdInsurer.getCdInsurerId());
    else if (dbName.equals(ContentName.onHist))
      cdInsurerT = cdInsurerReposHist.findByCdInsurerId(cdInsurer.getCdInsurerId());
    else 
      cdInsurerT = cdInsurerRepos.findByCdInsurerId(cdInsurer.getCdInsurerId());
    return cdInsurerT.isPresent() ? cdInsurerT.get() : null;
  }

  @Override
  public CdInsurer insert(CdInsurer cdInsurer, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + cdInsurer.getCdInsurerId());
    if (this.findById(cdInsurer.getCdInsurerId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdInsurer.setCreateEmpNo(empNot);

    if(cdInsurer.getLastUpdateEmpNo() == null || cdInsurer.getLastUpdateEmpNo().isEmpty())
      cdInsurer.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdInsurerReposDay.saveAndFlush(cdInsurer);	
    else if (dbName.equals(ContentName.onMon))
      return cdInsurerReposMon.saveAndFlush(cdInsurer);
    else if (dbName.equals(ContentName.onHist))
      return cdInsurerReposHist.saveAndFlush(cdInsurer);
    else 
    return cdInsurerRepos.saveAndFlush(cdInsurer);
  }

  @Override
  public CdInsurer update(CdInsurer cdInsurer, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + cdInsurer.getCdInsurerId());
    if (!empNot.isEmpty())
      cdInsurer.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdInsurerReposDay.saveAndFlush(cdInsurer);	
    else if (dbName.equals(ContentName.onMon))
      return cdInsurerReposMon.saveAndFlush(cdInsurer);
    else if (dbName.equals(ContentName.onHist))
      return cdInsurerReposHist.saveAndFlush(cdInsurer);
    else 
    return cdInsurerRepos.saveAndFlush(cdInsurer);
  }

  @Override
  public CdInsurer update2(CdInsurer cdInsurer, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + cdInsurer.getCdInsurerId());
    if (!empNot.isEmpty())
      cdInsurer.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdInsurerReposDay.saveAndFlush(cdInsurer);	
    else if (dbName.equals(ContentName.onMon))
      cdInsurerReposMon.saveAndFlush(cdInsurer);
    else if (dbName.equals(ContentName.onHist))
        cdInsurerReposHist.saveAndFlush(cdInsurer);
    else 
      cdInsurerRepos.saveAndFlush(cdInsurer);	
    return this.findById(cdInsurer.getCdInsurerId());
  }

  @Override
  public void delete(CdInsurer cdInsurer, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + cdInsurer.getCdInsurerId());
    if (dbName.equals(ContentName.onDay)) {
      cdInsurerReposDay.delete(cdInsurer);	
      cdInsurerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdInsurerReposMon.delete(cdInsurer);	
      cdInsurerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdInsurerReposHist.delete(cdInsurer);
      cdInsurerReposHist.flush();
    }
    else {
      cdInsurerRepos.delete(cdInsurer);
      cdInsurerRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdInsurer> cdInsurer, TitaVo... titaVo) throws DBException {
    if (cdInsurer == null || cdInsurer.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (CdInsurer t : cdInsurer){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdInsurer = cdInsurerReposDay.saveAll(cdInsurer);	
      cdInsurerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdInsurer = cdInsurerReposMon.saveAll(cdInsurer);	
      cdInsurerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdInsurer = cdInsurerReposHist.saveAll(cdInsurer);
      cdInsurerReposHist.flush();
    }
    else {
      cdInsurer = cdInsurerRepos.saveAll(cdInsurer);
      cdInsurerRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdInsurer> cdInsurer, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (cdInsurer == null || cdInsurer.size() == 0)
      throw new DBException(6);

    for (CdInsurer t : cdInsurer) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdInsurer = cdInsurerReposDay.saveAll(cdInsurer);	
      cdInsurerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdInsurer = cdInsurerReposMon.saveAll(cdInsurer);	
      cdInsurerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdInsurer = cdInsurerReposHist.saveAll(cdInsurer);
      cdInsurerReposHist.flush();
    }
    else {
      cdInsurer = cdInsurerRepos.saveAll(cdInsurer);
      cdInsurerRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdInsurer> cdInsurer, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdInsurer == null || cdInsurer.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdInsurerReposDay.deleteAll(cdInsurer);	
      cdInsurerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdInsurerReposMon.deleteAll(cdInsurer);	
      cdInsurerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdInsurerReposHist.deleteAll(cdInsurer);
      cdInsurerReposHist.flush();
    }
    else {
      cdInsurerRepos.deleteAll(cdInsurer);
      cdInsurerRepos.flush();
    }
  }

}
