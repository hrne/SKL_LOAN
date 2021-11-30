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
import com.st1.itx.db.domain.FacShareAppl;
import com.st1.itx.db.repository.online.FacShareApplRepository;
import com.st1.itx.db.repository.day.FacShareApplRepositoryDay;
import com.st1.itx.db.repository.mon.FacShareApplRepositoryMon;
import com.st1.itx.db.repository.hist.FacShareApplRepositoryHist;
import com.st1.itx.db.service.FacShareApplService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("facShareApplService")
@Repository
public class FacShareApplServiceImpl extends ASpringJpaParm implements FacShareApplService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private FacShareApplRepository facShareApplRepos;

  @Autowired
  private FacShareApplRepositoryDay facShareApplReposDay;

  @Autowired
  private FacShareApplRepositoryMon facShareApplReposMon;

  @Autowired
  private FacShareApplRepositoryHist facShareApplReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(facShareApplRepos);
    org.junit.Assert.assertNotNull(facShareApplReposDay);
    org.junit.Assert.assertNotNull(facShareApplReposMon);
    org.junit.Assert.assertNotNull(facShareApplReposHist);
  }

  @Override
  public FacShareAppl findById(int applNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + applNo);
    Optional<FacShareAppl> facShareAppl = null;
    if (dbName.equals(ContentName.onDay))
      facShareAppl = facShareApplReposDay.findById(applNo);
    else if (dbName.equals(ContentName.onMon))
      facShareAppl = facShareApplReposMon.findById(applNo);
    else if (dbName.equals(ContentName.onHist))
      facShareAppl = facShareApplReposHist.findById(applNo);
    else 
      facShareAppl = facShareApplRepos.findById(applNo);
    FacShareAppl obj = facShareAppl.isPresent() ? facShareAppl.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<FacShareAppl> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacShareAppl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ApplNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ApplNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = facShareApplReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facShareApplReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facShareApplReposHist.findAll(pageable);
    else 
      slice = facShareApplRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<FacShareAppl> findMainApplNo(int mainApplNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacShareAppl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findMainApplNo " + dbName + " : " + "mainApplNo_0 : " + mainApplNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = facShareApplReposDay.findAllByMainApplNoIsOrderByKeyinSeqAsc(mainApplNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facShareApplReposMon.findAllByMainApplNoIsOrderByKeyinSeqAsc(mainApplNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facShareApplReposHist.findAllByMainApplNoIsOrderByKeyinSeqAsc(mainApplNo_0, pageable);
    else 
      slice = facShareApplRepos.findAllByMainApplNoIsOrderByKeyinSeqAsc(mainApplNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<FacShareAppl> findCustNoEq(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacShareAppl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCustNoEq " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = facShareApplReposDay.findAllByCustNoIsOrderByMainApplNoAscKeyinSeqAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facShareApplReposMon.findAllByCustNoIsOrderByMainApplNoAscKeyinSeqAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facShareApplReposHist.findAllByCustNoIsOrderByMainApplNoAscKeyinSeqAsc(custNo_0, pageable);
    else 
      slice = facShareApplRepos.findAllByCustNoIsOrderByMainApplNoAscKeyinSeqAsc(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public FacShareAppl mApplNoFirst(int mainApplNo_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("mApplNoFirst " + dbName + " : " + "mainApplNo_0 : " + mainApplNo_0);
    Optional<FacShareAppl> facShareApplT = null;
    if (dbName.equals(ContentName.onDay))
      facShareApplT = facShareApplReposDay.findTopByMainApplNoIsOrderByKeyinSeqDesc(mainApplNo_0);
    else if (dbName.equals(ContentName.onMon))
      facShareApplT = facShareApplReposMon.findTopByMainApplNoIsOrderByKeyinSeqDesc(mainApplNo_0);
    else if (dbName.equals(ContentName.onHist))
      facShareApplT = facShareApplReposHist.findTopByMainApplNoIsOrderByKeyinSeqDesc(mainApplNo_0);
    else 
      facShareApplT = facShareApplRepos.findTopByMainApplNoIsOrderByKeyinSeqDesc(mainApplNo_0);

    return facShareApplT.isPresent() ? facShareApplT.get() : null;
  }

  @Override
  public FacShareAppl holdById(int applNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + applNo);
    Optional<FacShareAppl> facShareAppl = null;
    if (dbName.equals(ContentName.onDay))
      facShareAppl = facShareApplReposDay.findByApplNo(applNo);
    else if (dbName.equals(ContentName.onMon))
      facShareAppl = facShareApplReposMon.findByApplNo(applNo);
    else if (dbName.equals(ContentName.onHist))
      facShareAppl = facShareApplReposHist.findByApplNo(applNo);
    else 
      facShareAppl = facShareApplRepos.findByApplNo(applNo);
    return facShareAppl.isPresent() ? facShareAppl.get() : null;
  }

  @Override
  public FacShareAppl holdById(FacShareAppl facShareAppl, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + facShareAppl.getApplNo());
    Optional<FacShareAppl> facShareApplT = null;
    if (dbName.equals(ContentName.onDay))
      facShareApplT = facShareApplReposDay.findByApplNo(facShareAppl.getApplNo());
    else if (dbName.equals(ContentName.onMon))
      facShareApplT = facShareApplReposMon.findByApplNo(facShareAppl.getApplNo());
    else if (dbName.equals(ContentName.onHist))
      facShareApplT = facShareApplReposHist.findByApplNo(facShareAppl.getApplNo());
    else 
      facShareApplT = facShareApplRepos.findByApplNo(facShareAppl.getApplNo());
    return facShareApplT.isPresent() ? facShareApplT.get() : null;
  }

  @Override
  public FacShareAppl insert(FacShareAppl facShareAppl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + facShareAppl.getApplNo());
    if (this.findById(facShareAppl.getApplNo()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      facShareAppl.setCreateEmpNo(empNot);

    if(facShareAppl.getLastUpdateEmpNo() == null || facShareAppl.getLastUpdateEmpNo().isEmpty())
      facShareAppl.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return facShareApplReposDay.saveAndFlush(facShareAppl);	
    else if (dbName.equals(ContentName.onMon))
      return facShareApplReposMon.saveAndFlush(facShareAppl);
    else if (dbName.equals(ContentName.onHist))
      return facShareApplReposHist.saveAndFlush(facShareAppl);
    else 
    return facShareApplRepos.saveAndFlush(facShareAppl);
  }

  @Override
  public FacShareAppl update(FacShareAppl facShareAppl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + facShareAppl.getApplNo());
    if (!empNot.isEmpty())
      facShareAppl.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return facShareApplReposDay.saveAndFlush(facShareAppl);	
    else if (dbName.equals(ContentName.onMon))
      return facShareApplReposMon.saveAndFlush(facShareAppl);
    else if (dbName.equals(ContentName.onHist))
      return facShareApplReposHist.saveAndFlush(facShareAppl);
    else 
    return facShareApplRepos.saveAndFlush(facShareAppl);
  }

  @Override
  public FacShareAppl update2(FacShareAppl facShareAppl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + facShareAppl.getApplNo());
    if (!empNot.isEmpty())
      facShareAppl.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      facShareApplReposDay.saveAndFlush(facShareAppl);	
    else if (dbName.equals(ContentName.onMon))
      facShareApplReposMon.saveAndFlush(facShareAppl);
    else if (dbName.equals(ContentName.onHist))
        facShareApplReposHist.saveAndFlush(facShareAppl);
    else 
      facShareApplRepos.saveAndFlush(facShareAppl);	
    return this.findById(facShareAppl.getApplNo());
  }

  @Override
  public void delete(FacShareAppl facShareAppl, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + facShareAppl.getApplNo());
    if (dbName.equals(ContentName.onDay)) {
      facShareApplReposDay.delete(facShareAppl);	
      facShareApplReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facShareApplReposMon.delete(facShareAppl);	
      facShareApplReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facShareApplReposHist.delete(facShareAppl);
      facShareApplReposHist.flush();
    }
    else {
      facShareApplRepos.delete(facShareAppl);
      facShareApplRepos.flush();
    }
   }

  @Override
  public void insertAll(List<FacShareAppl> facShareAppl, TitaVo... titaVo) throws DBException {
    if (facShareAppl == null || facShareAppl.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (FacShareAppl t : facShareAppl){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      facShareAppl = facShareApplReposDay.saveAll(facShareAppl);	
      facShareApplReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facShareAppl = facShareApplReposMon.saveAll(facShareAppl);	
      facShareApplReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facShareAppl = facShareApplReposHist.saveAll(facShareAppl);
      facShareApplReposHist.flush();
    }
    else {
      facShareAppl = facShareApplRepos.saveAll(facShareAppl);
      facShareApplRepos.flush();
    }
    }

  @Override
  public void updateAll(List<FacShareAppl> facShareAppl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (facShareAppl == null || facShareAppl.size() == 0)
      throw new DBException(6);

    for (FacShareAppl t : facShareAppl) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      facShareAppl = facShareApplReposDay.saveAll(facShareAppl);	
      facShareApplReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facShareAppl = facShareApplReposMon.saveAll(facShareAppl);	
      facShareApplReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facShareAppl = facShareApplReposHist.saveAll(facShareAppl);
      facShareApplReposHist.flush();
    }
    else {
      facShareAppl = facShareApplRepos.saveAll(facShareAppl);
      facShareApplRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<FacShareAppl> facShareAppl, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (facShareAppl == null || facShareAppl.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      facShareApplReposDay.deleteAll(facShareAppl);	
      facShareApplReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facShareApplReposMon.deleteAll(facShareAppl);	
      facShareApplReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facShareApplReposHist.deleteAll(facShareAppl);
      facShareApplReposHist.flush();
    }
    else {
      facShareApplRepos.deleteAll(facShareAppl);
      facShareApplRepos.flush();
    }
  }

}
