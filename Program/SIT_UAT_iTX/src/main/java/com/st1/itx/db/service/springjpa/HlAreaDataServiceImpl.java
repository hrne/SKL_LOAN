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
import com.st1.itx.db.domain.HlAreaData;
import com.st1.itx.db.repository.online.HlAreaDataRepository;
import com.st1.itx.db.repository.day.HlAreaDataRepositoryDay;
import com.st1.itx.db.repository.mon.HlAreaDataRepositoryMon;
import com.st1.itx.db.repository.hist.HlAreaDataRepositoryHist;
import com.st1.itx.db.service.HlAreaDataService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("hlAreaDataService")
@Repository
public class HlAreaDataServiceImpl extends ASpringJpaParm implements HlAreaDataService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private HlAreaDataRepository hlAreaDataRepos;

  @Autowired
  private HlAreaDataRepositoryDay hlAreaDataReposDay;

  @Autowired
  private HlAreaDataRepositoryMon hlAreaDataReposMon;

  @Autowired
  private HlAreaDataRepositoryHist hlAreaDataReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(hlAreaDataRepos);
    org.junit.Assert.assertNotNull(hlAreaDataReposDay);
    org.junit.Assert.assertNotNull(hlAreaDataReposMon);
    org.junit.Assert.assertNotNull(hlAreaDataReposHist);
  }

  @Override
  public HlAreaData findById(String areaUnitNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + areaUnitNo);
    Optional<HlAreaData> hlAreaData = null;
    if (dbName.equals(ContentName.onDay))
      hlAreaData = hlAreaDataReposDay.findById(areaUnitNo);
    else if (dbName.equals(ContentName.onMon))
      hlAreaData = hlAreaDataReposMon.findById(areaUnitNo);
    else if (dbName.equals(ContentName.onHist))
      hlAreaData = hlAreaDataReposHist.findById(areaUnitNo);
    else 
      hlAreaData = hlAreaDataRepos.findById(areaUnitNo);
    HlAreaData obj = hlAreaData.isPresent() ? hlAreaData.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<HlAreaData> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<HlAreaData> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AreaUnitNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AreaUnitNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = hlAreaDataReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = hlAreaDataReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = hlAreaDataReposHist.findAll(pageable);
    else 
      slice = hlAreaDataRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<HlAreaData> FindAreaUnitNo(String areaUnitNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<HlAreaData> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("FindAreaUnitNo " + dbName + " : " + "areaUnitNo_0 : " + areaUnitNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = hlAreaDataReposDay.findAllByAreaUnitNoIsOrderByAreaUnitNoAsc(areaUnitNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = hlAreaDataReposMon.findAllByAreaUnitNoIsOrderByAreaUnitNoAsc(areaUnitNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = hlAreaDataReposHist.findAllByAreaUnitNoIsOrderByAreaUnitNoAsc(areaUnitNo_0, pageable);
    else 
      slice = hlAreaDataRepos.findAllByAreaUnitNoIsOrderByAreaUnitNoAsc(areaUnitNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<HlAreaData> FindAreaChiefEmpNo(String areaChiefEmpNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<HlAreaData> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("FindAreaChiefEmpNo " + dbName + " : " + "areaChiefEmpNo_0 : " + areaChiefEmpNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = hlAreaDataReposDay.findAllByAreaChiefEmpNoIsOrderByAreaUnitNoAsc(areaChiefEmpNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = hlAreaDataReposMon.findAllByAreaChiefEmpNoIsOrderByAreaUnitNoAsc(areaChiefEmpNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = hlAreaDataReposHist.findAllByAreaChiefEmpNoIsOrderByAreaUnitNoAsc(areaChiefEmpNo_0, pageable);
    else 
      slice = hlAreaDataRepos.findAllByAreaChiefEmpNoIsOrderByAreaUnitNoAsc(areaChiefEmpNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public HlAreaData holdById(String areaUnitNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + areaUnitNo);
    Optional<HlAreaData> hlAreaData = null;
    if (dbName.equals(ContentName.onDay))
      hlAreaData = hlAreaDataReposDay.findByAreaUnitNo(areaUnitNo);
    else if (dbName.equals(ContentName.onMon))
      hlAreaData = hlAreaDataReposMon.findByAreaUnitNo(areaUnitNo);
    else if (dbName.equals(ContentName.onHist))
      hlAreaData = hlAreaDataReposHist.findByAreaUnitNo(areaUnitNo);
    else 
      hlAreaData = hlAreaDataRepos.findByAreaUnitNo(areaUnitNo);
    return hlAreaData.isPresent() ? hlAreaData.get() : null;
  }

  @Override
  public HlAreaData holdById(HlAreaData hlAreaData, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + hlAreaData.getAreaUnitNo());
    Optional<HlAreaData> hlAreaDataT = null;
    if (dbName.equals(ContentName.onDay))
      hlAreaDataT = hlAreaDataReposDay.findByAreaUnitNo(hlAreaData.getAreaUnitNo());
    else if (dbName.equals(ContentName.onMon))
      hlAreaDataT = hlAreaDataReposMon.findByAreaUnitNo(hlAreaData.getAreaUnitNo());
    else if (dbName.equals(ContentName.onHist))
      hlAreaDataT = hlAreaDataReposHist.findByAreaUnitNo(hlAreaData.getAreaUnitNo());
    else 
      hlAreaDataT = hlAreaDataRepos.findByAreaUnitNo(hlAreaData.getAreaUnitNo());
    return hlAreaDataT.isPresent() ? hlAreaDataT.get() : null;
  }

  @Override
  public HlAreaData insert(HlAreaData hlAreaData, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + hlAreaData.getAreaUnitNo());
    if (this.findById(hlAreaData.getAreaUnitNo()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      hlAreaData.setCreateEmpNo(empNot);

    if(hlAreaData.getLastUpdateEmpNo() == null || hlAreaData.getLastUpdateEmpNo().isEmpty())
      hlAreaData.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return hlAreaDataReposDay.saveAndFlush(hlAreaData);	
    else if (dbName.equals(ContentName.onMon))
      return hlAreaDataReposMon.saveAndFlush(hlAreaData);
    else if (dbName.equals(ContentName.onHist))
      return hlAreaDataReposHist.saveAndFlush(hlAreaData);
    else 
    return hlAreaDataRepos.saveAndFlush(hlAreaData);
  }

  @Override
  public HlAreaData update(HlAreaData hlAreaData, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + hlAreaData.getAreaUnitNo());
    if (!empNot.isEmpty())
      hlAreaData.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return hlAreaDataReposDay.saveAndFlush(hlAreaData);	
    else if (dbName.equals(ContentName.onMon))
      return hlAreaDataReposMon.saveAndFlush(hlAreaData);
    else if (dbName.equals(ContentName.onHist))
      return hlAreaDataReposHist.saveAndFlush(hlAreaData);
    else 
    return hlAreaDataRepos.saveAndFlush(hlAreaData);
  }

  @Override
  public HlAreaData update2(HlAreaData hlAreaData, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + hlAreaData.getAreaUnitNo());
    if (!empNot.isEmpty())
      hlAreaData.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      hlAreaDataReposDay.saveAndFlush(hlAreaData);	
    else if (dbName.equals(ContentName.onMon))
      hlAreaDataReposMon.saveAndFlush(hlAreaData);
    else if (dbName.equals(ContentName.onHist))
        hlAreaDataReposHist.saveAndFlush(hlAreaData);
    else 
      hlAreaDataRepos.saveAndFlush(hlAreaData);	
    return this.findById(hlAreaData.getAreaUnitNo());
  }

  @Override
  public void delete(HlAreaData hlAreaData, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + hlAreaData.getAreaUnitNo());
    if (dbName.equals(ContentName.onDay)) {
      hlAreaDataReposDay.delete(hlAreaData);	
      hlAreaDataReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      hlAreaDataReposMon.delete(hlAreaData);	
      hlAreaDataReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      hlAreaDataReposHist.delete(hlAreaData);
      hlAreaDataReposHist.flush();
    }
    else {
      hlAreaDataRepos.delete(hlAreaData);
      hlAreaDataRepos.flush();
    }
   }

  @Override
  public void insertAll(List<HlAreaData> hlAreaData, TitaVo... titaVo) throws DBException {
    if (hlAreaData == null || hlAreaData.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (HlAreaData t : hlAreaData){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      hlAreaData = hlAreaDataReposDay.saveAll(hlAreaData);	
      hlAreaDataReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      hlAreaData = hlAreaDataReposMon.saveAll(hlAreaData);	
      hlAreaDataReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      hlAreaData = hlAreaDataReposHist.saveAll(hlAreaData);
      hlAreaDataReposHist.flush();
    }
    else {
      hlAreaData = hlAreaDataRepos.saveAll(hlAreaData);
      hlAreaDataRepos.flush();
    }
    }

  @Override
  public void updateAll(List<HlAreaData> hlAreaData, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (hlAreaData == null || hlAreaData.size() == 0)
      throw new DBException(6);

    for (HlAreaData t : hlAreaData) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      hlAreaData = hlAreaDataReposDay.saveAll(hlAreaData);	
      hlAreaDataReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      hlAreaData = hlAreaDataReposMon.saveAll(hlAreaData);	
      hlAreaDataReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      hlAreaData = hlAreaDataReposHist.saveAll(hlAreaData);
      hlAreaDataReposHist.flush();
    }
    else {
      hlAreaData = hlAreaDataRepos.saveAll(hlAreaData);
      hlAreaDataRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<HlAreaData> hlAreaData, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (hlAreaData == null || hlAreaData.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      hlAreaDataReposDay.deleteAll(hlAreaData);	
      hlAreaDataReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      hlAreaDataReposMon.deleteAll(hlAreaData);	
      hlAreaDataReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      hlAreaDataReposHist.deleteAll(hlAreaData);
      hlAreaDataReposHist.flush();
    }
    else {
      hlAreaDataRepos.deleteAll(hlAreaData);
      hlAreaDataRepos.flush();
    }
  }

}
