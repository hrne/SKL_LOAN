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
import com.st1.itx.db.domain.PfDetail;
import com.st1.itx.db.repository.online.PfDetailRepository;
import com.st1.itx.db.repository.day.PfDetailRepositoryDay;
import com.st1.itx.db.repository.mon.PfDetailRepositoryMon;
import com.st1.itx.db.repository.hist.PfDetailRepositoryHist;
import com.st1.itx.db.service.PfDetailService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("pfDetailService")
@Repository
public class PfDetailServiceImpl extends ASpringJpaParm implements PfDetailService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private PfDetailRepository pfDetailRepos;

  @Autowired
  private PfDetailRepositoryDay pfDetailReposDay;

  @Autowired
  private PfDetailRepositoryMon pfDetailReposMon;

  @Autowired
  private PfDetailRepositoryHist pfDetailReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(pfDetailRepos);
    org.junit.Assert.assertNotNull(pfDetailReposDay);
    org.junit.Assert.assertNotNull(pfDetailReposMon);
    org.junit.Assert.assertNotNull(pfDetailReposHist);
  }

  @Override
  public PfDetail findById(Long logNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + logNo);
    Optional<PfDetail> pfDetail = null;
    if (dbName.equals(ContentName.onDay))
      pfDetail = pfDetailReposDay.findById(logNo);
    else if (dbName.equals(ContentName.onMon))
      pfDetail = pfDetailReposMon.findById(logNo);
    else if (dbName.equals(ContentName.onHist))
      pfDetail = pfDetailReposHist.findById(logNo);
    else 
      pfDetail = pfDetailRepos.findById(logNo);
    PfDetail obj = pfDetail.isPresent() ? pfDetail.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<PfDetail> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LogNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = pfDetailReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfDetailReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfDetailReposHist.findAll(pageable);
    else 
      slice = pfDetailRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfDetail> findByPerfDate(int perfDate_0, int perfDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByPerfDate " + dbName + " : " + "perfDate_0 : " + perfDate_0 + " perfDate_1 : " +  perfDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = pfDetailReposDay.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(perfDate_0, perfDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfDetailReposMon.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(perfDate_0, perfDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfDetailReposHist.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(perfDate_0, perfDate_1, pageable);
    else 
      slice = pfDetailRepos.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(perfDate_0, perfDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfDetail> findFacmNoRange(int custNo_0, int facmNo_1, int facmNo_2, int workMonth_3, int workMonth_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findFacmNoRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " facmNo_2 : " +  facmNo_2 + " workMonth_3 : " +  workMonth_3 + " workMonth_4 : " +  workMonth_4);
    if (dbName.equals(ContentName.onDay))
      slice = pfDetailReposDay.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAscBorxNoAsc(custNo_0, facmNo_1, facmNo_2, workMonth_3, workMonth_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfDetailReposMon.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAscBorxNoAsc(custNo_0, facmNo_1, facmNo_2, workMonth_3, workMonth_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfDetailReposHist.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAscBorxNoAsc(custNo_0, facmNo_1, facmNo_2, workMonth_3, workMonth_4, pageable);
    else 
      slice = pfDetailRepos.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAscBorxNoAsc(custNo_0, facmNo_1, facmNo_2, workMonth_3, workMonth_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfDetail> findByWorkMonth(int workMonth_0, int workMonth_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByWorkMonth " + dbName + " : " + "workMonth_0 : " + workMonth_0 + " workMonth_1 : " +  workMonth_1);
    if (dbName.equals(ContentName.onDay))
      slice = pfDetailReposDay.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, workMonth_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfDetailReposMon.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, workMonth_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfDetailReposHist.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, workMonth_1, pageable);
    else 
      slice = pfDetailRepos.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, workMonth_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfDetail> findByBorxNo(int custNo_0, int facmNo_1, int bormNo_2, int borxNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBorxNo " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2 + " borxNo_3 : " +  borxNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = pfDetailReposDay.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndBorxNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, bormNo_2, borxNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfDetailReposMon.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndBorxNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, bormNo_2, borxNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfDetailReposHist.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndBorxNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, bormNo_2, borxNo_3, pageable);
    else 
      slice = pfDetailRepos.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndBorxNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, bormNo_2, borxNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfDetail> FindByBormNo(int custNo_0, int facmNo_1, int bormNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("FindByBormNo " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = pfDetailReposDay.findAllByCustNoIsAndFacmNoIsAndBormNoIs(custNo_0, facmNo_1, bormNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfDetailReposMon.findAllByCustNoIsAndFacmNoIsAndBormNoIs(custNo_0, facmNo_1, bormNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfDetailReposHist.findAllByCustNoIsAndFacmNoIsAndBormNoIs(custNo_0, facmNo_1, bormNo_2, pageable);
    else 
      slice = pfDetailRepos.findAllByCustNoIsAndFacmNoIsAndBormNoIs(custNo_0, facmNo_1, bormNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PfDetail holdById(Long logNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + logNo);
    Optional<PfDetail> pfDetail = null;
    if (dbName.equals(ContentName.onDay))
      pfDetail = pfDetailReposDay.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onMon))
      pfDetail = pfDetailReposMon.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onHist))
      pfDetail = pfDetailReposHist.findByLogNo(logNo);
    else 
      pfDetail = pfDetailRepos.findByLogNo(logNo);
    return pfDetail.isPresent() ? pfDetail.get() : null;
  }

  @Override
  public PfDetail holdById(PfDetail pfDetail, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + pfDetail.getLogNo());
    Optional<PfDetail> pfDetailT = null;
    if (dbName.equals(ContentName.onDay))
      pfDetailT = pfDetailReposDay.findByLogNo(pfDetail.getLogNo());
    else if (dbName.equals(ContentName.onMon))
      pfDetailT = pfDetailReposMon.findByLogNo(pfDetail.getLogNo());
    else if (dbName.equals(ContentName.onHist))
      pfDetailT = pfDetailReposHist.findByLogNo(pfDetail.getLogNo());
    else 
      pfDetailT = pfDetailRepos.findByLogNo(pfDetail.getLogNo());
    return pfDetailT.isPresent() ? pfDetailT.get() : null;
  }

  @Override
  public PfDetail insert(PfDetail pfDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + pfDetail.getLogNo());
    if (this.findById(pfDetail.getLogNo()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      pfDetail.setCreateEmpNo(empNot);

    if(pfDetail.getLastUpdateEmpNo() == null || pfDetail.getLastUpdateEmpNo().isEmpty())
      pfDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfDetailReposDay.saveAndFlush(pfDetail);	
    else if (dbName.equals(ContentName.onMon))
      return pfDetailReposMon.saveAndFlush(pfDetail);
    else if (dbName.equals(ContentName.onHist))
      return pfDetailReposHist.saveAndFlush(pfDetail);
    else 
    return pfDetailRepos.saveAndFlush(pfDetail);
  }

  @Override
  public PfDetail update(PfDetail pfDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + pfDetail.getLogNo());
    if (!empNot.isEmpty())
      pfDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfDetailReposDay.saveAndFlush(pfDetail);	
    else if (dbName.equals(ContentName.onMon))
      return pfDetailReposMon.saveAndFlush(pfDetail);
    else if (dbName.equals(ContentName.onHist))
      return pfDetailReposHist.saveAndFlush(pfDetail);
    else 
    return pfDetailRepos.saveAndFlush(pfDetail);
  }

  @Override
  public PfDetail update2(PfDetail pfDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + pfDetail.getLogNo());
    if (!empNot.isEmpty())
      pfDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      pfDetailReposDay.saveAndFlush(pfDetail);	
    else if (dbName.equals(ContentName.onMon))
      pfDetailReposMon.saveAndFlush(pfDetail);
    else if (dbName.equals(ContentName.onHist))
        pfDetailReposHist.saveAndFlush(pfDetail);
    else 
      pfDetailRepos.saveAndFlush(pfDetail);	
    return this.findById(pfDetail.getLogNo());
  }

  @Override
  public void delete(PfDetail pfDetail, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + pfDetail.getLogNo());
    if (dbName.equals(ContentName.onDay)) {
      pfDetailReposDay.delete(pfDetail);	
      pfDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfDetailReposMon.delete(pfDetail);	
      pfDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfDetailReposHist.delete(pfDetail);
      pfDetailReposHist.flush();
    }
    else {
      pfDetailRepos.delete(pfDetail);
      pfDetailRepos.flush();
    }
   }

  @Override
  public void insertAll(List<PfDetail> pfDetail, TitaVo... titaVo) throws DBException {
    if (pfDetail == null || pfDetail.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (PfDetail t : pfDetail){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      pfDetail = pfDetailReposDay.saveAll(pfDetail);	
      pfDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfDetail = pfDetailReposMon.saveAll(pfDetail);	
      pfDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfDetail = pfDetailReposHist.saveAll(pfDetail);
      pfDetailReposHist.flush();
    }
    else {
      pfDetail = pfDetailRepos.saveAll(pfDetail);
      pfDetailRepos.flush();
    }
    }

  @Override
  public void updateAll(List<PfDetail> pfDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (pfDetail == null || pfDetail.size() == 0)
      throw new DBException(6);

    for (PfDetail t : pfDetail) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      pfDetail = pfDetailReposDay.saveAll(pfDetail);	
      pfDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfDetail = pfDetailReposMon.saveAll(pfDetail);	
      pfDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfDetail = pfDetailReposHist.saveAll(pfDetail);
      pfDetailReposHist.flush();
    }
    else {
      pfDetail = pfDetailRepos.saveAll(pfDetail);
      pfDetailRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<PfDetail> pfDetail, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (pfDetail == null || pfDetail.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      pfDetailReposDay.deleteAll(pfDetail);	
      pfDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfDetailReposMon.deleteAll(pfDetail);	
      pfDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfDetailReposHist.deleteAll(pfDetail);
      pfDetailReposHist.flush();
    }
    else {
      pfDetailRepos.deleteAll(pfDetail);
      pfDetailRepos.flush();
    }
  }

}
