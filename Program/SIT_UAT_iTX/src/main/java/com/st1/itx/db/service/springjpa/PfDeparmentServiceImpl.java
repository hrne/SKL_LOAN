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
import com.st1.itx.db.domain.PfDeparment;
import com.st1.itx.db.domain.PfDeparmentId;
import com.st1.itx.db.repository.online.PfDeparmentRepository;
import com.st1.itx.db.repository.day.PfDeparmentRepositoryDay;
import com.st1.itx.db.repository.mon.PfDeparmentRepositoryMon;
import com.st1.itx.db.repository.hist.PfDeparmentRepositoryHist;
import com.st1.itx.db.service.PfDeparmentService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("pfDeparmentService")
@Repository
public class PfDeparmentServiceImpl extends ASpringJpaParm implements PfDeparmentService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private PfDeparmentRepository pfDeparmentRepos;

  @Autowired
  private PfDeparmentRepositoryDay pfDeparmentReposDay;

  @Autowired
  private PfDeparmentRepositoryMon pfDeparmentReposMon;

  @Autowired
  private PfDeparmentRepositoryHist pfDeparmentReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(pfDeparmentRepos);
    org.junit.Assert.assertNotNull(pfDeparmentReposDay);
    org.junit.Assert.assertNotNull(pfDeparmentReposMon);
    org.junit.Assert.assertNotNull(pfDeparmentReposHist);
  }

  @Override
  public PfDeparment findById(PfDeparmentId pfDeparmentId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + pfDeparmentId);
    Optional<PfDeparment> pfDeparment = null;
    if (dbName.equals(ContentName.onDay))
      pfDeparment = pfDeparmentReposDay.findById(pfDeparmentId);
    else if (dbName.equals(ContentName.onMon))
      pfDeparment = pfDeparmentReposMon.findById(pfDeparmentId);
    else if (dbName.equals(ContentName.onHist))
      pfDeparment = pfDeparmentReposHist.findById(pfDeparmentId);
    else 
      pfDeparment = pfDeparmentRepos.findById(pfDeparmentId);
    PfDeparment obj = pfDeparment.isPresent() ? pfDeparment.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<PfDeparment> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfDeparment> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DistCode", "DeptCode", "UnitCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DistCode", "DeptCode", "UnitCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = pfDeparmentReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfDeparmentReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfDeparmentReposHist.findAll(pageable);
    else 
      slice = pfDeparmentRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfDeparment> findByDeptCode(String deptCode_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfDeparment> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByDeptCode " + dbName + " : " + "deptCode_0 : " + deptCode_0);
    if (dbName.equals(ContentName.onDay))
      slice = pfDeparmentReposDay.findAllByDeptCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(deptCode_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfDeparmentReposMon.findAllByDeptCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(deptCode_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfDeparmentReposHist.findAllByDeptCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(deptCode_0, pageable);
    else 
      slice = pfDeparmentRepos.findAllByDeptCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(deptCode_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfDeparment> findByDistCode(String distCode_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfDeparment> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByDistCode " + dbName + " : " + "distCode_0 : " + distCode_0);
    if (dbName.equals(ContentName.onDay))
      slice = pfDeparmentReposDay.findAllByDistCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(distCode_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfDeparmentReposMon.findAllByDistCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(distCode_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfDeparmentReposHist.findAllByDistCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(distCode_0, pageable);
    else 
      slice = pfDeparmentRepos.findAllByDistCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(distCode_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfDeparment> findByUnitCode(String unitCode_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfDeparment> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByUnitCode " + dbName + " : " + "unitCode_0 : " + unitCode_0);
    if (dbName.equals(ContentName.onDay))
      slice = pfDeparmentReposDay.findAllByUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(unitCode_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfDeparmentReposMon.findAllByUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(unitCode_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfDeparmentReposHist.findAllByUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(unitCode_0, pageable);
    else 
      slice = pfDeparmentRepos.findAllByUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(unitCode_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfDeparment> findByDeptCodeAndDistCode(String deptCode_0, String distCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfDeparment> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByDeptCodeAndDistCode " + dbName + " : " + "deptCode_0 : " + deptCode_0 + " distCode_1 : " +  distCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = pfDeparmentReposDay.findAllByDeptCodeIsAndDistCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(deptCode_0, distCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfDeparmentReposMon.findAllByDeptCodeIsAndDistCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(deptCode_0, distCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfDeparmentReposHist.findAllByDeptCodeIsAndDistCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(deptCode_0, distCode_1, pageable);
    else 
      slice = pfDeparmentRepos.findAllByDeptCodeIsAndDistCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(deptCode_0, distCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfDeparment> findByDeptCodeAndUnitCode(String deptCode_0, String unitCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfDeparment> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByDeptCodeAndUnitCode " + dbName + " : " + "deptCode_0 : " + deptCode_0 + " unitCode_1 : " +  unitCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = pfDeparmentReposDay.findAllByDeptCodeIsAndUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(deptCode_0, unitCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfDeparmentReposMon.findAllByDeptCodeIsAndUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(deptCode_0, unitCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfDeparmentReposHist.findAllByDeptCodeIsAndUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(deptCode_0, unitCode_1, pageable);
    else 
      slice = pfDeparmentRepos.findAllByDeptCodeIsAndUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(deptCode_0, unitCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfDeparment> findByDistCodeAndUnitCode(String distCode_0, String unitCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfDeparment> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByDistCodeAndUnitCode " + dbName + " : " + "distCode_0 : " + distCode_0 + " unitCode_1 : " +  unitCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = pfDeparmentReposDay.findAllByDistCodeIsAndUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(distCode_0, unitCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfDeparmentReposMon.findAllByDistCodeIsAndUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(distCode_0, unitCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfDeparmentReposHist.findAllByDistCodeIsAndUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(distCode_0, unitCode_1, pageable);
    else 
      slice = pfDeparmentRepos.findAllByDistCodeIsAndUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(distCode_0, unitCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfDeparment> findByDeptCodeAndDistCodeAndUnitCode(String deptCode_0, String distCode_1, String unitCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfDeparment> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByDeptCodeAndDistCodeAndUnitCode " + dbName + " : " + "deptCode_0 : " + deptCode_0 + " distCode_1 : " +  distCode_1 + " unitCode_2 : " +  unitCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = pfDeparmentReposDay.findAllByDeptCodeIsAndDistCodeIsAndUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(deptCode_0, distCode_1, unitCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfDeparmentReposMon.findAllByDeptCodeIsAndDistCodeIsAndUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(deptCode_0, distCode_1, unitCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfDeparmentReposHist.findAllByDeptCodeIsAndDistCodeIsAndUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(deptCode_0, distCode_1, unitCode_2, pageable);
    else 
      slice = pfDeparmentRepos.findAllByDeptCodeIsAndDistCodeIsAndUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(deptCode_0, distCode_1, unitCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PfDeparment holdById(PfDeparmentId pfDeparmentId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + pfDeparmentId);
    Optional<PfDeparment> pfDeparment = null;
    if (dbName.equals(ContentName.onDay))
      pfDeparment = pfDeparmentReposDay.findByPfDeparmentId(pfDeparmentId);
    else if (dbName.equals(ContentName.onMon))
      pfDeparment = pfDeparmentReposMon.findByPfDeparmentId(pfDeparmentId);
    else if (dbName.equals(ContentName.onHist))
      pfDeparment = pfDeparmentReposHist.findByPfDeparmentId(pfDeparmentId);
    else 
      pfDeparment = pfDeparmentRepos.findByPfDeparmentId(pfDeparmentId);
    return pfDeparment.isPresent() ? pfDeparment.get() : null;
  }

  @Override
  public PfDeparment holdById(PfDeparment pfDeparment, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + pfDeparment.getPfDeparmentId());
    Optional<PfDeparment> pfDeparmentT = null;
    if (dbName.equals(ContentName.onDay))
      pfDeparmentT = pfDeparmentReposDay.findByPfDeparmentId(pfDeparment.getPfDeparmentId());
    else if (dbName.equals(ContentName.onMon))
      pfDeparmentT = pfDeparmentReposMon.findByPfDeparmentId(pfDeparment.getPfDeparmentId());
    else if (dbName.equals(ContentName.onHist))
      pfDeparmentT = pfDeparmentReposHist.findByPfDeparmentId(pfDeparment.getPfDeparmentId());
    else 
      pfDeparmentT = pfDeparmentRepos.findByPfDeparmentId(pfDeparment.getPfDeparmentId());
    return pfDeparmentT.isPresent() ? pfDeparmentT.get() : null;
  }

  @Override
  public PfDeparment insert(PfDeparment pfDeparment, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + pfDeparment.getPfDeparmentId());
    if (this.findById(pfDeparment.getPfDeparmentId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      pfDeparment.setCreateEmpNo(empNot);

    if(pfDeparment.getLastUpdateEmpNo() == null || pfDeparment.getLastUpdateEmpNo().isEmpty())
      pfDeparment.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfDeparmentReposDay.saveAndFlush(pfDeparment);	
    else if (dbName.equals(ContentName.onMon))
      return pfDeparmentReposMon.saveAndFlush(pfDeparment);
    else if (dbName.equals(ContentName.onHist))
      return pfDeparmentReposHist.saveAndFlush(pfDeparment);
    else 
    return pfDeparmentRepos.saveAndFlush(pfDeparment);
  }

  @Override
  public PfDeparment update(PfDeparment pfDeparment, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + pfDeparment.getPfDeparmentId());
    if (!empNot.isEmpty())
      pfDeparment.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfDeparmentReposDay.saveAndFlush(pfDeparment);	
    else if (dbName.equals(ContentName.onMon))
      return pfDeparmentReposMon.saveAndFlush(pfDeparment);
    else if (dbName.equals(ContentName.onHist))
      return pfDeparmentReposHist.saveAndFlush(pfDeparment);
    else 
    return pfDeparmentRepos.saveAndFlush(pfDeparment);
  }

  @Override
  public PfDeparment update2(PfDeparment pfDeparment, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + pfDeparment.getPfDeparmentId());
    if (!empNot.isEmpty())
      pfDeparment.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      pfDeparmentReposDay.saveAndFlush(pfDeparment);	
    else if (dbName.equals(ContentName.onMon))
      pfDeparmentReposMon.saveAndFlush(pfDeparment);
    else if (dbName.equals(ContentName.onHist))
        pfDeparmentReposHist.saveAndFlush(pfDeparment);
    else 
      pfDeparmentRepos.saveAndFlush(pfDeparment);	
    return this.findById(pfDeparment.getPfDeparmentId());
  }

  @Override
  public void delete(PfDeparment pfDeparment, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + pfDeparment.getPfDeparmentId());
    if (dbName.equals(ContentName.onDay)) {
      pfDeparmentReposDay.delete(pfDeparment);	
      pfDeparmentReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfDeparmentReposMon.delete(pfDeparment);	
      pfDeparmentReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfDeparmentReposHist.delete(pfDeparment);
      pfDeparmentReposHist.flush();
    }
    else {
      pfDeparmentRepos.delete(pfDeparment);
      pfDeparmentRepos.flush();
    }
   }

  @Override
  public void insertAll(List<PfDeparment> pfDeparment, TitaVo... titaVo) throws DBException {
    if (pfDeparment == null || pfDeparment.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (PfDeparment t : pfDeparment){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      pfDeparment = pfDeparmentReposDay.saveAll(pfDeparment);	
      pfDeparmentReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfDeparment = pfDeparmentReposMon.saveAll(pfDeparment);	
      pfDeparmentReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfDeparment = pfDeparmentReposHist.saveAll(pfDeparment);
      pfDeparmentReposHist.flush();
    }
    else {
      pfDeparment = pfDeparmentRepos.saveAll(pfDeparment);
      pfDeparmentRepos.flush();
    }
    }

  @Override
  public void updateAll(List<PfDeparment> pfDeparment, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (pfDeparment == null || pfDeparment.size() == 0)
      throw new DBException(6);

    for (PfDeparment t : pfDeparment) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      pfDeparment = pfDeparmentReposDay.saveAll(pfDeparment);	
      pfDeparmentReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfDeparment = pfDeparmentReposMon.saveAll(pfDeparment);	
      pfDeparmentReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfDeparment = pfDeparmentReposHist.saveAll(pfDeparment);
      pfDeparmentReposHist.flush();
    }
    else {
      pfDeparment = pfDeparmentRepos.saveAll(pfDeparment);
      pfDeparmentRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<PfDeparment> pfDeparment, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (pfDeparment == null || pfDeparment.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      pfDeparmentReposDay.deleteAll(pfDeparment);	
      pfDeparmentReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfDeparmentReposMon.deleteAll(pfDeparment);	
      pfDeparmentReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfDeparmentReposHist.deleteAll(pfDeparment);
      pfDeparmentReposHist.flush();
    }
    else {
      pfDeparmentRepos.deleteAll(pfDeparment);
      pfDeparmentRepos.flush();
    }
  }

}
