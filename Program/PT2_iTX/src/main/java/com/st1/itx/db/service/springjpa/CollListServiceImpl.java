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
import com.st1.itx.db.domain.CollList;
import com.st1.itx.db.domain.CollListId;
import com.st1.itx.db.repository.online.CollListRepository;
import com.st1.itx.db.repository.day.CollListRepositoryDay;
import com.st1.itx.db.repository.mon.CollListRepositoryMon;
import com.st1.itx.db.repository.hist.CollListRepositoryHist;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("collListService")
@Repository
public class CollListServiceImpl extends ASpringJpaParm implements CollListService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CollListRepository collListRepos;

  @Autowired
  private CollListRepositoryDay collListReposDay;

  @Autowired
  private CollListRepositoryMon collListReposMon;

  @Autowired
  private CollListRepositoryHist collListReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(collListRepos);
    org.junit.Assert.assertNotNull(collListReposDay);
    org.junit.Assert.assertNotNull(collListReposMon);
    org.junit.Assert.assertNotNull(collListReposHist);
  }

  @Override
  public CollList findById(CollListId collListId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + collListId);
    Optional<CollList> collList = null;
    if (dbName.equals(ContentName.onDay))
      collList = collListReposDay.findById(collListId);
    else if (dbName.equals(ContentName.onMon))
      collList = collListReposMon.findById(collListId);
    else if (dbName.equals(ContentName.onHist))
      collList = collListReposHist.findById(collListId);
    else 
      collList = collListRepos.findById(collListId);
    CollList obj = collList.isPresent() ? collList.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CollList> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollList> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = collListReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collListReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collListReposHist.findAll(pageable);
    else 
      slice = collListRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CollList> findCl(int clCustNo_0, int clFacmNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollList> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCl " + dbName + " : " + "clCustNo_0 : " + clCustNo_0 + " clFacmNo_1 : " +  clFacmNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = collListReposDay.findAllByClCustNoIsAndClFacmNoIs(clCustNo_0, clFacmNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collListReposMon.findAllByClCustNoIsAndClFacmNoIs(clCustNo_0, clFacmNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collListReposHist.findAllByClCustNoIsAndClFacmNoIs(clCustNo_0, clFacmNo_1, pageable);
    else 
      slice = collListRepos.findAllByClCustNoIsAndClFacmNoIs(clCustNo_0, clFacmNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CollList> statusRng(int custNo_0, int custNo_1, int facmNo_2, int facmNo_3, List<Integer> status_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollList> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("statusRng " + dbName + " : " + "custNo_0 : " + custNo_0 + " custNo_1 : " +  custNo_1 + " facmNo_2 : " +  facmNo_2 + " facmNo_3 : " +  facmNo_3 + " status_4 : " +  status_4);
    if (dbName.equals(ContentName.onDay))
      slice = collListReposDay.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndStatusIn(custNo_0, custNo_1, facmNo_2, facmNo_3, status_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collListReposMon.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndStatusIn(custNo_0, custNo_1, facmNo_2, facmNo_3, status_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collListReposHist.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndStatusIn(custNo_0, custNo_1, facmNo_2, facmNo_3, status_4, pageable);
    else 
      slice = collListRepos.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndStatusIn(custNo_0, custNo_1, facmNo_2, facmNo_3, status_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CollList> ovduDaysRange(int ovduDays_0, int ovduDays_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollList> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ovduDaysRange " + dbName + " : " + "ovduDays_0 : " + ovduDays_0 + " ovduDays_1 : " +  ovduDays_1);
    if (dbName.equals(ContentName.onDay))
      slice = collListReposDay.findAllByOvduDaysGreaterThanEqualAndOvduDaysLessThanEqual(ovduDays_0, ovduDays_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collListReposMon.findAllByOvduDaysGreaterThanEqualAndOvduDaysLessThanEqual(ovduDays_0, ovduDays_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collListReposHist.findAllByOvduDaysGreaterThanEqualAndOvduDaysLessThanEqual(ovduDays_0, ovduDays_1, pageable);
    else 
      slice = collListRepos.findAllByOvduDaysGreaterThanEqualAndOvduDaysLessThanEqual(ovduDays_0, ovduDays_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CollList> findCityCode(String cityCode_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollList> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCityCode " + dbName + " : " + "cityCode_0 : " + cityCode_0);
    if (dbName.equals(ContentName.onDay))
      slice = collListReposDay.findAllByCityCodeIs(cityCode_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collListReposMon.findAllByCityCodeIs(cityCode_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collListReposHist.findAllByCityCodeIs(cityCode_0, pageable);
    else 
      slice = collListRepos.findAllByCityCodeIs(cityCode_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CollList holdById(CollListId collListId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + collListId);
    Optional<CollList> collList = null;
    if (dbName.equals(ContentName.onDay))
      collList = collListReposDay.findByCollListId(collListId);
    else if (dbName.equals(ContentName.onMon))
      collList = collListReposMon.findByCollListId(collListId);
    else if (dbName.equals(ContentName.onHist))
      collList = collListReposHist.findByCollListId(collListId);
    else 
      collList = collListRepos.findByCollListId(collListId);
    return collList.isPresent() ? collList.get() : null;
  }

  @Override
  public CollList holdById(CollList collList, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + collList.getCollListId());
    Optional<CollList> collListT = null;
    if (dbName.equals(ContentName.onDay))
      collListT = collListReposDay.findByCollListId(collList.getCollListId());
    else if (dbName.equals(ContentName.onMon))
      collListT = collListReposMon.findByCollListId(collList.getCollListId());
    else if (dbName.equals(ContentName.onHist))
      collListT = collListReposHist.findByCollListId(collList.getCollListId());
    else 
      collListT = collListRepos.findByCollListId(collList.getCollListId());
    return collListT.isPresent() ? collListT.get() : null;
  }

  @Override
  public CollList insert(CollList collList, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + collList.getCollListId());
    if (this.findById(collList.getCollListId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      collList.setCreateEmpNo(empNot);

    if(collList.getLastUpdateEmpNo() == null || collList.getLastUpdateEmpNo().isEmpty())
      collList.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return collListReposDay.saveAndFlush(collList);	
    else if (dbName.equals(ContentName.onMon))
      return collListReposMon.saveAndFlush(collList);
    else if (dbName.equals(ContentName.onHist))
      return collListReposHist.saveAndFlush(collList);
    else 
    return collListRepos.saveAndFlush(collList);
  }

  @Override
  public CollList update(CollList collList, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + collList.getCollListId());
    if (!empNot.isEmpty())
      collList.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return collListReposDay.saveAndFlush(collList);	
    else if (dbName.equals(ContentName.onMon))
      return collListReposMon.saveAndFlush(collList);
    else if (dbName.equals(ContentName.onHist))
      return collListReposHist.saveAndFlush(collList);
    else 
    return collListRepos.saveAndFlush(collList);
  }

  @Override
  public CollList update2(CollList collList, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + collList.getCollListId());
    if (!empNot.isEmpty())
      collList.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      collListReposDay.saveAndFlush(collList);	
    else if (dbName.equals(ContentName.onMon))
      collListReposMon.saveAndFlush(collList);
    else if (dbName.equals(ContentName.onHist))
        collListReposHist.saveAndFlush(collList);
    else 
      collListRepos.saveAndFlush(collList);	
    return this.findById(collList.getCollListId());
  }

  @Override
  public void delete(CollList collList, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + collList.getCollListId());
    if (dbName.equals(ContentName.onDay)) {
      collListReposDay.delete(collList);	
      collListReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collListReposMon.delete(collList);	
      collListReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collListReposHist.delete(collList);
      collListReposHist.flush();
    }
    else {
      collListRepos.delete(collList);
      collListRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CollList> collList, TitaVo... titaVo) throws DBException {
    if (collList == null || collList.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CollList t : collList){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      collList = collListReposDay.saveAll(collList);	
      collListReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collList = collListReposMon.saveAll(collList);	
      collListReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collList = collListReposHist.saveAll(collList);
      collListReposHist.flush();
    }
    else {
      collList = collListRepos.saveAll(collList);
      collListRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CollList> collList, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (collList == null || collList.size() == 0)
      throw new DBException(6);

    for (CollList t : collList) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      collList = collListReposDay.saveAll(collList);	
      collListReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collList = collListReposMon.saveAll(collList);	
      collListReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collList = collListReposHist.saveAll(collList);
      collListReposHist.flush();
    }
    else {
      collList = collListRepos.saveAll(collList);
      collListRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CollList> collList, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (collList == null || collList.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      collListReposDay.deleteAll(collList);	
      collListReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collListReposMon.deleteAll(collList);	
      collListReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collListReposHist.deleteAll(collList);
      collListReposHist.flush();
    }
    else {
      collListRepos.deleteAll(collList);
      collListRepos.flush();
    }
  }

  @Override
  public void Usp_L5_CollList_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      collListReposDay.uspL5ColllistUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      collListReposMon.uspL5ColllistUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      collListReposHist.uspL5ColllistUpd(tbsdyf,  empNo);
   else
      collListRepos.uspL5ColllistUpd(tbsdyf,  empNo);
  }

}
