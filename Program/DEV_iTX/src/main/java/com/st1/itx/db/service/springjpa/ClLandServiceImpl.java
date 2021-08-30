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
import com.st1.itx.db.domain.ClLand;
import com.st1.itx.db.domain.ClLandId;
import com.st1.itx.db.repository.online.ClLandRepository;
import com.st1.itx.db.repository.day.ClLandRepositoryDay;
import com.st1.itx.db.repository.mon.ClLandRepositoryMon;
import com.st1.itx.db.repository.hist.ClLandRepositoryHist;
import com.st1.itx.db.service.ClLandService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clLandService")
@Repository
public class ClLandServiceImpl extends ASpringJpaParm implements ClLandService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private ClLandRepository clLandRepos;

  @Autowired
  private ClLandRepositoryDay clLandReposDay;

  @Autowired
  private ClLandRepositoryMon clLandReposMon;

  @Autowired
  private ClLandRepositoryHist clLandReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(clLandRepos);
    org.junit.Assert.assertNotNull(clLandReposDay);
    org.junit.Assert.assertNotNull(clLandReposMon);
    org.junit.Assert.assertNotNull(clLandReposHist);
  }

  @Override
  public ClLand findById(ClLandId clLandId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + clLandId);
    Optional<ClLand> clLand = null;
    if (dbName.equals(ContentName.onDay))
      clLand = clLandReposDay.findById(clLandId);
    else if (dbName.equals(ContentName.onMon))
      clLand = clLandReposMon.findById(clLandId);
    else if (dbName.equals(ContentName.onHist))
      clLand = clLandReposHist.findById(clLandId);
    else 
      clLand = clLandRepos.findById(clLandId);
    ClLand obj = clLand.isPresent() ? clLand.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<ClLand> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClLand> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "LandSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "LandSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = clLandReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clLandReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clLandReposHist.findAll(pageable);
    else 
      slice = clLandRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClLand> findClCode1(int clCode1_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClLand> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findClCode1 " + dbName + " : " + "clCode1_0 : " + clCode1_0);
    if (dbName.equals(ContentName.onDay))
      slice = clLandReposDay.findAllByClCode1Is(clCode1_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clLandReposMon.findAllByClCode1Is(clCode1_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clLandReposHist.findAllByClCode1Is(clCode1_0, pageable);
    else 
      slice = clLandRepos.findAllByClCode1Is(clCode1_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClLand> findClCode2(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClLand> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findClCode2 " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1);
    if (dbName.equals(ContentName.onDay))
      slice = clLandReposDay.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clLandReposMon.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clLandReposHist.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);
    else 
      slice = clLandRepos.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClLand> findClNo(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClLand> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findClNo " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = clLandReposDay.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByLandSeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clLandReposMon.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByLandSeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clLandReposHist.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByLandSeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);
    else 
      slice = clLandRepos.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByLandSeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClLand> findIrCode(String cityCode_0, String areaCode_1, String irCode_2, String landNo1_3, String landNo1_4, String landNo2_5, String landNo2_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClLand> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findIrCode " + dbName + " : " + "cityCode_0 : " + cityCode_0 + " areaCode_1 : " +  areaCode_1 + " irCode_2 : " +  irCode_2 + " landNo1_3 : " +  landNo1_3 + " landNo1_4 : " +  landNo1_4 + " landNo2_5 : " +  landNo2_5 + " landNo2_6 : " +  landNo2_6);
    if (dbName.equals(ContentName.onDay))
      slice = clLandReposDay.findAllByCityCodeIsAndAreaCodeIsAndIrCodeIsAndLandNo1GreaterThanEqualAndLandNo1LessThanEqualAndLandNo2GreaterThanEqualAndLandNo2LessThanEqual(cityCode_0, areaCode_1, irCode_2, landNo1_3, landNo1_4, landNo2_5, landNo2_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clLandReposMon.findAllByCityCodeIsAndAreaCodeIsAndIrCodeIsAndLandNo1GreaterThanEqualAndLandNo1LessThanEqualAndLandNo2GreaterThanEqualAndLandNo2LessThanEqual(cityCode_0, areaCode_1, irCode_2, landNo1_3, landNo1_4, landNo2_5, landNo2_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clLandReposHist.findAllByCityCodeIsAndAreaCodeIsAndIrCodeIsAndLandNo1GreaterThanEqualAndLandNo1LessThanEqualAndLandNo2GreaterThanEqualAndLandNo2LessThanEqual(cityCode_0, areaCode_1, irCode_2, landNo1_3, landNo1_4, landNo2_5, landNo2_6, pageable);
    else 
      slice = clLandRepos.findAllByCityCodeIsAndAreaCodeIsAndIrCodeIsAndLandNo1GreaterThanEqualAndLandNo1LessThanEqualAndLandNo2GreaterThanEqualAndLandNo2LessThanEqual(cityCode_0, areaCode_1, irCode_2, landNo1_3, landNo1_4, landNo2_5, landNo2_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClLand> findLandLocationEq(String cityCode_0, String areaCode_1, String irCode_2, String landNo1_3, String landNo2_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClLand> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findLandLocationEq " + dbName + " : " + "cityCode_0 : " + cityCode_0 + " areaCode_1 : " +  areaCode_1 + " irCode_2 : " +  irCode_2 + " landNo1_3 : " +  landNo1_3 + " landNo2_4 : " +  landNo2_4);
    if (dbName.equals(ContentName.onDay))
      slice = clLandReposDay.findAllByCityCodeIsAndAreaCodeIsAndIrCodeIsAndLandNo1IsAndLandNo2IsOrderByClCode1AscClCode2AscClNoAsc(cityCode_0, areaCode_1, irCode_2, landNo1_3, landNo2_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clLandReposMon.findAllByCityCodeIsAndAreaCodeIsAndIrCodeIsAndLandNo1IsAndLandNo2IsOrderByClCode1AscClCode2AscClNoAsc(cityCode_0, areaCode_1, irCode_2, landNo1_3, landNo2_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clLandReposHist.findAllByCityCodeIsAndAreaCodeIsAndIrCodeIsAndLandNo1IsAndLandNo2IsOrderByClCode1AscClCode2AscClNoAsc(cityCode_0, areaCode_1, irCode_2, landNo1_3, landNo2_4, pageable);
    else 
      slice = clLandRepos.findAllByCityCodeIsAndAreaCodeIsAndIrCodeIsAndLandNo1IsAndLandNo2IsOrderByClCode1AscClCode2AscClNoAsc(cityCode_0, areaCode_1, irCode_2, landNo1_3, landNo2_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ClLand findClNoFirst(int clCode1_0, int clCode2_1, int clNo_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findClNoFirst " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2);
    Optional<ClLand> clLandT = null;
    if (dbName.equals(ContentName.onDay))
      clLandT = clLandReposDay.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByLandSeqDesc(clCode1_0, clCode2_1, clNo_2);
    else if (dbName.equals(ContentName.onMon))
      clLandT = clLandReposMon.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByLandSeqDesc(clCode1_0, clCode2_1, clNo_2);
    else if (dbName.equals(ContentName.onHist))
      clLandT = clLandReposHist.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByLandSeqDesc(clCode1_0, clCode2_1, clNo_2);
    else 
      clLandT = clLandRepos.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByLandSeqDesc(clCode1_0, clCode2_1, clNo_2);

    return clLandT.isPresent() ? clLandT.get() : null;
  }

  @Override
  public ClLand holdById(ClLandId clLandId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clLandId);
    Optional<ClLand> clLand = null;
    if (dbName.equals(ContentName.onDay))
      clLand = clLandReposDay.findByClLandId(clLandId);
    else if (dbName.equals(ContentName.onMon))
      clLand = clLandReposMon.findByClLandId(clLandId);
    else if (dbName.equals(ContentName.onHist))
      clLand = clLandReposHist.findByClLandId(clLandId);
    else 
      clLand = clLandRepos.findByClLandId(clLandId);
    return clLand.isPresent() ? clLand.get() : null;
  }

  @Override
  public ClLand holdById(ClLand clLand, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clLand.getClLandId());
    Optional<ClLand> clLandT = null;
    if (dbName.equals(ContentName.onDay))
      clLandT = clLandReposDay.findByClLandId(clLand.getClLandId());
    else if (dbName.equals(ContentName.onMon))
      clLandT = clLandReposMon.findByClLandId(clLand.getClLandId());
    else if (dbName.equals(ContentName.onHist))
      clLandT = clLandReposHist.findByClLandId(clLand.getClLandId());
    else 
      clLandT = clLandRepos.findByClLandId(clLand.getClLandId());
    return clLandT.isPresent() ? clLandT.get() : null;
  }

  @Override
  public ClLand insert(ClLand clLand, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + clLand.getClLandId());
    if (this.findById(clLand.getClLandId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      clLand.setCreateEmpNo(empNot);

    if(clLand.getLastUpdateEmpNo() == null || clLand.getLastUpdateEmpNo().isEmpty())
      clLand.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clLandReposDay.saveAndFlush(clLand);	
    else if (dbName.equals(ContentName.onMon))
      return clLandReposMon.saveAndFlush(clLand);
    else if (dbName.equals(ContentName.onHist))
      return clLandReposHist.saveAndFlush(clLand);
    else 
    return clLandRepos.saveAndFlush(clLand);
  }

  @Override
  public ClLand update(ClLand clLand, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + clLand.getClLandId());
    if (!empNot.isEmpty())
      clLand.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clLandReposDay.saveAndFlush(clLand);	
    else if (dbName.equals(ContentName.onMon))
      return clLandReposMon.saveAndFlush(clLand);
    else if (dbName.equals(ContentName.onHist))
      return clLandReposHist.saveAndFlush(clLand);
    else 
    return clLandRepos.saveAndFlush(clLand);
  }

  @Override
  public ClLand update2(ClLand clLand, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + clLand.getClLandId());
    if (!empNot.isEmpty())
      clLand.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      clLandReposDay.saveAndFlush(clLand);	
    else if (dbName.equals(ContentName.onMon))
      clLandReposMon.saveAndFlush(clLand);
    else if (dbName.equals(ContentName.onHist))
        clLandReposHist.saveAndFlush(clLand);
    else 
      clLandRepos.saveAndFlush(clLand);	
    return this.findById(clLand.getClLandId());
  }

  @Override
  public void delete(ClLand clLand, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + clLand.getClLandId());
    if (dbName.equals(ContentName.onDay)) {
      clLandReposDay.delete(clLand);	
      clLandReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clLandReposMon.delete(clLand);	
      clLandReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clLandReposHist.delete(clLand);
      clLandReposHist.flush();
    }
    else {
      clLandRepos.delete(clLand);
      clLandRepos.flush();
    }
   }

  @Override
  public void insertAll(List<ClLand> clLand, TitaVo... titaVo) throws DBException {
    if (clLand == null || clLand.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (ClLand t : clLand){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      clLand = clLandReposDay.saveAll(clLand);	
      clLandReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clLand = clLandReposMon.saveAll(clLand);	
      clLandReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clLand = clLandReposHist.saveAll(clLand);
      clLandReposHist.flush();
    }
    else {
      clLand = clLandRepos.saveAll(clLand);
      clLandRepos.flush();
    }
    }

  @Override
  public void updateAll(List<ClLand> clLand, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (clLand == null || clLand.size() == 0)
      throw new DBException(6);

    for (ClLand t : clLand) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      clLand = clLandReposDay.saveAll(clLand);	
      clLandReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clLand = clLandReposMon.saveAll(clLand);	
      clLandReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clLand = clLandReposHist.saveAll(clLand);
      clLandReposHist.flush();
    }
    else {
      clLand = clLandRepos.saveAll(clLand);
      clLandRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<ClLand> clLand, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (clLand == null || clLand.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      clLandReposDay.deleteAll(clLand);	
      clLandReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clLandReposMon.deleteAll(clLand);	
      clLandReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clLandReposHist.deleteAll(clLand);
      clLandReposHist.flush();
    }
    else {
      clLandRepos.deleteAll(clLand);
      clLandRepos.flush();
    }
  }

}
