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
import com.st1.itx.db.domain.AcLoanInt;
import com.st1.itx.db.domain.AcLoanIntId;
import com.st1.itx.db.repository.online.AcLoanIntRepository;
import com.st1.itx.db.repository.day.AcLoanIntRepositoryDay;
import com.st1.itx.db.repository.mon.AcLoanIntRepositoryMon;
import com.st1.itx.db.repository.hist.AcLoanIntRepositoryHist;
import com.st1.itx.db.service.AcLoanIntService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("acLoanIntService")
@Repository
public class AcLoanIntServiceImpl implements AcLoanIntService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(AcLoanIntServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private AcLoanIntRepository acLoanIntRepos;

  @Autowired
  private AcLoanIntRepositoryDay acLoanIntReposDay;

  @Autowired
  private AcLoanIntRepositoryMon acLoanIntReposMon;

  @Autowired
  private AcLoanIntRepositoryHist acLoanIntReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(acLoanIntRepos);
    org.junit.Assert.assertNotNull(acLoanIntReposDay);
    org.junit.Assert.assertNotNull(acLoanIntReposMon);
    org.junit.Assert.assertNotNull(acLoanIntReposHist);
  }

  @Override
  public AcLoanInt findById(AcLoanIntId acLoanIntId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + acLoanIntId);
    Optional<AcLoanInt> acLoanInt = null;
    if (dbName.equals(ContentName.onDay))
      acLoanInt = acLoanIntReposDay.findById(acLoanIntId);
    else if (dbName.equals(ContentName.onMon))
      acLoanInt = acLoanIntReposMon.findById(acLoanIntId);
    else if (dbName.equals(ContentName.onHist))
      acLoanInt = acLoanIntReposHist.findById(acLoanIntId);
    else 
      acLoanInt = acLoanIntRepos.findById(acLoanIntId);
    AcLoanInt obj = acLoanInt.isPresent() ? acLoanInt.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<AcLoanInt> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcLoanInt> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "YearMonth", "CustNo", "FacmNo", "BormNo", "TermNo"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = acLoanIntReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acLoanIntReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acLoanIntReposHist.findAll(pageable);
    else 
      slice = acLoanIntRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcLoanInt> findYearMonthEq(int yearMonth_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcLoanInt> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findYearMonthEq " + dbName + " : " + "yearMonth_0 : " + yearMonth_0);
    if (dbName.equals(ContentName.onDay))
      slice = acLoanIntReposDay.findAllByYearMonthIsOrderByAcctCodeAscAgingAscAcBookCodeAscAcSubBookCodeAscCustNoAscFacmNoAscBormNoAsc(yearMonth_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acLoanIntReposMon.findAllByYearMonthIsOrderByAcctCodeAscAgingAscAcBookCodeAscAcSubBookCodeAscCustNoAscFacmNoAscBormNoAsc(yearMonth_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acLoanIntReposHist.findAllByYearMonthIsOrderByAcctCodeAscAgingAscAcBookCodeAscAcSubBookCodeAscCustNoAscFacmNoAscBormNoAsc(yearMonth_0, pageable);
    else 
      slice = acLoanIntRepos.findAllByYearMonthIsOrderByAcctCodeAscAgingAscAcBookCodeAscAcSubBookCodeAscCustNoAscFacmNoAscBormNoAsc(yearMonth_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AcLoanInt holdById(AcLoanIntId acLoanIntId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + acLoanIntId);
    Optional<AcLoanInt> acLoanInt = null;
    if (dbName.equals(ContentName.onDay))
      acLoanInt = acLoanIntReposDay.findByAcLoanIntId(acLoanIntId);
    else if (dbName.equals(ContentName.onMon))
      acLoanInt = acLoanIntReposMon.findByAcLoanIntId(acLoanIntId);
    else if (dbName.equals(ContentName.onHist))
      acLoanInt = acLoanIntReposHist.findByAcLoanIntId(acLoanIntId);
    else 
      acLoanInt = acLoanIntRepos.findByAcLoanIntId(acLoanIntId);
    return acLoanInt.isPresent() ? acLoanInt.get() : null;
  }

  @Override
  public AcLoanInt holdById(AcLoanInt acLoanInt, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + acLoanInt.getAcLoanIntId());
    Optional<AcLoanInt> acLoanIntT = null;
    if (dbName.equals(ContentName.onDay))
      acLoanIntT = acLoanIntReposDay.findByAcLoanIntId(acLoanInt.getAcLoanIntId());
    else if (dbName.equals(ContentName.onMon))
      acLoanIntT = acLoanIntReposMon.findByAcLoanIntId(acLoanInt.getAcLoanIntId());
    else if (dbName.equals(ContentName.onHist))
      acLoanIntT = acLoanIntReposHist.findByAcLoanIntId(acLoanInt.getAcLoanIntId());
    else 
      acLoanIntT = acLoanIntRepos.findByAcLoanIntId(acLoanInt.getAcLoanIntId());
    return acLoanIntT.isPresent() ? acLoanIntT.get() : null;
  }

  @Override
  public AcLoanInt insert(AcLoanInt acLoanInt, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + acLoanInt.getAcLoanIntId());
    if (this.findById(acLoanInt.getAcLoanIntId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      acLoanInt.setCreateEmpNo(empNot);

    if(acLoanInt.getLastUpdateEmpNo() == null || acLoanInt.getLastUpdateEmpNo().isEmpty())
      acLoanInt.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return acLoanIntReposDay.saveAndFlush(acLoanInt);	
    else if (dbName.equals(ContentName.onMon))
      return acLoanIntReposMon.saveAndFlush(acLoanInt);
    else if (dbName.equals(ContentName.onHist))
      return acLoanIntReposHist.saveAndFlush(acLoanInt);
    else 
    return acLoanIntRepos.saveAndFlush(acLoanInt);
  }

  @Override
  public AcLoanInt update(AcLoanInt acLoanInt, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + acLoanInt.getAcLoanIntId());
    if (!empNot.isEmpty())
      acLoanInt.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return acLoanIntReposDay.saveAndFlush(acLoanInt);	
    else if (dbName.equals(ContentName.onMon))
      return acLoanIntReposMon.saveAndFlush(acLoanInt);
    else if (dbName.equals(ContentName.onHist))
      return acLoanIntReposHist.saveAndFlush(acLoanInt);
    else 
    return acLoanIntRepos.saveAndFlush(acLoanInt);
  }

  @Override
  public AcLoanInt update2(AcLoanInt acLoanInt, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + acLoanInt.getAcLoanIntId());
    if (!empNot.isEmpty())
      acLoanInt.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      acLoanIntReposDay.saveAndFlush(acLoanInt);	
    else if (dbName.equals(ContentName.onMon))
      acLoanIntReposMon.saveAndFlush(acLoanInt);
    else if (dbName.equals(ContentName.onHist))
        acLoanIntReposHist.saveAndFlush(acLoanInt);
    else 
      acLoanIntRepos.saveAndFlush(acLoanInt);	
    return this.findById(acLoanInt.getAcLoanIntId());
  }

  @Override
  public void delete(AcLoanInt acLoanInt, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + acLoanInt.getAcLoanIntId());
    if (dbName.equals(ContentName.onDay)) {
      acLoanIntReposDay.delete(acLoanInt);	
      acLoanIntReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acLoanIntReposMon.delete(acLoanInt);	
      acLoanIntReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acLoanIntReposHist.delete(acLoanInt);
      acLoanIntReposHist.flush();
    }
    else {
      acLoanIntRepos.delete(acLoanInt);
      acLoanIntRepos.flush();
    }
   }

  @Override
  public void insertAll(List<AcLoanInt> acLoanInt, TitaVo... titaVo) throws DBException {
    if (acLoanInt == null || acLoanInt.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (AcLoanInt t : acLoanInt){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      acLoanInt = acLoanIntReposDay.saveAll(acLoanInt);	
      acLoanIntReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acLoanInt = acLoanIntReposMon.saveAll(acLoanInt);	
      acLoanIntReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acLoanInt = acLoanIntReposHist.saveAll(acLoanInt);
      acLoanIntReposHist.flush();
    }
    else {
      acLoanInt = acLoanIntRepos.saveAll(acLoanInt);
      acLoanIntRepos.flush();
    }
    }

  @Override
  public void updateAll(List<AcLoanInt> acLoanInt, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (acLoanInt == null || acLoanInt.size() == 0)
      throw new DBException(6);

    for (AcLoanInt t : acLoanInt) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      acLoanInt = acLoanIntReposDay.saveAll(acLoanInt);	
      acLoanIntReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acLoanInt = acLoanIntReposMon.saveAll(acLoanInt);	
      acLoanIntReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acLoanInt = acLoanIntReposHist.saveAll(acLoanInt);
      acLoanIntReposHist.flush();
    }
    else {
      acLoanInt = acLoanIntRepos.saveAll(acLoanInt);
      acLoanIntRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<AcLoanInt> acLoanInt, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (acLoanInt == null || acLoanInt.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      acLoanIntReposDay.deleteAll(acLoanInt);	
      acLoanIntReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acLoanIntReposMon.deleteAll(acLoanInt);	
      acLoanIntReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acLoanIntReposHist.deleteAll(acLoanInt);
      acLoanIntReposHist.flush();
    }
    else {
      acLoanIntRepos.deleteAll(acLoanInt);
      acLoanIntRepos.flush();
    }
  }

}
