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
import com.st1.itx.db.domain.ClEva;
import com.st1.itx.db.domain.ClEvaId;
import com.st1.itx.db.repository.online.ClEvaRepository;
import com.st1.itx.db.repository.day.ClEvaRepositoryDay;
import com.st1.itx.db.repository.mon.ClEvaRepositoryMon;
import com.st1.itx.db.repository.hist.ClEvaRepositoryHist;
import com.st1.itx.db.service.ClEvaService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clEvaService")
@Repository
public class ClEvaServiceImpl extends ASpringJpaParm implements ClEvaService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private ClEvaRepository clEvaRepos;

  @Autowired
  private ClEvaRepositoryDay clEvaReposDay;

  @Autowired
  private ClEvaRepositoryMon clEvaReposMon;

  @Autowired
  private ClEvaRepositoryHist clEvaReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(clEvaRepos);
    org.junit.Assert.assertNotNull(clEvaReposDay);
    org.junit.Assert.assertNotNull(clEvaReposMon);
    org.junit.Assert.assertNotNull(clEvaReposHist);
  }

  @Override
  public ClEva findById(ClEvaId clEvaId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + clEvaId);
    Optional<ClEva> clEva = null;
    if (dbName.equals(ContentName.onDay))
      clEva = clEvaReposDay.findById(clEvaId);
    else if (dbName.equals(ContentName.onMon))
      clEva = clEvaReposMon.findById(clEvaId);
    else if (dbName.equals(ContentName.onHist))
      clEva = clEvaReposHist.findById(clEvaId);
    else 
      clEva = clEvaRepos.findById(clEvaId);
    ClEva obj = clEva.isPresent() ? clEva.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<ClEva> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClEva> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "EvaNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "EvaNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = clEvaReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clEvaReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clEvaReposHist.findAll(pageable);
    else 
      slice = clEvaRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClEva> findClNo(int clCode1_0, int clCode2_1, int clNo_2, int evaNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClEva> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findClNo " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2 + " evaNo_3 : " +  evaNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = clEvaReposDay.findAllByClCode1IsAndClCode2IsAndClNoIsAndEvaNoNotOrderByEvaNoAsc(clCode1_0, clCode2_1, clNo_2, evaNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clEvaReposMon.findAllByClCode1IsAndClCode2IsAndClNoIsAndEvaNoNotOrderByEvaNoAsc(clCode1_0, clCode2_1, clNo_2, evaNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clEvaReposHist.findAllByClCode1IsAndClCode2IsAndClNoIsAndEvaNoNotOrderByEvaNoAsc(clCode1_0, clCode2_1, clNo_2, evaNo_3, pageable);
    else 
      slice = clEvaRepos.findAllByClCode1IsAndClCode2IsAndClNoIsAndEvaNoNotOrderByEvaNoAsc(clCode1_0, clCode2_1, clNo_2, evaNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ClEva ClNoFirst(int clCode1_0, int clCode2_1, int clNo_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ClNoFirst " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2);
    Optional<ClEva> clEvaT = null;
    if (dbName.equals(ContentName.onDay))
      clEvaT = clEvaReposDay.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByEvaNoDesc(clCode1_0, clCode2_1, clNo_2);
    else if (dbName.equals(ContentName.onMon))
      clEvaT = clEvaReposMon.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByEvaNoDesc(clCode1_0, clCode2_1, clNo_2);
    else if (dbName.equals(ContentName.onHist))
      clEvaT = clEvaReposHist.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByEvaNoDesc(clCode1_0, clCode2_1, clNo_2);
    else 
      clEvaT = clEvaRepos.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByEvaNoDesc(clCode1_0, clCode2_1, clNo_2);

    return clEvaT.isPresent() ? clEvaT.get() : null;
  }

  @Override
  public ClEva holdById(ClEvaId clEvaId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clEvaId);
    Optional<ClEva> clEva = null;
    if (dbName.equals(ContentName.onDay))
      clEva = clEvaReposDay.findByClEvaId(clEvaId);
    else if (dbName.equals(ContentName.onMon))
      clEva = clEvaReposMon.findByClEvaId(clEvaId);
    else if (dbName.equals(ContentName.onHist))
      clEva = clEvaReposHist.findByClEvaId(clEvaId);
    else 
      clEva = clEvaRepos.findByClEvaId(clEvaId);
    return clEva.isPresent() ? clEva.get() : null;
  }

  @Override
  public ClEva holdById(ClEva clEva, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clEva.getClEvaId());
    Optional<ClEva> clEvaT = null;
    if (dbName.equals(ContentName.onDay))
      clEvaT = clEvaReposDay.findByClEvaId(clEva.getClEvaId());
    else if (dbName.equals(ContentName.onMon))
      clEvaT = clEvaReposMon.findByClEvaId(clEva.getClEvaId());
    else if (dbName.equals(ContentName.onHist))
      clEvaT = clEvaReposHist.findByClEvaId(clEva.getClEvaId());
    else 
      clEvaT = clEvaRepos.findByClEvaId(clEva.getClEvaId());
    return clEvaT.isPresent() ? clEvaT.get() : null;
  }

  @Override
  public ClEva insert(ClEva clEva, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + clEva.getClEvaId());
    if (this.findById(clEva.getClEvaId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      clEva.setCreateEmpNo(empNot);

    if(clEva.getLastUpdateEmpNo() == null || clEva.getLastUpdateEmpNo().isEmpty())
      clEva.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clEvaReposDay.saveAndFlush(clEva);	
    else if (dbName.equals(ContentName.onMon))
      return clEvaReposMon.saveAndFlush(clEva);
    else if (dbName.equals(ContentName.onHist))
      return clEvaReposHist.saveAndFlush(clEva);
    else 
    return clEvaRepos.saveAndFlush(clEva);
  }

  @Override
  public ClEva update(ClEva clEva, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + clEva.getClEvaId());
    if (!empNot.isEmpty())
      clEva.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clEvaReposDay.saveAndFlush(clEva);	
    else if (dbName.equals(ContentName.onMon))
      return clEvaReposMon.saveAndFlush(clEva);
    else if (dbName.equals(ContentName.onHist))
      return clEvaReposHist.saveAndFlush(clEva);
    else 
    return clEvaRepos.saveAndFlush(clEva);
  }

  @Override
  public ClEva update2(ClEva clEva, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + clEva.getClEvaId());
    if (!empNot.isEmpty())
      clEva.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      clEvaReposDay.saveAndFlush(clEva);	
    else if (dbName.equals(ContentName.onMon))
      clEvaReposMon.saveAndFlush(clEva);
    else if (dbName.equals(ContentName.onHist))
        clEvaReposHist.saveAndFlush(clEva);
    else 
      clEvaRepos.saveAndFlush(clEva);	
    return this.findById(clEva.getClEvaId());
  }

  @Override
  public void delete(ClEva clEva, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + clEva.getClEvaId());
    if (dbName.equals(ContentName.onDay)) {
      clEvaReposDay.delete(clEva);	
      clEvaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clEvaReposMon.delete(clEva);	
      clEvaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clEvaReposHist.delete(clEva);
      clEvaReposHist.flush();
    }
    else {
      clEvaRepos.delete(clEva);
      clEvaRepos.flush();
    }
   }

  @Override
  public void insertAll(List<ClEva> clEva, TitaVo... titaVo) throws DBException {
    if (clEva == null || clEva.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (ClEva t : clEva){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      clEva = clEvaReposDay.saveAll(clEva);	
      clEvaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clEva = clEvaReposMon.saveAll(clEva);	
      clEvaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clEva = clEvaReposHist.saveAll(clEva);
      clEvaReposHist.flush();
    }
    else {
      clEva = clEvaRepos.saveAll(clEva);
      clEvaRepos.flush();
    }
    }

  @Override
  public void updateAll(List<ClEva> clEva, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (clEva == null || clEva.size() == 0)
      throw new DBException(6);

    for (ClEva t : clEva) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      clEva = clEvaReposDay.saveAll(clEva);	
      clEvaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clEva = clEvaReposMon.saveAll(clEva);	
      clEvaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clEva = clEvaReposHist.saveAll(clEva);
      clEvaReposHist.flush();
    }
    else {
      clEva = clEvaRepos.saveAll(clEva);
      clEvaRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<ClEva> clEva, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (clEva == null || clEva.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      clEvaReposDay.deleteAll(clEva);	
      clEvaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clEvaReposMon.deleteAll(clEva);	
      clEvaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clEvaReposHist.deleteAll(clEva);
      clEvaReposHist.flush();
    }
    else {
      clEvaRepos.deleteAll(clEva);
      clEvaRepos.flush();
    }
  }

}
