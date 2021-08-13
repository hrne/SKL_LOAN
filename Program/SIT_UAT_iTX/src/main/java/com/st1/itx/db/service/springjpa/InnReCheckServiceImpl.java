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
import com.st1.itx.db.domain.InnReCheck;
import com.st1.itx.db.domain.InnReCheckId;
import com.st1.itx.db.repository.online.InnReCheckRepository;
import com.st1.itx.db.repository.day.InnReCheckRepositoryDay;
import com.st1.itx.db.repository.mon.InnReCheckRepositoryMon;
import com.st1.itx.db.repository.hist.InnReCheckRepositoryHist;
import com.st1.itx.db.service.InnReCheckService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("innReCheckService")
@Repository
public class InnReCheckServiceImpl implements InnReCheckService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(InnReCheckServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private InnReCheckRepository innReCheckRepos;

  @Autowired
  private InnReCheckRepositoryDay innReCheckReposDay;

  @Autowired
  private InnReCheckRepositoryMon innReCheckReposMon;

  @Autowired
  private InnReCheckRepositoryHist innReCheckReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(innReCheckRepos);
    org.junit.Assert.assertNotNull(innReCheckReposDay);
    org.junit.Assert.assertNotNull(innReCheckReposMon);
    org.junit.Assert.assertNotNull(innReCheckReposHist);
  }

  @Override
  public InnReCheck findById(InnReCheckId innReCheckId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + innReCheckId);
    Optional<InnReCheck> innReCheck = null;
    if (dbName.equals(ContentName.onDay))
      innReCheck = innReCheckReposDay.findById(innReCheckId);
    else if (dbName.equals(ContentName.onMon))
      innReCheck = innReCheckReposMon.findById(innReCheckId);
    else if (dbName.equals(ContentName.onHist))
      innReCheck = innReCheckReposHist.findById(innReCheckId);
    else 
      innReCheck = innReCheckRepos.findById(innReCheckId);
    InnReCheck obj = innReCheck.isPresent() ? innReCheck.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<InnReCheck> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InnReCheck> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "YearMonth", "ConditionCode", "CustNo", "FacmNo"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = innReCheckReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = innReCheckReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = innReCheckReposHist.findAll(pageable);
    else 
      slice = innReCheckRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InnReCheck> findCustNo(int yearMonth_0, int conditionCode_1, int custNo_2, int custNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InnReCheck> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findCustNo " + dbName + " : " + "yearMonth_0 : " + yearMonth_0 + " conditionCode_1 : " +  conditionCode_1 + " custNo_2 : " +  custNo_2 + " custNo_3 : " +  custNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = innReCheckReposDay.findAllByYearMonthIsAndConditionCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscFacmNoAsc(yearMonth_0, conditionCode_1, custNo_2, custNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = innReCheckReposMon.findAllByYearMonthIsAndConditionCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscFacmNoAsc(yearMonth_0, conditionCode_1, custNo_2, custNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = innReCheckReposHist.findAllByYearMonthIsAndConditionCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscFacmNoAsc(yearMonth_0, conditionCode_1, custNo_2, custNo_3, pageable);
    else 
      slice = innReCheckRepos.findAllByYearMonthIsAndConditionCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscFacmNoAsc(yearMonth_0, conditionCode_1, custNo_2, custNo_3, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InnReCheck> findYearMonth(int yearMonth_0, int yearMonth_1, int custNo_2, int custNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InnReCheck> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findYearMonth " + dbName + " : " + "yearMonth_0 : " + yearMonth_0 + " yearMonth_1 : " +  yearMonth_1 + " custNo_2 : " +  custNo_2 + " custNo_3 : " +  custNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = innReCheckReposDay.findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByYearMonthAscConditionCodeAscCustNoAscFacmNoAsc(yearMonth_0, yearMonth_1, custNo_2, custNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = innReCheckReposMon.findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByYearMonthAscConditionCodeAscCustNoAscFacmNoAsc(yearMonth_0, yearMonth_1, custNo_2, custNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = innReCheckReposHist.findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByYearMonthAscConditionCodeAscCustNoAscFacmNoAsc(yearMonth_0, yearMonth_1, custNo_2, custNo_3, pageable);
    else 
      slice = innReCheckRepos.findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByYearMonthAscConditionCodeAscCustNoAscFacmNoAsc(yearMonth_0, yearMonth_1, custNo_2, custNo_3, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public InnReCheck holdById(InnReCheckId innReCheckId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + innReCheckId);
    Optional<InnReCheck> innReCheck = null;
    if (dbName.equals(ContentName.onDay))
      innReCheck = innReCheckReposDay.findByInnReCheckId(innReCheckId);
    else if (dbName.equals(ContentName.onMon))
      innReCheck = innReCheckReposMon.findByInnReCheckId(innReCheckId);
    else if (dbName.equals(ContentName.onHist))
      innReCheck = innReCheckReposHist.findByInnReCheckId(innReCheckId);
    else 
      innReCheck = innReCheckRepos.findByInnReCheckId(innReCheckId);
    return innReCheck.isPresent() ? innReCheck.get() : null;
  }

  @Override
  public InnReCheck holdById(InnReCheck innReCheck, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + innReCheck.getInnReCheckId());
    Optional<InnReCheck> innReCheckT = null;
    if (dbName.equals(ContentName.onDay))
      innReCheckT = innReCheckReposDay.findByInnReCheckId(innReCheck.getInnReCheckId());
    else if (dbName.equals(ContentName.onMon))
      innReCheckT = innReCheckReposMon.findByInnReCheckId(innReCheck.getInnReCheckId());
    else if (dbName.equals(ContentName.onHist))
      innReCheckT = innReCheckReposHist.findByInnReCheckId(innReCheck.getInnReCheckId());
    else 
      innReCheckT = innReCheckRepos.findByInnReCheckId(innReCheck.getInnReCheckId());
    return innReCheckT.isPresent() ? innReCheckT.get() : null;
  }

  @Override
  public InnReCheck insert(InnReCheck innReCheck, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Insert..." + dbName + " " + innReCheck.getInnReCheckId());
    if (this.findById(innReCheck.getInnReCheckId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      innReCheck.setCreateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return innReCheckReposDay.saveAndFlush(innReCheck);	
    else if (dbName.equals(ContentName.onMon))
      return innReCheckReposMon.saveAndFlush(innReCheck);
    else if (dbName.equals(ContentName.onHist))
      return innReCheckReposHist.saveAndFlush(innReCheck);
    else 
    return innReCheckRepos.saveAndFlush(innReCheck);
  }

  @Override
  public InnReCheck update(InnReCheck innReCheck, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + innReCheck.getInnReCheckId());
    if (!empNot.isEmpty())
      innReCheck.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return innReCheckReposDay.saveAndFlush(innReCheck);	
    else if (dbName.equals(ContentName.onMon))
      return innReCheckReposMon.saveAndFlush(innReCheck);
    else if (dbName.equals(ContentName.onHist))
      return innReCheckReposHist.saveAndFlush(innReCheck);
    else 
    return innReCheckRepos.saveAndFlush(innReCheck);
  }

  @Override
  public InnReCheck update2(InnReCheck innReCheck, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + innReCheck.getInnReCheckId());
    if (!empNot.isEmpty())
      innReCheck.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      innReCheckReposDay.saveAndFlush(innReCheck);	
    else if (dbName.equals(ContentName.onMon))
      innReCheckReposMon.saveAndFlush(innReCheck);
    else if (dbName.equals(ContentName.onHist))
        innReCheckReposHist.saveAndFlush(innReCheck);
    else 
      innReCheckRepos.saveAndFlush(innReCheck);	
    return this.findById(innReCheck.getInnReCheckId());
  }

  @Override
  public void delete(InnReCheck innReCheck, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + innReCheck.getInnReCheckId());
    if (dbName.equals(ContentName.onDay)) {
      innReCheckReposDay.delete(innReCheck);	
      innReCheckReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      innReCheckReposMon.delete(innReCheck);	
      innReCheckReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      innReCheckReposHist.delete(innReCheck);
      innReCheckReposHist.flush();
    }
    else {
      innReCheckRepos.delete(innReCheck);
      innReCheckRepos.flush();
    }
   }

  @Override
  public void insertAll(List<InnReCheck> innReCheck, TitaVo... titaVo) throws DBException {
    if (innReCheck == null || innReCheck.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}    logger.info("InsertAll...");
    for (InnReCheck t : innReCheck) 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      innReCheck = innReCheckReposDay.saveAll(innReCheck);	
      innReCheckReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      innReCheck = innReCheckReposMon.saveAll(innReCheck);	
      innReCheckReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      innReCheck = innReCheckReposHist.saveAll(innReCheck);
      innReCheckReposHist.flush();
    }
    else {
      innReCheck = innReCheckRepos.saveAll(innReCheck);
      innReCheckRepos.flush();
    }
    }

  @Override
  public void updateAll(List<InnReCheck> innReCheck, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (innReCheck == null || innReCheck.size() == 0)
      throw new DBException(6);

    for (InnReCheck t : innReCheck) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      innReCheck = innReCheckReposDay.saveAll(innReCheck);	
      innReCheckReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      innReCheck = innReCheckReposMon.saveAll(innReCheck);	
      innReCheckReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      innReCheck = innReCheckReposHist.saveAll(innReCheck);
      innReCheckReposHist.flush();
    }
    else {
      innReCheck = innReCheckRepos.saveAll(innReCheck);
      innReCheckRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<InnReCheck> innReCheck, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (innReCheck == null || innReCheck.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      innReCheckReposDay.deleteAll(innReCheck);	
      innReCheckReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      innReCheckReposMon.deleteAll(innReCheck);	
      innReCheckReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      innReCheckReposHist.deleteAll(innReCheck);
      innReCheckReposHist.flush();
    }
    else {
      innReCheckRepos.deleteAll(innReCheck);
      innReCheckRepos.flush();
    }
  }

  @Override
  public void Usp_L5_InnReCheck_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      innReCheckReposDay.uspL5InnrecheckUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      innReCheckReposMon.uspL5InnrecheckUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      innReCheckReposHist.uspL5InnrecheckUpd(tbsdyf,  empNo);
   else
      innReCheckRepos.uspL5InnrecheckUpd(tbsdyf,  empNo);
  }

}
