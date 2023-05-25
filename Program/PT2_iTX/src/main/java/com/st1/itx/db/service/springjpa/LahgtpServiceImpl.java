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
import com.st1.itx.db.domain.Lahgtp;
import com.st1.itx.db.domain.LahgtpId;
import com.st1.itx.db.repository.online.LahgtpRepository;
import com.st1.itx.db.repository.day.LahgtpRepositoryDay;
import com.st1.itx.db.repository.mon.LahgtpRepositoryMon;
import com.st1.itx.db.repository.hist.LahgtpRepositoryHist;
import com.st1.itx.db.service.LahgtpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("lahgtpService")
@Repository
public class LahgtpServiceImpl extends ASpringJpaParm implements LahgtpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LahgtpRepository lahgtpRepos;

  @Autowired
  private LahgtpRepositoryDay lahgtpReposDay;

  @Autowired
  private LahgtpRepositoryMon lahgtpReposMon;

  @Autowired
  private LahgtpRepositoryHist lahgtpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(lahgtpRepos);
    org.junit.Assert.assertNotNull(lahgtpReposDay);
    org.junit.Assert.assertNotNull(lahgtpReposMon);
    org.junit.Assert.assertNotNull(lahgtpReposHist);
  }

  @Override
  public Lahgtp findById(LahgtpId lahgtpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + lahgtpId);
    Optional<Lahgtp> lahgtp = null;
    if (dbName.equals(ContentName.onDay))
      lahgtp = lahgtpReposDay.findById(lahgtpId);
    else if (dbName.equals(ContentName.onMon))
      lahgtp = lahgtpReposMon.findById(lahgtpId);
    else if (dbName.equals(ContentName.onHist))
      lahgtp = lahgtpReposHist.findById(lahgtpId);
    else 
      lahgtp = lahgtpRepos.findById(lahgtpId);
    Lahgtp obj = lahgtp.isPresent() ? lahgtp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<Lahgtp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<Lahgtp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Cusbrh", "Gdrid1", "Gdrid2", "Gdrnum", "Lgtseq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Cusbrh", "Gdrid1", "Gdrid2", "Gdrnum", "Lgtseq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = lahgtpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = lahgtpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = lahgtpReposHist.findAll(pageable);
    else 
      slice = lahgtpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Lahgtp holdById(LahgtpId lahgtpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + lahgtpId);
    Optional<Lahgtp> lahgtp = null;
    if (dbName.equals(ContentName.onDay))
      lahgtp = lahgtpReposDay.findByLahgtpId(lahgtpId);
    else if (dbName.equals(ContentName.onMon))
      lahgtp = lahgtpReposMon.findByLahgtpId(lahgtpId);
    else if (dbName.equals(ContentName.onHist))
      lahgtp = lahgtpReposHist.findByLahgtpId(lahgtpId);
    else 
      lahgtp = lahgtpRepos.findByLahgtpId(lahgtpId);
    return lahgtp.isPresent() ? lahgtp.get() : null;
  }

  @Override
  public Lahgtp holdById(Lahgtp lahgtp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + lahgtp.getLahgtpId());
    Optional<Lahgtp> lahgtpT = null;
    if (dbName.equals(ContentName.onDay))
      lahgtpT = lahgtpReposDay.findByLahgtpId(lahgtp.getLahgtpId());
    else if (dbName.equals(ContentName.onMon))
      lahgtpT = lahgtpReposMon.findByLahgtpId(lahgtp.getLahgtpId());
    else if (dbName.equals(ContentName.onHist))
      lahgtpT = lahgtpReposHist.findByLahgtpId(lahgtp.getLahgtpId());
    else 
      lahgtpT = lahgtpRepos.findByLahgtpId(lahgtp.getLahgtpId());
    return lahgtpT.isPresent() ? lahgtpT.get() : null;
  }

  @Override
  public Lahgtp insert(Lahgtp lahgtp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + lahgtp.getLahgtpId());
    if (this.findById(lahgtp.getLahgtpId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      lahgtp.setCreateEmpNo(empNot);

    if(lahgtp.getLastUpdateEmpNo() == null || lahgtp.getLastUpdateEmpNo().isEmpty())
      lahgtp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return lahgtpReposDay.saveAndFlush(lahgtp);	
    else if (dbName.equals(ContentName.onMon))
      return lahgtpReposMon.saveAndFlush(lahgtp);
    else if (dbName.equals(ContentName.onHist))
      return lahgtpReposHist.saveAndFlush(lahgtp);
    else 
    return lahgtpRepos.saveAndFlush(lahgtp);
  }

  @Override
  public Lahgtp update(Lahgtp lahgtp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + lahgtp.getLahgtpId());
    if (!empNot.isEmpty())
      lahgtp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return lahgtpReposDay.saveAndFlush(lahgtp);	
    else if (dbName.equals(ContentName.onMon))
      return lahgtpReposMon.saveAndFlush(lahgtp);
    else if (dbName.equals(ContentName.onHist))
      return lahgtpReposHist.saveAndFlush(lahgtp);
    else 
    return lahgtpRepos.saveAndFlush(lahgtp);
  }

  @Override
  public Lahgtp update2(Lahgtp lahgtp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + lahgtp.getLahgtpId());
    if (!empNot.isEmpty())
      lahgtp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      lahgtpReposDay.saveAndFlush(lahgtp);	
    else if (dbName.equals(ContentName.onMon))
      lahgtpReposMon.saveAndFlush(lahgtp);
    else if (dbName.equals(ContentName.onHist))
        lahgtpReposHist.saveAndFlush(lahgtp);
    else 
      lahgtpRepos.saveAndFlush(lahgtp);	
    return this.findById(lahgtp.getLahgtpId());
  }

  @Override
  public void delete(Lahgtp lahgtp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + lahgtp.getLahgtpId());
    if (dbName.equals(ContentName.onDay)) {
      lahgtpReposDay.delete(lahgtp);	
      lahgtpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      lahgtpReposMon.delete(lahgtp);	
      lahgtpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      lahgtpReposHist.delete(lahgtp);
      lahgtpReposHist.flush();
    }
    else {
      lahgtpRepos.delete(lahgtp);
      lahgtpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<Lahgtp> lahgtp, TitaVo... titaVo) throws DBException {
    if (lahgtp == null || lahgtp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (Lahgtp t : lahgtp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      lahgtp = lahgtpReposDay.saveAll(lahgtp);	
      lahgtpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      lahgtp = lahgtpReposMon.saveAll(lahgtp);	
      lahgtpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      lahgtp = lahgtpReposHist.saveAll(lahgtp);
      lahgtpReposHist.flush();
    }
    else {
      lahgtp = lahgtpRepos.saveAll(lahgtp);
      lahgtpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<Lahgtp> lahgtp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (lahgtp == null || lahgtp.size() == 0)
      throw new DBException(6);

    for (Lahgtp t : lahgtp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      lahgtp = lahgtpReposDay.saveAll(lahgtp);	
      lahgtpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      lahgtp = lahgtpReposMon.saveAll(lahgtp);	
      lahgtpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      lahgtp = lahgtpReposHist.saveAll(lahgtp);
      lahgtpReposHist.flush();
    }
    else {
      lahgtp = lahgtpRepos.saveAll(lahgtp);
      lahgtpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<Lahgtp> lahgtp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (lahgtp == null || lahgtp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      lahgtpReposDay.deleteAll(lahgtp);	
      lahgtpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      lahgtpReposMon.deleteAll(lahgtp);	
      lahgtpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      lahgtpReposHist.deleteAll(lahgtp);
      lahgtpReposHist.flush();
    }
    else {
      lahgtpRepos.deleteAll(lahgtp);
      lahgtpRepos.flush();
    }
  }

}
