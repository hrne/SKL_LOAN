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
import com.st1.itx.db.domain.EmpDeductSchedule;
import com.st1.itx.db.domain.EmpDeductScheduleId;
import com.st1.itx.db.repository.online.EmpDeductScheduleRepository;
import com.st1.itx.db.repository.day.EmpDeductScheduleRepositoryDay;
import com.st1.itx.db.repository.mon.EmpDeductScheduleRepositoryMon;
import com.st1.itx.db.repository.hist.EmpDeductScheduleRepositoryHist;
import com.st1.itx.db.service.EmpDeductScheduleService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("empDeductScheduleService")
@Repository
public class EmpDeductScheduleServiceImpl implements EmpDeductScheduleService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(EmpDeductScheduleServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private EmpDeductScheduleRepository empDeductScheduleRepos;

  @Autowired
  private EmpDeductScheduleRepositoryDay empDeductScheduleReposDay;

  @Autowired
  private EmpDeductScheduleRepositoryMon empDeductScheduleReposMon;

  @Autowired
  private EmpDeductScheduleRepositoryHist empDeductScheduleReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(empDeductScheduleRepos);
    org.junit.Assert.assertNotNull(empDeductScheduleReposDay);
    org.junit.Assert.assertNotNull(empDeductScheduleReposMon);
    org.junit.Assert.assertNotNull(empDeductScheduleReposHist);
  }

  @Override
  public EmpDeductSchedule findById(EmpDeductScheduleId empDeductScheduleId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + empDeductScheduleId);
    Optional<EmpDeductSchedule> empDeductSchedule = null;
    if (dbName.equals(ContentName.onDay))
      empDeductSchedule = empDeductScheduleReposDay.findById(empDeductScheduleId);
    else if (dbName.equals(ContentName.onMon))
      empDeductSchedule = empDeductScheduleReposMon.findById(empDeductScheduleId);
    else if (dbName.equals(ContentName.onHist))
      empDeductSchedule = empDeductScheduleReposHist.findById(empDeductScheduleId);
    else 
      empDeductSchedule = empDeductScheduleRepos.findById(empDeductScheduleId);
    EmpDeductSchedule obj = empDeductSchedule.isPresent() ? empDeductSchedule.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<EmpDeductSchedule> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<EmpDeductSchedule> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "WorkMonth", "AgType1"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = empDeductScheduleReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = empDeductScheduleReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = empDeductScheduleReposHist.findAll(pageable);
    else 
      slice = empDeductScheduleRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<EmpDeductSchedule> monthEqual(int workMonth_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<EmpDeductSchedule> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("monthEqual " + dbName + " : " + "workMonth_0 : " + workMonth_0);
    if (dbName.equals(ContentName.onDay))
      slice = empDeductScheduleReposDay.findAllByWorkMonthIs(workMonth_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = empDeductScheduleReposMon.findAllByWorkMonthIs(workMonth_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = empDeductScheduleReposHist.findAllByWorkMonthIs(workMonth_0, pageable);
    else 
      slice = empDeductScheduleRepos.findAllByWorkMonthIs(workMonth_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<EmpDeductSchedule> agType1Equal(String agType1_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<EmpDeductSchedule> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("agType1Equal " + dbName + " : " + "agType1_0 : " + agType1_0);
    if (dbName.equals(ContentName.onDay))
      slice = empDeductScheduleReposDay.findAllByAgType1Is(agType1_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = empDeductScheduleReposMon.findAllByAgType1Is(agType1_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = empDeductScheduleReposHist.findAllByAgType1Is(agType1_0, pageable);
    else 
      slice = empDeductScheduleRepos.findAllByAgType1Is(agType1_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<EmpDeductSchedule> entryDateRange(int entryDate_0, int entryDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<EmpDeductSchedule> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("entryDateRange " + dbName + " : " + "entryDate_0 : " + entryDate_0 + " entryDate_1 : " +  entryDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = empDeductScheduleReposDay.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqual(entryDate_0, entryDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = empDeductScheduleReposMon.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqual(entryDate_0, entryDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = empDeductScheduleReposHist.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqual(entryDate_0, entryDate_1, pageable);
    else 
      slice = empDeductScheduleRepos.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqual(entryDate_0, entryDate_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<EmpDeductSchedule> mediaDateRange(int mediaDate_0, int mediaDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<EmpDeductSchedule> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("mediaDateRange " + dbName + " : " + "mediaDate_0 : " + mediaDate_0 + " mediaDate_1 : " +  mediaDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = empDeductScheduleReposDay.findAllByMediaDateGreaterThanEqualAndMediaDateLessThanEqualOrderByAgType1Asc(mediaDate_0, mediaDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = empDeductScheduleReposMon.findAllByMediaDateGreaterThanEqualAndMediaDateLessThanEqualOrderByAgType1Asc(mediaDate_0, mediaDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = empDeductScheduleReposHist.findAllByMediaDateGreaterThanEqualAndMediaDateLessThanEqualOrderByAgType1Asc(mediaDate_0, mediaDate_1, pageable);
    else 
      slice = empDeductScheduleRepos.findAllByMediaDateGreaterThanEqualAndMediaDateLessThanEqualOrderByAgType1Asc(mediaDate_0, mediaDate_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<EmpDeductSchedule> monthRange(int workMonth_0, int workMonth_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<EmpDeductSchedule> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("monthRange " + dbName + " : " + "workMonth_0 : " + workMonth_0 + " workMonth_1 : " +  workMonth_1);
    if (dbName.equals(ContentName.onDay))
      slice = empDeductScheduleReposDay.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByWorkMonthAscAgType1Asc(workMonth_0, workMonth_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = empDeductScheduleReposMon.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByWorkMonthAscAgType1Asc(workMonth_0, workMonth_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = empDeductScheduleReposHist.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByWorkMonthAscAgType1Asc(workMonth_0, workMonth_1, pageable);
    else 
      slice = empDeductScheduleRepos.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByWorkMonthAscAgType1Asc(workMonth_0, workMonth_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<EmpDeductSchedule> findL4R15A(int workMonth_0, int workMonth_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<EmpDeductSchedule> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findL4R15A " + dbName + " : " + "workMonth_0 : " + workMonth_0 + " workMonth_1 : " +  workMonth_1);
    if (dbName.equals(ContentName.onDay))
      slice = empDeductScheduleReposDay.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByAgType1AscWorkMonthAsc(workMonth_0, workMonth_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = empDeductScheduleReposMon.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByAgType1AscWorkMonthAsc(workMonth_0, workMonth_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = empDeductScheduleReposHist.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByAgType1AscWorkMonthAsc(workMonth_0, workMonth_1, pageable);
    else 
      slice = empDeductScheduleRepos.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByAgType1AscWorkMonthAsc(workMonth_0, workMonth_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<EmpDeductSchedule> findL4R15B(int workMonth_0, int workMonth_1, String agType1_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<EmpDeductSchedule> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findL4R15B " + dbName + " : " + "workMonth_0 : " + workMonth_0 + " workMonth_1 : " +  workMonth_1 + " agType1_2 : " +  agType1_2);
    if (dbName.equals(ContentName.onDay))
      slice = empDeductScheduleReposDay.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualAndAgType1IsOrderByAgType1AscWorkMonthAsc(workMonth_0, workMonth_1, agType1_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = empDeductScheduleReposMon.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualAndAgType1IsOrderByAgType1AscWorkMonthAsc(workMonth_0, workMonth_1, agType1_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = empDeductScheduleReposHist.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualAndAgType1IsOrderByAgType1AscWorkMonthAsc(workMonth_0, workMonth_1, agType1_2, pageable);
    else 
      slice = empDeductScheduleRepos.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualAndAgType1IsOrderByAgType1AscWorkMonthAsc(workMonth_0, workMonth_1, agType1_2, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public EmpDeductSchedule holdById(EmpDeductScheduleId empDeductScheduleId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + empDeductScheduleId);
    Optional<EmpDeductSchedule> empDeductSchedule = null;
    if (dbName.equals(ContentName.onDay))
      empDeductSchedule = empDeductScheduleReposDay.findByEmpDeductScheduleId(empDeductScheduleId);
    else if (dbName.equals(ContentName.onMon))
      empDeductSchedule = empDeductScheduleReposMon.findByEmpDeductScheduleId(empDeductScheduleId);
    else if (dbName.equals(ContentName.onHist))
      empDeductSchedule = empDeductScheduleReposHist.findByEmpDeductScheduleId(empDeductScheduleId);
    else 
      empDeductSchedule = empDeductScheduleRepos.findByEmpDeductScheduleId(empDeductScheduleId);
    return empDeductSchedule.isPresent() ? empDeductSchedule.get() : null;
  }

  @Override
  public EmpDeductSchedule holdById(EmpDeductSchedule empDeductSchedule, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + empDeductSchedule.getEmpDeductScheduleId());
    Optional<EmpDeductSchedule> empDeductScheduleT = null;
    if (dbName.equals(ContentName.onDay))
      empDeductScheduleT = empDeductScheduleReposDay.findByEmpDeductScheduleId(empDeductSchedule.getEmpDeductScheduleId());
    else if (dbName.equals(ContentName.onMon))
      empDeductScheduleT = empDeductScheduleReposMon.findByEmpDeductScheduleId(empDeductSchedule.getEmpDeductScheduleId());
    else if (dbName.equals(ContentName.onHist))
      empDeductScheduleT = empDeductScheduleReposHist.findByEmpDeductScheduleId(empDeductSchedule.getEmpDeductScheduleId());
    else 
      empDeductScheduleT = empDeductScheduleRepos.findByEmpDeductScheduleId(empDeductSchedule.getEmpDeductScheduleId());
    return empDeductScheduleT.isPresent() ? empDeductScheduleT.get() : null;
  }

  @Override
  public EmpDeductSchedule insert(EmpDeductSchedule empDeductSchedule, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + empDeductSchedule.getEmpDeductScheduleId());
    if (this.findById(empDeductSchedule.getEmpDeductScheduleId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      empDeductSchedule.setCreateEmpNo(empNot);

    if(empDeductSchedule.getLastUpdateEmpNo() == null || empDeductSchedule.getLastUpdateEmpNo().isEmpty())
      empDeductSchedule.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return empDeductScheduleReposDay.saveAndFlush(empDeductSchedule);	
    else if (dbName.equals(ContentName.onMon))
      return empDeductScheduleReposMon.saveAndFlush(empDeductSchedule);
    else if (dbName.equals(ContentName.onHist))
      return empDeductScheduleReposHist.saveAndFlush(empDeductSchedule);
    else 
    return empDeductScheduleRepos.saveAndFlush(empDeductSchedule);
  }

  @Override
  public EmpDeductSchedule update(EmpDeductSchedule empDeductSchedule, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + empDeductSchedule.getEmpDeductScheduleId());
    if (!empNot.isEmpty())
      empDeductSchedule.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return empDeductScheduleReposDay.saveAndFlush(empDeductSchedule);	
    else if (dbName.equals(ContentName.onMon))
      return empDeductScheduleReposMon.saveAndFlush(empDeductSchedule);
    else if (dbName.equals(ContentName.onHist))
      return empDeductScheduleReposHist.saveAndFlush(empDeductSchedule);
    else 
    return empDeductScheduleRepos.saveAndFlush(empDeductSchedule);
  }

  @Override
  public EmpDeductSchedule update2(EmpDeductSchedule empDeductSchedule, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + empDeductSchedule.getEmpDeductScheduleId());
    if (!empNot.isEmpty())
      empDeductSchedule.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      empDeductScheduleReposDay.saveAndFlush(empDeductSchedule);	
    else if (dbName.equals(ContentName.onMon))
      empDeductScheduleReposMon.saveAndFlush(empDeductSchedule);
    else if (dbName.equals(ContentName.onHist))
        empDeductScheduleReposHist.saveAndFlush(empDeductSchedule);
    else 
      empDeductScheduleRepos.saveAndFlush(empDeductSchedule);	
    return this.findById(empDeductSchedule.getEmpDeductScheduleId());
  }

  @Override
  public void delete(EmpDeductSchedule empDeductSchedule, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + empDeductSchedule.getEmpDeductScheduleId());
    if (dbName.equals(ContentName.onDay)) {
      empDeductScheduleReposDay.delete(empDeductSchedule);	
      empDeductScheduleReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      empDeductScheduleReposMon.delete(empDeductSchedule);	
      empDeductScheduleReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      empDeductScheduleReposHist.delete(empDeductSchedule);
      empDeductScheduleReposHist.flush();
    }
    else {
      empDeductScheduleRepos.delete(empDeductSchedule);
      empDeductScheduleRepos.flush();
    }
   }

  @Override
  public void insertAll(List<EmpDeductSchedule> empDeductSchedule, TitaVo... titaVo) throws DBException {
    if (empDeductSchedule == null || empDeductSchedule.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (EmpDeductSchedule t : empDeductSchedule){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      empDeductSchedule = empDeductScheduleReposDay.saveAll(empDeductSchedule);	
      empDeductScheduleReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      empDeductSchedule = empDeductScheduleReposMon.saveAll(empDeductSchedule);	
      empDeductScheduleReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      empDeductSchedule = empDeductScheduleReposHist.saveAll(empDeductSchedule);
      empDeductScheduleReposHist.flush();
    }
    else {
      empDeductSchedule = empDeductScheduleRepos.saveAll(empDeductSchedule);
      empDeductScheduleRepos.flush();
    }
    }

  @Override
  public void updateAll(List<EmpDeductSchedule> empDeductSchedule, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (empDeductSchedule == null || empDeductSchedule.size() == 0)
      throw new DBException(6);

    for (EmpDeductSchedule t : empDeductSchedule) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      empDeductSchedule = empDeductScheduleReposDay.saveAll(empDeductSchedule);	
      empDeductScheduleReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      empDeductSchedule = empDeductScheduleReposMon.saveAll(empDeductSchedule);	
      empDeductScheduleReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      empDeductSchedule = empDeductScheduleReposHist.saveAll(empDeductSchedule);
      empDeductScheduleReposHist.flush();
    }
    else {
      empDeductSchedule = empDeductScheduleRepos.saveAll(empDeductSchedule);
      empDeductScheduleRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<EmpDeductSchedule> empDeductSchedule, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (empDeductSchedule == null || empDeductSchedule.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      empDeductScheduleReposDay.deleteAll(empDeductSchedule);	
      empDeductScheduleReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      empDeductScheduleReposMon.deleteAll(empDeductSchedule);	
      empDeductScheduleReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      empDeductScheduleReposHist.deleteAll(empDeductSchedule);
      empDeductScheduleReposHist.flush();
    }
    else {
      empDeductScheduleRepos.deleteAll(empDeductSchedule);
      empDeductScheduleRepos.flush();
    }
  }

}
