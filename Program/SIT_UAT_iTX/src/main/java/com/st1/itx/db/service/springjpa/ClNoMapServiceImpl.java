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
import com.st1.itx.db.domain.ClNoMap;
import com.st1.itx.db.domain.ClNoMapId;
import com.st1.itx.db.repository.online.ClNoMapRepository;
import com.st1.itx.db.repository.day.ClNoMapRepositoryDay;
import com.st1.itx.db.repository.mon.ClNoMapRepositoryMon;
import com.st1.itx.db.repository.hist.ClNoMapRepositoryHist;
import com.st1.itx.db.service.ClNoMapService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clNoMapService")
@Repository
public class ClNoMapServiceImpl implements ClNoMapService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(ClNoMapServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private ClNoMapRepository clNoMapRepos;

  @Autowired
  private ClNoMapRepositoryDay clNoMapReposDay;

  @Autowired
  private ClNoMapRepositoryMon clNoMapReposMon;

  @Autowired
  private ClNoMapRepositoryHist clNoMapReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(clNoMapRepos);
    org.junit.Assert.assertNotNull(clNoMapReposDay);
    org.junit.Assert.assertNotNull(clNoMapReposMon);
    org.junit.Assert.assertNotNull(clNoMapReposHist);
  }

  @Override
  public ClNoMap findById(ClNoMapId clNoMapId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + clNoMapId);
    Optional<ClNoMap> clNoMap = null;
    if (dbName.equals(ContentName.onDay))
      clNoMap = clNoMapReposDay.findById(clNoMapId);
    else if (dbName.equals(ContentName.onMon))
      clNoMap = clNoMapReposMon.findById(clNoMapId);
    else if (dbName.equals(ContentName.onHist))
      clNoMap = clNoMapReposHist.findById(clNoMapId);
    else 
      clNoMap = clNoMapRepos.findById(clNoMapId);
    ClNoMap obj = clNoMap.isPresent() ? clNoMap.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<ClNoMap> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClNoMap> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "GDRID1", "GDRID2", "GDRNUM", "LGTSEQ"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = clNoMapReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clNoMapReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clNoMapReposHist.findAll(pageable);
    else 
      slice = clNoMapRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClNoMap> findGDRNUM(int gDRID1_0, int gDRID2_1, int gDRNUM_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClNoMap> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findGDRNUM " + dbName + " : " + "gDRID1_0 : " + gDRID1_0 + " gDRID2_1 : " +  gDRID2_1 + " gDRNUM_2 : " +  gDRNUM_2);
    if (dbName.equals(ContentName.onDay))
      slice = clNoMapReposDay.findAllByGDRID1IsAndGDRID2IsAndGDRNUMIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(gDRID1_0, gDRID2_1, gDRNUM_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clNoMapReposMon.findAllByGDRID1IsAndGDRID2IsAndGDRNUMIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(gDRID1_0, gDRID2_1, gDRNUM_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clNoMapReposHist.findAllByGDRID1IsAndGDRID2IsAndGDRNUMIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(gDRID1_0, gDRID2_1, gDRNUM_2, pageable);
    else 
      slice = clNoMapRepos.findAllByGDRID1IsAndGDRID2IsAndGDRNUMIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(gDRID1_0, gDRID2_1, gDRNUM_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClNoMap> findMainLGTSEQ(int mainGDRID1_0, int mainGDRID2_1, int mainGDRNUM_2, int mainLGTSEQ_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClNoMap> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findMainLGTSEQ " + dbName + " : " + "mainGDRID1_0 : " + mainGDRID1_0 + " mainGDRID2_1 : " +  mainGDRID2_1 + " mainGDRNUM_2 : " +  mainGDRNUM_2 + " mainLGTSEQ_3 : " +  mainLGTSEQ_3);
    if (dbName.equals(ContentName.onDay))
      slice = clNoMapReposDay.findAllByMainGDRID1IsAndMainGDRID2IsAndMainGDRNUMIsAndMainLGTSEQIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(mainGDRID1_0, mainGDRID2_1, mainGDRNUM_2, mainLGTSEQ_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clNoMapReposMon.findAllByMainGDRID1IsAndMainGDRID2IsAndMainGDRNUMIsAndMainLGTSEQIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(mainGDRID1_0, mainGDRID2_1, mainGDRNUM_2, mainLGTSEQ_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clNoMapReposHist.findAllByMainGDRID1IsAndMainGDRID2IsAndMainGDRNUMIsAndMainLGTSEQIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(mainGDRID1_0, mainGDRID2_1, mainGDRNUM_2, mainLGTSEQ_3, pageable);
    else 
      slice = clNoMapRepos.findAllByMainGDRID1IsAndMainGDRID2IsAndMainGDRNUMIsAndMainLGTSEQIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(mainGDRID1_0, mainGDRID2_1, mainGDRNUM_2, mainLGTSEQ_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClNoMap> findMainGDRNUM(int mainGDRID1_0, int mainGDRID2_1, int mainGDRNUM_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClNoMap> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findMainGDRNUM " + dbName + " : " + "mainGDRID1_0 : " + mainGDRID1_0 + " mainGDRID2_1 : " +  mainGDRID2_1 + " mainGDRNUM_2 : " +  mainGDRNUM_2);
    if (dbName.equals(ContentName.onDay))
      slice = clNoMapReposDay.findAllByMainGDRID1IsAndMainGDRID2IsAndMainGDRNUMIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(mainGDRID1_0, mainGDRID2_1, mainGDRNUM_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clNoMapReposMon.findAllByMainGDRID1IsAndMainGDRID2IsAndMainGDRNUMIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(mainGDRID1_0, mainGDRID2_1, mainGDRNUM_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clNoMapReposHist.findAllByMainGDRID1IsAndMainGDRID2IsAndMainGDRNUMIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(mainGDRID1_0, mainGDRID2_1, mainGDRNUM_2, pageable);
    else 
      slice = clNoMapRepos.findAllByMainGDRID1IsAndMainGDRID2IsAndMainGDRNUMIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(mainGDRID1_0, mainGDRID2_1, mainGDRNUM_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClNoMap> findNewClNo(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClNoMap> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findNewClNo " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = clNoMapReposDay.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clNoMapReposMon.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clNoMapReposHist.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(clCode1_0, clCode2_1, clNo_2, pageable);
    else 
      slice = clNoMapRepos.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(clCode1_0, clCode2_1, clNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ClNoMap holdById(ClNoMapId clNoMapId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + clNoMapId);
    Optional<ClNoMap> clNoMap = null;
    if (dbName.equals(ContentName.onDay))
      clNoMap = clNoMapReposDay.findByClNoMapId(clNoMapId);
    else if (dbName.equals(ContentName.onMon))
      clNoMap = clNoMapReposMon.findByClNoMapId(clNoMapId);
    else if (dbName.equals(ContentName.onHist))
      clNoMap = clNoMapReposHist.findByClNoMapId(clNoMapId);
    else 
      clNoMap = clNoMapRepos.findByClNoMapId(clNoMapId);
    return clNoMap.isPresent() ? clNoMap.get() : null;
  }

  @Override
  public ClNoMap holdById(ClNoMap clNoMap, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + clNoMap.getClNoMapId());
    Optional<ClNoMap> clNoMapT = null;
    if (dbName.equals(ContentName.onDay))
      clNoMapT = clNoMapReposDay.findByClNoMapId(clNoMap.getClNoMapId());
    else if (dbName.equals(ContentName.onMon))
      clNoMapT = clNoMapReposMon.findByClNoMapId(clNoMap.getClNoMapId());
    else if (dbName.equals(ContentName.onHist))
      clNoMapT = clNoMapReposHist.findByClNoMapId(clNoMap.getClNoMapId());
    else 
      clNoMapT = clNoMapRepos.findByClNoMapId(clNoMap.getClNoMapId());
    return clNoMapT.isPresent() ? clNoMapT.get() : null;
  }

  @Override
  public ClNoMap insert(ClNoMap clNoMap, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + clNoMap.getClNoMapId());
    if (this.findById(clNoMap.getClNoMapId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      clNoMap.setCreateEmpNo(empNot);

    if(clNoMap.getLastUpdateEmpNo() == null || clNoMap.getLastUpdateEmpNo().isEmpty())
      clNoMap.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clNoMapReposDay.saveAndFlush(clNoMap);	
    else if (dbName.equals(ContentName.onMon))
      return clNoMapReposMon.saveAndFlush(clNoMap);
    else if (dbName.equals(ContentName.onHist))
      return clNoMapReposHist.saveAndFlush(clNoMap);
    else 
    return clNoMapRepos.saveAndFlush(clNoMap);
  }

  @Override
  public ClNoMap update(ClNoMap clNoMap, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + clNoMap.getClNoMapId());
    if (!empNot.isEmpty())
      clNoMap.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clNoMapReposDay.saveAndFlush(clNoMap);	
    else if (dbName.equals(ContentName.onMon))
      return clNoMapReposMon.saveAndFlush(clNoMap);
    else if (dbName.equals(ContentName.onHist))
      return clNoMapReposHist.saveAndFlush(clNoMap);
    else 
    return clNoMapRepos.saveAndFlush(clNoMap);
  }

  @Override
  public ClNoMap update2(ClNoMap clNoMap, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + clNoMap.getClNoMapId());
    if (!empNot.isEmpty())
      clNoMap.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      clNoMapReposDay.saveAndFlush(clNoMap);	
    else if (dbName.equals(ContentName.onMon))
      clNoMapReposMon.saveAndFlush(clNoMap);
    else if (dbName.equals(ContentName.onHist))
        clNoMapReposHist.saveAndFlush(clNoMap);
    else 
      clNoMapRepos.saveAndFlush(clNoMap);	
    return this.findById(clNoMap.getClNoMapId());
  }

  @Override
  public void delete(ClNoMap clNoMap, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + clNoMap.getClNoMapId());
    if (dbName.equals(ContentName.onDay)) {
      clNoMapReposDay.delete(clNoMap);	
      clNoMapReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clNoMapReposMon.delete(clNoMap);	
      clNoMapReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clNoMapReposHist.delete(clNoMap);
      clNoMapReposHist.flush();
    }
    else {
      clNoMapRepos.delete(clNoMap);
      clNoMapRepos.flush();
    }
   }

  @Override
  public void insertAll(List<ClNoMap> clNoMap, TitaVo... titaVo) throws DBException {
    if (clNoMap == null || clNoMap.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (ClNoMap t : clNoMap){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      clNoMap = clNoMapReposDay.saveAll(clNoMap);	
      clNoMapReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clNoMap = clNoMapReposMon.saveAll(clNoMap);	
      clNoMapReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clNoMap = clNoMapReposHist.saveAll(clNoMap);
      clNoMapReposHist.flush();
    }
    else {
      clNoMap = clNoMapRepos.saveAll(clNoMap);
      clNoMapRepos.flush();
    }
    }

  @Override
  public void updateAll(List<ClNoMap> clNoMap, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (clNoMap == null || clNoMap.size() == 0)
      throw new DBException(6);

    for (ClNoMap t : clNoMap) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      clNoMap = clNoMapReposDay.saveAll(clNoMap);	
      clNoMapReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clNoMap = clNoMapReposMon.saveAll(clNoMap);	
      clNoMapReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clNoMap = clNoMapReposHist.saveAll(clNoMap);
      clNoMapReposHist.flush();
    }
    else {
      clNoMap = clNoMapRepos.saveAll(clNoMap);
      clNoMapRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<ClNoMap> clNoMap, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (clNoMap == null || clNoMap.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      clNoMapReposDay.deleteAll(clNoMap);	
      clNoMapReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clNoMapReposMon.deleteAll(clNoMap);	
      clNoMapReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clNoMapReposHist.deleteAll(clNoMap);
      clNoMapReposHist.flush();
    }
    else {
      clNoMapRepos.deleteAll(clNoMap);
      clNoMapRepos.flush();
    }
  }

}
