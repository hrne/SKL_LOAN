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
import com.st1.itx.db.domain.InsuComm;
import com.st1.itx.db.domain.InsuCommId;
import com.st1.itx.db.repository.online.InsuCommRepository;
import com.st1.itx.db.repository.day.InsuCommRepositoryDay;
import com.st1.itx.db.repository.mon.InsuCommRepositoryMon;
import com.st1.itx.db.repository.hist.InsuCommRepositoryHist;
import com.st1.itx.db.service.InsuCommService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("insuCommService")
@Repository
public class InsuCommServiceImpl extends ASpringJpaParm implements InsuCommService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private InsuCommRepository insuCommRepos;

  @Autowired
  private InsuCommRepositoryDay insuCommReposDay;

  @Autowired
  private InsuCommRepositoryMon insuCommReposMon;

  @Autowired
  private InsuCommRepositoryHist insuCommReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(insuCommRepos);
    org.junit.Assert.assertNotNull(insuCommReposDay);
    org.junit.Assert.assertNotNull(insuCommReposMon);
    org.junit.Assert.assertNotNull(insuCommReposHist);
  }

  @Override
  public InsuComm findById(InsuCommId insuCommId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + insuCommId);
    Optional<InsuComm> insuComm = null;
    if (dbName.equals(ContentName.onDay))
      insuComm = insuCommReposDay.findById(insuCommId);
    else if (dbName.equals(ContentName.onMon))
      insuComm = insuCommReposMon.findById(insuCommId);
    else if (dbName.equals(ContentName.onHist))
      insuComm = insuCommReposHist.findById(insuCommId);
    else 
      insuComm = insuCommRepos.findById(insuCommId);
    InsuComm obj = insuComm.isPresent() ? insuComm.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<InsuComm> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuComm> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "InsuYearMonth", "InsuCommSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "InsuYearMonth", "InsuCommSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = insuCommReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuCommReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuCommReposHist.findAll(pageable);
    else 
      slice = insuCommRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuComm> insuYearMonthRng(int insuYearMonth_0, int insuYearMonth_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuComm> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("insuYearMonthRng " + dbName + " : " + "insuYearMonth_0 : " + insuYearMonth_0 + " insuYearMonth_1 : " +  insuYearMonth_1);
    if (dbName.equals(ContentName.onDay))
      slice = insuCommReposDay.findAllByInsuYearMonthGreaterThanEqualAndInsuYearMonthLessThanEqualOrderByEmpIdAscNowInsuNoAscInsuCateAsc(insuYearMonth_0, insuYearMonth_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuCommReposMon.findAllByInsuYearMonthGreaterThanEqualAndInsuYearMonthLessThanEqualOrderByEmpIdAscNowInsuNoAscInsuCateAsc(insuYearMonth_0, insuYearMonth_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuCommReposHist.findAllByInsuYearMonthGreaterThanEqualAndInsuYearMonthLessThanEqualOrderByEmpIdAscNowInsuNoAscInsuCateAsc(insuYearMonth_0, insuYearMonth_1, pageable);
    else 
      slice = insuCommRepos.findAllByInsuYearMonthGreaterThanEqualAndInsuYearMonthLessThanEqualOrderByEmpIdAscNowInsuNoAscInsuCateAsc(insuYearMonth_0, insuYearMonth_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuComm> findL4606A(int insuYearMonth_0, int commDate_1, int commDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuComm> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4606A " + dbName + " : " + "insuYearMonth_0 : " + insuYearMonth_0 + " commDate_1 : " +  commDate_1 + " commDate_2 : " +  commDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = insuCommReposDay.findAllByInsuYearMonthIsAndCommDateGreaterThanEqualAndCommDateLessThanEqualOrderByNowInsuNoAscInsuCateAsc(insuYearMonth_0, commDate_1, commDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuCommReposMon.findAllByInsuYearMonthIsAndCommDateGreaterThanEqualAndCommDateLessThanEqualOrderByNowInsuNoAscInsuCateAsc(insuYearMonth_0, commDate_1, commDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuCommReposHist.findAllByInsuYearMonthIsAndCommDateGreaterThanEqualAndCommDateLessThanEqualOrderByNowInsuNoAscInsuCateAsc(insuYearMonth_0, commDate_1, commDate_2, pageable);
    else 
      slice = insuCommRepos.findAllByInsuYearMonthIsAndCommDateGreaterThanEqualAndCommDateLessThanEqualOrderByNowInsuNoAscInsuCateAsc(insuYearMonth_0, commDate_1, commDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuComm> findCustNo(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuComm> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCustNo " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = insuCommReposDay.findAllByCustNoIsOrderByInsuYearMonthDescNowInsuNoAscInsuCateAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuCommReposMon.findAllByCustNoIsOrderByInsuYearMonthDescNowInsuNoAscInsuCateAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuCommReposHist.findAllByCustNoIsOrderByInsuYearMonthDescNowInsuNoAscInsuCateAsc(custNo_0, pageable);
    else 
      slice = insuCommRepos.findAllByCustNoIsOrderByInsuYearMonthDescNowInsuNoAscInsuCateAsc(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuComm> findFireOfficer(String fireOfficer_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuComm> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findFireOfficer " + dbName + " : " + "fireOfficer_0 : " + fireOfficer_0);
    if (dbName.equals(ContentName.onDay))
      slice = insuCommReposDay.findAllByFireOfficerIsOrderByInsuYearMonthDescNowInsuNoAscInsuCateAsc(fireOfficer_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuCommReposMon.findAllByFireOfficerIsOrderByInsuYearMonthDescNowInsuNoAscInsuCateAsc(fireOfficer_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuCommReposHist.findAllByFireOfficerIsOrderByInsuYearMonthDescNowInsuNoAscInsuCateAsc(fireOfficer_0, pageable);
    else 
      slice = insuCommRepos.findAllByFireOfficerIsOrderByInsuYearMonthDescNowInsuNoAscInsuCateAsc(fireOfficer_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuComm> findEmpId(String empId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuComm> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findEmpId " + dbName + " : " + "empId_0 : " + empId_0);
    if (dbName.equals(ContentName.onDay))
      slice = insuCommReposDay.findAllByEmpIdIsOrderByInsuYearMonthDescNowInsuNoAscInsuCateAsc(empId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuCommReposMon.findAllByEmpIdIsOrderByInsuYearMonthDescNowInsuNoAscInsuCateAsc(empId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuCommReposHist.findAllByEmpIdIsOrderByInsuYearMonthDescNowInsuNoAscInsuCateAsc(empId_0, pageable);
    else 
      slice = insuCommRepos.findAllByEmpIdIsOrderByInsuYearMonthDescNowInsuNoAscInsuCateAsc(empId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public InsuComm holdById(InsuCommId insuCommId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + insuCommId);
    Optional<InsuComm> insuComm = null;
    if (dbName.equals(ContentName.onDay))
      insuComm = insuCommReposDay.findByInsuCommId(insuCommId);
    else if (dbName.equals(ContentName.onMon))
      insuComm = insuCommReposMon.findByInsuCommId(insuCommId);
    else if (dbName.equals(ContentName.onHist))
      insuComm = insuCommReposHist.findByInsuCommId(insuCommId);
    else 
      insuComm = insuCommRepos.findByInsuCommId(insuCommId);
    return insuComm.isPresent() ? insuComm.get() : null;
  }

  @Override
  public InsuComm holdById(InsuComm insuComm, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + insuComm.getInsuCommId());
    Optional<InsuComm> insuCommT = null;
    if (dbName.equals(ContentName.onDay))
      insuCommT = insuCommReposDay.findByInsuCommId(insuComm.getInsuCommId());
    else if (dbName.equals(ContentName.onMon))
      insuCommT = insuCommReposMon.findByInsuCommId(insuComm.getInsuCommId());
    else if (dbName.equals(ContentName.onHist))
      insuCommT = insuCommReposHist.findByInsuCommId(insuComm.getInsuCommId());
    else 
      insuCommT = insuCommRepos.findByInsuCommId(insuComm.getInsuCommId());
    return insuCommT.isPresent() ? insuCommT.get() : null;
  }

  @Override
  public InsuComm insert(InsuComm insuComm, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + insuComm.getInsuCommId());
    if (this.findById(insuComm.getInsuCommId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      insuComm.setCreateEmpNo(empNot);

    if(insuComm.getLastUpdateEmpNo() == null || insuComm.getLastUpdateEmpNo().isEmpty())
      insuComm.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return insuCommReposDay.saveAndFlush(insuComm);	
    else if (dbName.equals(ContentName.onMon))
      return insuCommReposMon.saveAndFlush(insuComm);
    else if (dbName.equals(ContentName.onHist))
      return insuCommReposHist.saveAndFlush(insuComm);
    else 
    return insuCommRepos.saveAndFlush(insuComm);
  }

  @Override
  public InsuComm update(InsuComm insuComm, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + insuComm.getInsuCommId());
    if (!empNot.isEmpty())
      insuComm.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return insuCommReposDay.saveAndFlush(insuComm);	
    else if (dbName.equals(ContentName.onMon))
      return insuCommReposMon.saveAndFlush(insuComm);
    else if (dbName.equals(ContentName.onHist))
      return insuCommReposHist.saveAndFlush(insuComm);
    else 
    return insuCommRepos.saveAndFlush(insuComm);
  }

  @Override
  public InsuComm update2(InsuComm insuComm, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + insuComm.getInsuCommId());
    if (!empNot.isEmpty())
      insuComm.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      insuCommReposDay.saveAndFlush(insuComm);	
    else if (dbName.equals(ContentName.onMon))
      insuCommReposMon.saveAndFlush(insuComm);
    else if (dbName.equals(ContentName.onHist))
        insuCommReposHist.saveAndFlush(insuComm);
    else 
      insuCommRepos.saveAndFlush(insuComm);	
    return this.findById(insuComm.getInsuCommId());
  }

  @Override
  public void delete(InsuComm insuComm, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + insuComm.getInsuCommId());
    if (dbName.equals(ContentName.onDay)) {
      insuCommReposDay.delete(insuComm);	
      insuCommReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      insuCommReposMon.delete(insuComm);	
      insuCommReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      insuCommReposHist.delete(insuComm);
      insuCommReposHist.flush();
    }
    else {
      insuCommRepos.delete(insuComm);
      insuCommRepos.flush();
    }
   }

  @Override
  public void insertAll(List<InsuComm> insuComm, TitaVo... titaVo) throws DBException {
    if (insuComm == null || insuComm.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (InsuComm t : insuComm){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      insuComm = insuCommReposDay.saveAll(insuComm);	
      insuCommReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      insuComm = insuCommReposMon.saveAll(insuComm);	
      insuCommReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      insuComm = insuCommReposHist.saveAll(insuComm);
      insuCommReposHist.flush();
    }
    else {
      insuComm = insuCommRepos.saveAll(insuComm);
      insuCommRepos.flush();
    }
    }

  @Override
  public void updateAll(List<InsuComm> insuComm, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (insuComm == null || insuComm.size() == 0)
      throw new DBException(6);

    for (InsuComm t : insuComm) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      insuComm = insuCommReposDay.saveAll(insuComm);	
      insuCommReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      insuComm = insuCommReposMon.saveAll(insuComm);	
      insuCommReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      insuComm = insuCommReposHist.saveAll(insuComm);
      insuCommReposHist.flush();
    }
    else {
      insuComm = insuCommRepos.saveAll(insuComm);
      insuCommRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<InsuComm> insuComm, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (insuComm == null || insuComm.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      insuCommReposDay.deleteAll(insuComm);	
      insuCommReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      insuCommReposMon.deleteAll(insuComm);	
      insuCommReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      insuCommReposHist.deleteAll(insuComm);
      insuCommReposHist.flush();
    }
    else {
      insuCommRepos.deleteAll(insuComm);
      insuCommRepos.flush();
    }
  }

}
