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
import com.st1.itx.db.domain.Ias34Bp;
import com.st1.itx.db.domain.Ias34BpId;
import com.st1.itx.db.repository.online.Ias34BpRepository;
import com.st1.itx.db.repository.day.Ias34BpRepositoryDay;
import com.st1.itx.db.repository.mon.Ias34BpRepositoryMon;
import com.st1.itx.db.repository.hist.Ias34BpRepositoryHist;
import com.st1.itx.db.service.Ias34BpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("ias34BpService")
@Repository
public class Ias34BpServiceImpl extends ASpringJpaParm implements Ias34BpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private Ias34BpRepository ias34BpRepos;

  @Autowired
  private Ias34BpRepositoryDay ias34BpReposDay;

  @Autowired
  private Ias34BpRepositoryMon ias34BpReposMon;

  @Autowired
  private Ias34BpRepositoryHist ias34BpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(ias34BpRepos);
    org.junit.Assert.assertNotNull(ias34BpReposDay);
    org.junit.Assert.assertNotNull(ias34BpReposMon);
    org.junit.Assert.assertNotNull(ias34BpReposHist);
  }

  @Override
  public Ias34Bp findById(Ias34BpId ias34BpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + ias34BpId);
    Optional<Ias34Bp> ias34Bp = null;
    if (dbName.equals(ContentName.onDay))
      ias34Bp = ias34BpReposDay.findById(ias34BpId);
    else if (dbName.equals(ContentName.onMon))
      ias34Bp = ias34BpReposMon.findById(ias34BpId);
    else if (dbName.equals(ContentName.onHist))
      ias34Bp = ias34BpReposHist.findById(ias34BpId);
    else 
      ias34Bp = ias34BpRepos.findById(ias34BpId);
    Ias34Bp obj = ias34Bp.isPresent() ? ias34Bp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<Ias34Bp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<Ias34Bp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo", "EffectDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo", "EffectDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = ias34BpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = ias34BpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = ias34BpReposHist.findAll(pageable);
    else 
      slice = ias34BpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Ias34Bp holdById(Ias34BpId ias34BpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + ias34BpId);
    Optional<Ias34Bp> ias34Bp = null;
    if (dbName.equals(ContentName.onDay))
      ias34Bp = ias34BpReposDay.findByIas34BpId(ias34BpId);
    else if (dbName.equals(ContentName.onMon))
      ias34Bp = ias34BpReposMon.findByIas34BpId(ias34BpId);
    else if (dbName.equals(ContentName.onHist))
      ias34Bp = ias34BpReposHist.findByIas34BpId(ias34BpId);
    else 
      ias34Bp = ias34BpRepos.findByIas34BpId(ias34BpId);
    return ias34Bp.isPresent() ? ias34Bp.get() : null;
  }

  @Override
  public Ias34Bp holdById(Ias34Bp ias34Bp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + ias34Bp.getIas34BpId());
    Optional<Ias34Bp> ias34BpT = null;
    if (dbName.equals(ContentName.onDay))
      ias34BpT = ias34BpReposDay.findByIas34BpId(ias34Bp.getIas34BpId());
    else if (dbName.equals(ContentName.onMon))
      ias34BpT = ias34BpReposMon.findByIas34BpId(ias34Bp.getIas34BpId());
    else if (dbName.equals(ContentName.onHist))
      ias34BpT = ias34BpReposHist.findByIas34BpId(ias34Bp.getIas34BpId());
    else 
      ias34BpT = ias34BpRepos.findByIas34BpId(ias34Bp.getIas34BpId());
    return ias34BpT.isPresent() ? ias34BpT.get() : null;
  }

  @Override
  public Ias34Bp insert(Ias34Bp ias34Bp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + ias34Bp.getIas34BpId());
    if (this.findById(ias34Bp.getIas34BpId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      ias34Bp.setCreateEmpNo(empNot);

    if(ias34Bp.getLastUpdateEmpNo() == null || ias34Bp.getLastUpdateEmpNo().isEmpty())
      ias34Bp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return ias34BpReposDay.saveAndFlush(ias34Bp);	
    else if (dbName.equals(ContentName.onMon))
      return ias34BpReposMon.saveAndFlush(ias34Bp);
    else if (dbName.equals(ContentName.onHist))
      return ias34BpReposHist.saveAndFlush(ias34Bp);
    else 
    return ias34BpRepos.saveAndFlush(ias34Bp);
  }

  @Override
  public Ias34Bp update(Ias34Bp ias34Bp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + ias34Bp.getIas34BpId());
    if (!empNot.isEmpty())
      ias34Bp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return ias34BpReposDay.saveAndFlush(ias34Bp);	
    else if (dbName.equals(ContentName.onMon))
      return ias34BpReposMon.saveAndFlush(ias34Bp);
    else if (dbName.equals(ContentName.onHist))
      return ias34BpReposHist.saveAndFlush(ias34Bp);
    else 
    return ias34BpRepos.saveAndFlush(ias34Bp);
  }

  @Override
  public Ias34Bp update2(Ias34Bp ias34Bp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + ias34Bp.getIas34BpId());
    if (!empNot.isEmpty())
      ias34Bp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      ias34BpReposDay.saveAndFlush(ias34Bp);	
    else if (dbName.equals(ContentName.onMon))
      ias34BpReposMon.saveAndFlush(ias34Bp);
    else if (dbName.equals(ContentName.onHist))
        ias34BpReposHist.saveAndFlush(ias34Bp);
    else 
      ias34BpRepos.saveAndFlush(ias34Bp);	
    return this.findById(ias34Bp.getIas34BpId());
  }

  @Override
  public void delete(Ias34Bp ias34Bp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + ias34Bp.getIas34BpId());
    if (dbName.equals(ContentName.onDay)) {
      ias34BpReposDay.delete(ias34Bp);	
      ias34BpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias34BpReposMon.delete(ias34Bp);	
      ias34BpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias34BpReposHist.delete(ias34Bp);
      ias34BpReposHist.flush();
    }
    else {
      ias34BpRepos.delete(ias34Bp);
      ias34BpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<Ias34Bp> ias34Bp, TitaVo... titaVo) throws DBException {
    if (ias34Bp == null || ias34Bp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (Ias34Bp t : ias34Bp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      ias34Bp = ias34BpReposDay.saveAll(ias34Bp);	
      ias34BpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias34Bp = ias34BpReposMon.saveAll(ias34Bp);	
      ias34BpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias34Bp = ias34BpReposHist.saveAll(ias34Bp);
      ias34BpReposHist.flush();
    }
    else {
      ias34Bp = ias34BpRepos.saveAll(ias34Bp);
      ias34BpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<Ias34Bp> ias34Bp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (ias34Bp == null || ias34Bp.size() == 0)
      throw new DBException(6);

    for (Ias34Bp t : ias34Bp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      ias34Bp = ias34BpReposDay.saveAll(ias34Bp);	
      ias34BpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias34Bp = ias34BpReposMon.saveAll(ias34Bp);	
      ias34BpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias34Bp = ias34BpReposHist.saveAll(ias34Bp);
      ias34BpReposHist.flush();
    }
    else {
      ias34Bp = ias34BpRepos.saveAll(ias34Bp);
      ias34BpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<Ias34Bp> ias34Bp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (ias34Bp == null || ias34Bp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      ias34BpReposDay.deleteAll(ias34Bp);	
      ias34BpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias34BpReposMon.deleteAll(ias34Bp);	
      ias34BpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias34BpReposHist.deleteAll(ias34Bp);
      ias34BpReposHist.flush();
    }
    else {
      ias34BpRepos.deleteAll(ias34Bp);
      ias34BpRepos.flush();
    }
  }

  @Override
  public void Usp_L7_Ias34Bp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      ias34BpReposDay.uspL7Ias34bpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      ias34BpReposMon.uspL7Ias34bpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      ias34BpReposHist.uspL7Ias34bpUpd(TBSDYF, EmpNo);
   else
      ias34BpRepos.uspL7Ias34bpUpd(TBSDYF, EmpNo);
  }

}
