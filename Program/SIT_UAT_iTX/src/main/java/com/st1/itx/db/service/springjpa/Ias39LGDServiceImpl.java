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
import com.st1.itx.db.domain.Ias39LGD;
import com.st1.itx.db.domain.Ias39LGDId;
import com.st1.itx.db.repository.online.Ias39LGDRepository;
import com.st1.itx.db.repository.day.Ias39LGDRepositoryDay;
import com.st1.itx.db.repository.mon.Ias39LGDRepositoryMon;
import com.st1.itx.db.repository.hist.Ias39LGDRepositoryHist;
import com.st1.itx.db.service.Ias39LGDService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("ias39LGDService")
@Repository
public class Ias39LGDServiceImpl extends ASpringJpaParm implements Ias39LGDService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private Ias39LGDRepository ias39LGDRepos;

  @Autowired
  private Ias39LGDRepositoryDay ias39LGDReposDay;

  @Autowired
  private Ias39LGDRepositoryMon ias39LGDReposMon;

  @Autowired
  private Ias39LGDRepositoryHist ias39LGDReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(ias39LGDRepos);
    org.junit.Assert.assertNotNull(ias39LGDReposDay);
    org.junit.Assert.assertNotNull(ias39LGDReposMon);
    org.junit.Assert.assertNotNull(ias39LGDReposHist);
  }

  @Override
  public Ias39LGD findById(Ias39LGDId ias39LGDId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + ias39LGDId);
    Optional<Ias39LGD> ias39LGD = null;
    if (dbName.equals(ContentName.onDay))
      ias39LGD = ias39LGDReposDay.findById(ias39LGDId);
    else if (dbName.equals(ContentName.onMon))
      ias39LGD = ias39LGDReposMon.findById(ias39LGDId);
    else if (dbName.equals(ContentName.onHist))
      ias39LGD = ias39LGDReposHist.findById(ias39LGDId);
    else 
      ias39LGD = ias39LGDRepos.findById(ias39LGDId);
    Ias39LGD obj = ias39LGD.isPresent() ? ias39LGD.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<Ias39LGD> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<Ias39LGD> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Date", "Type"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Date", "Type"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = ias39LGDReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = ias39LGDReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = ias39LGDReposHist.findAll(pageable);
    else 
      slice = ias39LGDRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<Ias39LGD> findDate(String type_0, int date_1, int date_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<Ias39LGD> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findDate " + dbName + " : " + "type_0 : " + type_0 + " date_1 : " +  date_1 + " date_2 : " +  date_2);
    if (dbName.equals(ContentName.onDay))
      slice = ias39LGDReposDay.findAllByTypeIsAndDateGreaterThanEqualAndDateLessThanEqualOrderByDateDesc(type_0, date_1, date_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = ias39LGDReposMon.findAllByTypeIsAndDateGreaterThanEqualAndDateLessThanEqualOrderByDateDesc(type_0, date_1, date_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = ias39LGDReposHist.findAllByTypeIsAndDateGreaterThanEqualAndDateLessThanEqualOrderByDateDesc(type_0, date_1, date_2, pageable);
    else 
      slice = ias39LGDRepos.findAllByTypeIsAndDateGreaterThanEqualAndDateLessThanEqualOrderByDateDesc(type_0, date_1, date_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<Ias39LGD> findType(int date_0, int date_1, String type_2, String type_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<Ias39LGD> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findType " + dbName + " : " + "date_0 : " + date_0 + " date_1 : " +  date_1 + " type_2 : " +  type_2 + " type_3 : " +  type_3);
    if (dbName.equals(ContentName.onDay))
      slice = ias39LGDReposDay.findAllByDateGreaterThanEqualAndDateLessThanEqualAndTypeGreaterThanEqualAndTypeLessThanEqualOrderByDateDescTypeAsc(date_0, date_1, type_2, type_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = ias39LGDReposMon.findAllByDateGreaterThanEqualAndDateLessThanEqualAndTypeGreaterThanEqualAndTypeLessThanEqualOrderByDateDescTypeAsc(date_0, date_1, type_2, type_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = ias39LGDReposHist.findAllByDateGreaterThanEqualAndDateLessThanEqualAndTypeGreaterThanEqualAndTypeLessThanEqualOrderByDateDescTypeAsc(date_0, date_1, type_2, type_3, pageable);
    else 
      slice = ias39LGDRepos.findAllByDateGreaterThanEqualAndDateLessThanEqualAndTypeGreaterThanEqualAndTypeLessThanEqualOrderByDateDescTypeAsc(date_0, date_1, type_2, type_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Ias39LGD holdById(Ias39LGDId ias39LGDId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + ias39LGDId);
    Optional<Ias39LGD> ias39LGD = null;
    if (dbName.equals(ContentName.onDay))
      ias39LGD = ias39LGDReposDay.findByIas39LGDId(ias39LGDId);
    else if (dbName.equals(ContentName.onMon))
      ias39LGD = ias39LGDReposMon.findByIas39LGDId(ias39LGDId);
    else if (dbName.equals(ContentName.onHist))
      ias39LGD = ias39LGDReposHist.findByIas39LGDId(ias39LGDId);
    else 
      ias39LGD = ias39LGDRepos.findByIas39LGDId(ias39LGDId);
    return ias39LGD.isPresent() ? ias39LGD.get() : null;
  }

  @Override
  public Ias39LGD holdById(Ias39LGD ias39LGD, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + ias39LGD.getIas39LGDId());
    Optional<Ias39LGD> ias39LGDT = null;
    if (dbName.equals(ContentName.onDay))
      ias39LGDT = ias39LGDReposDay.findByIas39LGDId(ias39LGD.getIas39LGDId());
    else if (dbName.equals(ContentName.onMon))
      ias39LGDT = ias39LGDReposMon.findByIas39LGDId(ias39LGD.getIas39LGDId());
    else if (dbName.equals(ContentName.onHist))
      ias39LGDT = ias39LGDReposHist.findByIas39LGDId(ias39LGD.getIas39LGDId());
    else 
      ias39LGDT = ias39LGDRepos.findByIas39LGDId(ias39LGD.getIas39LGDId());
    return ias39LGDT.isPresent() ? ias39LGDT.get() : null;
  }

  @Override
  public Ias39LGD insert(Ias39LGD ias39LGD, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + ias39LGD.getIas39LGDId());
    if (this.findById(ias39LGD.getIas39LGDId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      ias39LGD.setCreateEmpNo(empNot);

    if(ias39LGD.getLastUpdateEmpNo() == null || ias39LGD.getLastUpdateEmpNo().isEmpty())
      ias39LGD.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return ias39LGDReposDay.saveAndFlush(ias39LGD);	
    else if (dbName.equals(ContentName.onMon))
      return ias39LGDReposMon.saveAndFlush(ias39LGD);
    else if (dbName.equals(ContentName.onHist))
      return ias39LGDReposHist.saveAndFlush(ias39LGD);
    else 
    return ias39LGDRepos.saveAndFlush(ias39LGD);
  }

  @Override
  public Ias39LGD update(Ias39LGD ias39LGD, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + ias39LGD.getIas39LGDId());
    if (!empNot.isEmpty())
      ias39LGD.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return ias39LGDReposDay.saveAndFlush(ias39LGD);	
    else if (dbName.equals(ContentName.onMon))
      return ias39LGDReposMon.saveAndFlush(ias39LGD);
    else if (dbName.equals(ContentName.onHist))
      return ias39LGDReposHist.saveAndFlush(ias39LGD);
    else 
    return ias39LGDRepos.saveAndFlush(ias39LGD);
  }

  @Override
  public Ias39LGD update2(Ias39LGD ias39LGD, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + ias39LGD.getIas39LGDId());
    if (!empNot.isEmpty())
      ias39LGD.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      ias39LGDReposDay.saveAndFlush(ias39LGD);	
    else if (dbName.equals(ContentName.onMon))
      ias39LGDReposMon.saveAndFlush(ias39LGD);
    else if (dbName.equals(ContentName.onHist))
        ias39LGDReposHist.saveAndFlush(ias39LGD);
    else 
      ias39LGDRepos.saveAndFlush(ias39LGD);	
    return this.findById(ias39LGD.getIas39LGDId());
  }

  @Override
  public void delete(Ias39LGD ias39LGD, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + ias39LGD.getIas39LGDId());
    if (dbName.equals(ContentName.onDay)) {
      ias39LGDReposDay.delete(ias39LGD);	
      ias39LGDReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias39LGDReposMon.delete(ias39LGD);	
      ias39LGDReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias39LGDReposHist.delete(ias39LGD);
      ias39LGDReposHist.flush();
    }
    else {
      ias39LGDRepos.delete(ias39LGD);
      ias39LGDRepos.flush();
    }
   }

  @Override
  public void insertAll(List<Ias39LGD> ias39LGD, TitaVo... titaVo) throws DBException {
    if (ias39LGD == null || ias39LGD.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (Ias39LGD t : ias39LGD){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      ias39LGD = ias39LGDReposDay.saveAll(ias39LGD);	
      ias39LGDReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias39LGD = ias39LGDReposMon.saveAll(ias39LGD);	
      ias39LGDReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias39LGD = ias39LGDReposHist.saveAll(ias39LGD);
      ias39LGDReposHist.flush();
    }
    else {
      ias39LGD = ias39LGDRepos.saveAll(ias39LGD);
      ias39LGDRepos.flush();
    }
    }

  @Override
  public void updateAll(List<Ias39LGD> ias39LGD, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (ias39LGD == null || ias39LGD.size() == 0)
      throw new DBException(6);

    for (Ias39LGD t : ias39LGD) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      ias39LGD = ias39LGDReposDay.saveAll(ias39LGD);	
      ias39LGDReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias39LGD = ias39LGDReposMon.saveAll(ias39LGD);	
      ias39LGDReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias39LGD = ias39LGDReposHist.saveAll(ias39LGD);
      ias39LGDReposHist.flush();
    }
    else {
      ias39LGD = ias39LGDRepos.saveAll(ias39LGD);
      ias39LGDRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<Ias39LGD> ias39LGD, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (ias39LGD == null || ias39LGD.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      ias39LGDReposDay.deleteAll(ias39LGD);	
      ias39LGDReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ias39LGDReposMon.deleteAll(ias39LGD);	
      ias39LGDReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ias39LGDReposHist.deleteAll(ias39LGD);
      ias39LGDReposHist.flush();
    }
    else {
      ias39LGDRepos.deleteAll(ias39LGD);
      ias39LGDRepos.flush();
    }
  }

}
