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
import com.st1.itx.db.domain.CdLandSection;
import com.st1.itx.db.domain.CdLandSectionId;
import com.st1.itx.db.repository.online.CdLandSectionRepository;
import com.st1.itx.db.repository.day.CdLandSectionRepositoryDay;
import com.st1.itx.db.repository.mon.CdLandSectionRepositoryMon;
import com.st1.itx.db.repository.hist.CdLandSectionRepositoryHist;
import com.st1.itx.db.service.CdLandSectionService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdLandSectionService")
@Repository
public class CdLandSectionServiceImpl extends ASpringJpaParm implements CdLandSectionService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdLandSectionRepository cdLandSectionRepos;

  @Autowired
  private CdLandSectionRepositoryDay cdLandSectionReposDay;

  @Autowired
  private CdLandSectionRepositoryMon cdLandSectionReposMon;

  @Autowired
  private CdLandSectionRepositoryHist cdLandSectionReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdLandSectionRepos);
    org.junit.Assert.assertNotNull(cdLandSectionReposDay);
    org.junit.Assert.assertNotNull(cdLandSectionReposMon);
    org.junit.Assert.assertNotNull(cdLandSectionReposHist);
  }

  @Override
  public CdLandSection findById(CdLandSectionId cdLandSectionId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + cdLandSectionId);
    Optional<CdLandSection> cdLandSection = null;
    if (dbName.equals(ContentName.onDay))
      cdLandSection = cdLandSectionReposDay.findById(cdLandSectionId);
    else if (dbName.equals(ContentName.onMon))
      cdLandSection = cdLandSectionReposMon.findById(cdLandSectionId);
    else if (dbName.equals(ContentName.onHist))
      cdLandSection = cdLandSectionReposHist.findById(cdLandSectionId);
    else 
      cdLandSection = cdLandSectionRepos.findById(cdLandSectionId);
    CdLandSection obj = cdLandSection.isPresent() ? cdLandSection.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdLandSection> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdLandSection> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CityCode", "AreaCode", "IrCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CityCode", "AreaCode", "IrCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdLandSectionReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdLandSectionReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdLandSectionReposHist.findAll(pageable);
    else 
      slice = cdLandSectionRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdLandSection> cityCodeEq(String cityCode_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdLandSection> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("cityCodeEq " + dbName + " : " + "cityCode_0 : " + cityCode_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdLandSectionReposDay.findAllByCityCodeIsOrderByCityCodeAscIrCodeAsc(cityCode_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdLandSectionReposMon.findAllByCityCodeIsOrderByCityCodeAscIrCodeAsc(cityCode_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdLandSectionReposHist.findAllByCityCodeIsOrderByCityCodeAscIrCodeAsc(cityCode_0, pageable);
    else 
      slice = cdLandSectionRepos.findAllByCityCodeIsOrderByCityCodeAscIrCodeAsc(cityCode_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdLandSection> areaCodeEq(String cityCode_0, String areaCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdLandSection> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("areaCodeEq " + dbName + " : " + "cityCode_0 : " + cityCode_0 + " areaCode_1 : " +  areaCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdLandSectionReposDay.findAllByCityCodeIsAndAreaCodeIsOrderByCityCodeAscIrCodeAsc(cityCode_0, areaCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdLandSectionReposMon.findAllByCityCodeIsAndAreaCodeIsOrderByCityCodeAscIrCodeAsc(cityCode_0, areaCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdLandSectionReposHist.findAllByCityCodeIsAndAreaCodeIsOrderByCityCodeAscIrCodeAsc(cityCode_0, areaCode_1, pageable);
    else 
      slice = cdLandSectionRepos.findAllByCityCodeIsAndAreaCodeIsOrderByCityCodeAscIrCodeAsc(cityCode_0, areaCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdLandSection> landOfficeCodeEq(String landOfficeCode_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdLandSection> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("landOfficeCodeEq " + dbName + " : " + "landOfficeCode_0 : " + landOfficeCode_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdLandSectionReposDay.findAllByLandOfficeCodeIsOrderByCityCodeAscIrCodeAsc(landOfficeCode_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdLandSectionReposMon.findAllByLandOfficeCodeIsOrderByCityCodeAscIrCodeAsc(landOfficeCode_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdLandSectionReposHist.findAllByLandOfficeCodeIsOrderByCityCodeAscIrCodeAsc(landOfficeCode_0, pageable);
    else 
      slice = cdLandSectionRepos.findAllByLandOfficeCodeIsOrderByCityCodeAscIrCodeAsc(landOfficeCode_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdLandSection holdById(CdLandSectionId cdLandSectionId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdLandSectionId);
    Optional<CdLandSection> cdLandSection = null;
    if (dbName.equals(ContentName.onDay))
      cdLandSection = cdLandSectionReposDay.findByCdLandSectionId(cdLandSectionId);
    else if (dbName.equals(ContentName.onMon))
      cdLandSection = cdLandSectionReposMon.findByCdLandSectionId(cdLandSectionId);
    else if (dbName.equals(ContentName.onHist))
      cdLandSection = cdLandSectionReposHist.findByCdLandSectionId(cdLandSectionId);
    else 
      cdLandSection = cdLandSectionRepos.findByCdLandSectionId(cdLandSectionId);
    return cdLandSection.isPresent() ? cdLandSection.get() : null;
  }

  @Override
  public CdLandSection holdById(CdLandSection cdLandSection, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdLandSection.getCdLandSectionId());
    Optional<CdLandSection> cdLandSectionT = null;
    if (dbName.equals(ContentName.onDay))
      cdLandSectionT = cdLandSectionReposDay.findByCdLandSectionId(cdLandSection.getCdLandSectionId());
    else if (dbName.equals(ContentName.onMon))
      cdLandSectionT = cdLandSectionReposMon.findByCdLandSectionId(cdLandSection.getCdLandSectionId());
    else if (dbName.equals(ContentName.onHist))
      cdLandSectionT = cdLandSectionReposHist.findByCdLandSectionId(cdLandSection.getCdLandSectionId());
    else 
      cdLandSectionT = cdLandSectionRepos.findByCdLandSectionId(cdLandSection.getCdLandSectionId());
    return cdLandSectionT.isPresent() ? cdLandSectionT.get() : null;
  }

  @Override
  public CdLandSection insert(CdLandSection cdLandSection, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + cdLandSection.getCdLandSectionId());
    if (this.findById(cdLandSection.getCdLandSectionId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdLandSection.setCreateEmpNo(empNot);

    if(cdLandSection.getLastUpdateEmpNo() == null || cdLandSection.getLastUpdateEmpNo().isEmpty())
      cdLandSection.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdLandSectionReposDay.saveAndFlush(cdLandSection);	
    else if (dbName.equals(ContentName.onMon))
      return cdLandSectionReposMon.saveAndFlush(cdLandSection);
    else if (dbName.equals(ContentName.onHist))
      return cdLandSectionReposHist.saveAndFlush(cdLandSection);
    else 
    return cdLandSectionRepos.saveAndFlush(cdLandSection);
  }

  @Override
  public CdLandSection update(CdLandSection cdLandSection, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdLandSection.getCdLandSectionId());
    if (!empNot.isEmpty())
      cdLandSection.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdLandSectionReposDay.saveAndFlush(cdLandSection);	
    else if (dbName.equals(ContentName.onMon))
      return cdLandSectionReposMon.saveAndFlush(cdLandSection);
    else if (dbName.equals(ContentName.onHist))
      return cdLandSectionReposHist.saveAndFlush(cdLandSection);
    else 
    return cdLandSectionRepos.saveAndFlush(cdLandSection);
  }

  @Override
  public CdLandSection update2(CdLandSection cdLandSection, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdLandSection.getCdLandSectionId());
    if (!empNot.isEmpty())
      cdLandSection.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdLandSectionReposDay.saveAndFlush(cdLandSection);	
    else if (dbName.equals(ContentName.onMon))
      cdLandSectionReposMon.saveAndFlush(cdLandSection);
    else if (dbName.equals(ContentName.onHist))
        cdLandSectionReposHist.saveAndFlush(cdLandSection);
    else 
      cdLandSectionRepos.saveAndFlush(cdLandSection);	
    return this.findById(cdLandSection.getCdLandSectionId());
  }

  @Override
  public void delete(CdLandSection cdLandSection, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdLandSection.getCdLandSectionId());
    if (dbName.equals(ContentName.onDay)) {
      cdLandSectionReposDay.delete(cdLandSection);	
      cdLandSectionReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdLandSectionReposMon.delete(cdLandSection);	
      cdLandSectionReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdLandSectionReposHist.delete(cdLandSection);
      cdLandSectionReposHist.flush();
    }
    else {
      cdLandSectionRepos.delete(cdLandSection);
      cdLandSectionRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdLandSection> cdLandSection, TitaVo... titaVo) throws DBException {
    if (cdLandSection == null || cdLandSection.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CdLandSection t : cdLandSection){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdLandSection = cdLandSectionReposDay.saveAll(cdLandSection);	
      cdLandSectionReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdLandSection = cdLandSectionReposMon.saveAll(cdLandSection);	
      cdLandSectionReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdLandSection = cdLandSectionReposHist.saveAll(cdLandSection);
      cdLandSectionReposHist.flush();
    }
    else {
      cdLandSection = cdLandSectionRepos.saveAll(cdLandSection);
      cdLandSectionRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdLandSection> cdLandSection, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (cdLandSection == null || cdLandSection.size() == 0)
      throw new DBException(6);

    for (CdLandSection t : cdLandSection) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdLandSection = cdLandSectionReposDay.saveAll(cdLandSection);	
      cdLandSectionReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdLandSection = cdLandSectionReposMon.saveAll(cdLandSection);	
      cdLandSectionReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdLandSection = cdLandSectionReposHist.saveAll(cdLandSection);
      cdLandSectionReposHist.flush();
    }
    else {
      cdLandSection = cdLandSectionRepos.saveAll(cdLandSection);
      cdLandSectionRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdLandSection> cdLandSection, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdLandSection == null || cdLandSection.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdLandSectionReposDay.deleteAll(cdLandSection);	
      cdLandSectionReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdLandSectionReposMon.deleteAll(cdLandSection);	
      cdLandSectionReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdLandSectionReposHist.deleteAll(cdLandSection);
      cdLandSectionReposHist.flush();
    }
    else {
      cdLandSectionRepos.deleteAll(cdLandSection);
      cdLandSectionRepos.flush();
    }
  }

}
