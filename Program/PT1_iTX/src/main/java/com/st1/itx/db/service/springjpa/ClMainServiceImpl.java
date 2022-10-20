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
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.repository.online.ClMainRepository;
import com.st1.itx.db.repository.day.ClMainRepositoryDay;
import com.st1.itx.db.repository.mon.ClMainRepositoryMon;
import com.st1.itx.db.repository.hist.ClMainRepositoryHist;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clMainService")
@Repository
public class ClMainServiceImpl extends ASpringJpaParm implements ClMainService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private ClMainRepository clMainRepos;

  @Autowired
  private ClMainRepositoryDay clMainReposDay;

  @Autowired
  private ClMainRepositoryMon clMainReposMon;

  @Autowired
  private ClMainRepositoryHist clMainReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(clMainRepos);
    org.junit.Assert.assertNotNull(clMainReposDay);
    org.junit.Assert.assertNotNull(clMainReposMon);
    org.junit.Assert.assertNotNull(clMainReposHist);
  }

  @Override
  public ClMain findById(ClMainId clMainId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + clMainId);
    Optional<ClMain> clMain = null;
    if (dbName.equals(ContentName.onDay))
      clMain = clMainReposDay.findById(clMainId);
    else if (dbName.equals(ContentName.onMon))
      clMain = clMainReposMon.findById(clMainId);
    else if (dbName.equals(ContentName.onHist))
      clMain = clMainReposHist.findById(clMainId);
    else 
      clMain = clMainRepos.findById(clMainId);
    ClMain obj = clMain.isPresent() ? clMain.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<ClMain> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = clMainReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clMainReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clMainReposHist.findAll(pageable);
    else 
      slice = clMainRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClMain> findClCode1(int clCode1_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findClCode1 " + dbName + " : " + "clCode1_0 : " + clCode1_0);
    if (dbName.equals(ContentName.onDay))
      slice = clMainReposDay.findAllByClCode1IsOrderByClCode2AscClNoAsc(clCode1_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clMainReposMon.findAllByClCode1IsOrderByClCode2AscClNoAsc(clCode1_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clMainReposHist.findAllByClCode1IsOrderByClCode2AscClNoAsc(clCode1_0, pageable);
    else 
      slice = clMainRepos.findAllByClCode1IsOrderByClCode2AscClNoAsc(clCode1_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClMain> findClCode2(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findClCode2 " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1);
    if (dbName.equals(ContentName.onDay))
      slice = clMainReposDay.findAllByClCode1IsAndClCode2IsOrderByClNoAsc(clCode1_0, clCode2_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clMainReposMon.findAllByClCode1IsAndClCode2IsOrderByClNoAsc(clCode1_0, clCode2_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clMainReposHist.findAllByClCode1IsAndClCode2IsOrderByClNoAsc(clCode1_0, clCode2_1, pageable);
    else 
      slice = clMainRepos.findAllByClCode1IsAndClCode2IsOrderByClNoAsc(clCode1_0, clCode2_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClMain> findClNo(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findClNo " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = clMainReposDay.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clMainReposMon.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clMainReposHist.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
    else 
      slice = clMainRepos.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClMain> findCustUKey(String custUKey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCustUKey " + dbName + " : " + "custUKey_0 : " + custUKey_0);
    if (dbName.equals(ContentName.onDay))
      slice = clMainReposDay.findAllByCustUKeyIs(custUKey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clMainReposMon.findAllByCustUKeyIs(custUKey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clMainReposHist.findAllByCustUKeyIs(custUKey_0, pageable);
    else 
      slice = clMainRepos.findAllByCustUKeyIs(custUKey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClMain> findRange(int clCode1_0, int clCode1_1, String clTypeCode_2, String clTypeCode_3, int clNo_4, int clNo_5, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findRange " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode1_1 : " +  clCode1_1 + " clTypeCode_2 : " +  clTypeCode_2 + " clTypeCode_3 : " +  clTypeCode_3 + " clNo_4 : " +  clNo_4 + " clNo_5 : " +  clNo_5);
    if (dbName.equals(ContentName.onDay))
      slice = clMainReposDay.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClTypeCodeGreaterThanEqualAndClTypeCodeLessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqual(clCode1_0, clCode1_1, clTypeCode_2, clTypeCode_3, clNo_4, clNo_5, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clMainReposMon.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClTypeCodeGreaterThanEqualAndClTypeCodeLessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqual(clCode1_0, clCode1_1, clTypeCode_2, clTypeCode_3, clNo_4, clNo_5, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clMainReposHist.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClTypeCodeGreaterThanEqualAndClTypeCodeLessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqual(clCode1_0, clCode1_1, clTypeCode_2, clTypeCode_3, clNo_4, clNo_5, pageable);
    else 
      slice = clMainRepos.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClTypeCodeGreaterThanEqualAndClTypeCodeLessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqual(clCode1_0, clCode1_1, clTypeCode_2, clTypeCode_3, clNo_4, clNo_5, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClMain> selectForL2038(int clCode1_0, int clCode1_1, String clTypeCode_2, String clTypeCode_3, int clNo_4, int clNo_5, String custUKey_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectForL2038 " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode1_1 : " +  clCode1_1 + " clTypeCode_2 : " +  clTypeCode_2 + " clTypeCode_3 : " +  clTypeCode_3 + " clNo_4 : " +  clNo_4 + " clNo_5 : " +  clNo_5 + " custUKey_6 : " +  custUKey_6);
    if (dbName.equals(ContentName.onDay))
      slice = clMainReposDay.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClTypeCodeGreaterThanEqualAndClTypeCodeLessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualAndCustUKeyIs(clCode1_0, clCode1_1, clTypeCode_2, clTypeCode_3, clNo_4, clNo_5, custUKey_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clMainReposMon.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClTypeCodeGreaterThanEqualAndClTypeCodeLessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualAndCustUKeyIs(clCode1_0, clCode1_1, clTypeCode_2, clTypeCode_3, clNo_4, clNo_5, custUKey_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clMainReposHist.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClTypeCodeGreaterThanEqualAndClTypeCodeLessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualAndCustUKeyIs(clCode1_0, clCode1_1, clTypeCode_2, clTypeCode_3, clNo_4, clNo_5, custUKey_6, pageable);
    else 
      slice = clMainRepos.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClTypeCodeGreaterThanEqualAndClTypeCodeLessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualAndCustUKeyIs(clCode1_0, clCode1_1, clTypeCode_2, clTypeCode_3, clNo_4, clNo_5, custUKey_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClMain> selectForL2049(int clCode1_0, int clCode1_1, int clCode2_2, int clCode2_3, int clNo_4, int clNo_5, String clTypeCode_6, String clTypeCode_7, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectForL2049 " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode1_1 : " +  clCode1_1 + " clCode2_2 : " +  clCode2_2 + " clCode2_3 : " +  clCode2_3 + " clNo_4 : " +  clNo_4 + " clNo_5 : " +  clNo_5 + " clTypeCode_6 : " +  clTypeCode_6 + " clTypeCode_7 : " +  clTypeCode_7);
    if (dbName.equals(ContentName.onDay))
      slice = clMainReposDay.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualAndClTypeCodeGreaterThanEqualAndClTypeCodeLessThanEqual(clCode1_0, clCode1_1, clCode2_2, clCode2_3, clNo_4, clNo_5, clTypeCode_6, clTypeCode_7, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clMainReposMon.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualAndClTypeCodeGreaterThanEqualAndClTypeCodeLessThanEqual(clCode1_0, clCode1_1, clCode2_2, clCode2_3, clNo_4, clNo_5, clTypeCode_6, clTypeCode_7, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clMainReposHist.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualAndClTypeCodeGreaterThanEqualAndClTypeCodeLessThanEqual(clCode1_0, clCode1_1, clCode2_2, clCode2_3, clNo_4, clNo_5, clTypeCode_6, clTypeCode_7, pageable);
    else 
      slice = clMainRepos.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualAndClTypeCodeGreaterThanEqualAndClTypeCodeLessThanEqual(clCode1_0, clCode1_1, clCode2_2, clCode2_3, clNo_4, clNo_5, clTypeCode_6, clTypeCode_7, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ClMain lastClNoFirst(int clCode1_0, int clCode2_1, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("lastClNoFirst " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1);
    Optional<ClMain> clMainT = null;
    if (dbName.equals(ContentName.onDay))
      clMainT = clMainReposDay.findTopByClCode1IsAndClCode2IsOrderByClNoDesc(clCode1_0, clCode2_1);
    else if (dbName.equals(ContentName.onMon))
      clMainT = clMainReposMon.findTopByClCode1IsAndClCode2IsOrderByClNoDesc(clCode1_0, clCode2_1);
    else if (dbName.equals(ContentName.onHist))
      clMainT = clMainReposHist.findTopByClCode1IsAndClCode2IsOrderByClNoDesc(clCode1_0, clCode2_1);
    else 
      clMainT = clMainRepos.findTopByClCode1IsAndClCode2IsOrderByClNoDesc(clCode1_0, clCode2_1);

    return clMainT.isPresent() ? clMainT.get() : null;
  }

  @Override
  public Slice<ClMain> selectForL1001(int clCode1_0, int clCode1_1, String custUKey_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectForL1001 " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode1_1 : " +  clCode1_1 + " custUKey_2 : " +  custUKey_2);
    if (dbName.equals(ContentName.onDay))
      slice = clMainReposDay.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndCustUKeyIsOrderByClCode1AscClCode2AscClNoAsc(clCode1_0, clCode1_1, custUKey_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clMainReposMon.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndCustUKeyIsOrderByClCode1AscClCode2AscClNoAsc(clCode1_0, clCode1_1, custUKey_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clMainReposHist.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndCustUKeyIsOrderByClCode1AscClCode2AscClNoAsc(clCode1_0, clCode1_1, custUKey_2, pageable);
    else 
      slice = clMainRepos.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndCustUKeyIsOrderByClCode1AscClCode2AscClNoAsc(clCode1_0, clCode1_1, custUKey_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ClMain holdById(ClMainId clMainId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clMainId);
    Optional<ClMain> clMain = null;
    if (dbName.equals(ContentName.onDay))
      clMain = clMainReposDay.findByClMainId(clMainId);
    else if (dbName.equals(ContentName.onMon))
      clMain = clMainReposMon.findByClMainId(clMainId);
    else if (dbName.equals(ContentName.onHist))
      clMain = clMainReposHist.findByClMainId(clMainId);
    else 
      clMain = clMainRepos.findByClMainId(clMainId);
    return clMain.isPresent() ? clMain.get() : null;
  }

  @Override
  public ClMain holdById(ClMain clMain, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clMain.getClMainId());
    Optional<ClMain> clMainT = null;
    if (dbName.equals(ContentName.onDay))
      clMainT = clMainReposDay.findByClMainId(clMain.getClMainId());
    else if (dbName.equals(ContentName.onMon))
      clMainT = clMainReposMon.findByClMainId(clMain.getClMainId());
    else if (dbName.equals(ContentName.onHist))
      clMainT = clMainReposHist.findByClMainId(clMain.getClMainId());
    else 
      clMainT = clMainRepos.findByClMainId(clMain.getClMainId());
    return clMainT.isPresent() ? clMainT.get() : null;
  }

  @Override
  public ClMain insert(ClMain clMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + clMain.getClMainId());
    if (this.findById(clMain.getClMainId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      clMain.setCreateEmpNo(empNot);

    if(clMain.getLastUpdateEmpNo() == null || clMain.getLastUpdateEmpNo().isEmpty())
      clMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clMainReposDay.saveAndFlush(clMain);	
    else if (dbName.equals(ContentName.onMon))
      return clMainReposMon.saveAndFlush(clMain);
    else if (dbName.equals(ContentName.onHist))
      return clMainReposHist.saveAndFlush(clMain);
    else 
    return clMainRepos.saveAndFlush(clMain);
  }

  @Override
  public ClMain update(ClMain clMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + clMain.getClMainId());
    if (!empNot.isEmpty())
      clMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clMainReposDay.saveAndFlush(clMain);	
    else if (dbName.equals(ContentName.onMon))
      return clMainReposMon.saveAndFlush(clMain);
    else if (dbName.equals(ContentName.onHist))
      return clMainReposHist.saveAndFlush(clMain);
    else 
    return clMainRepos.saveAndFlush(clMain);
  }

  @Override
  public ClMain update2(ClMain clMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + clMain.getClMainId());
    if (!empNot.isEmpty())
      clMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      clMainReposDay.saveAndFlush(clMain);	
    else if (dbName.equals(ContentName.onMon))
      clMainReposMon.saveAndFlush(clMain);
    else if (dbName.equals(ContentName.onHist))
        clMainReposHist.saveAndFlush(clMain);
    else 
      clMainRepos.saveAndFlush(clMain);	
    return this.findById(clMain.getClMainId());
  }

  @Override
  public void delete(ClMain clMain, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + clMain.getClMainId());
    if (dbName.equals(ContentName.onDay)) {
      clMainReposDay.delete(clMain);	
      clMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clMainReposMon.delete(clMain);	
      clMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clMainReposHist.delete(clMain);
      clMainReposHist.flush();
    }
    else {
      clMainRepos.delete(clMain);
      clMainRepos.flush();
    }
   }

  @Override
  public void insertAll(List<ClMain> clMain, TitaVo... titaVo) throws DBException {
    if (clMain == null || clMain.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (ClMain t : clMain){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      clMain = clMainReposDay.saveAll(clMain);	
      clMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clMain = clMainReposMon.saveAll(clMain);	
      clMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clMain = clMainReposHist.saveAll(clMain);
      clMainReposHist.flush();
    }
    else {
      clMain = clMainRepos.saveAll(clMain);
      clMainRepos.flush();
    }
    }

  @Override
  public void updateAll(List<ClMain> clMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (clMain == null || clMain.size() == 0)
      throw new DBException(6);

    for (ClMain t : clMain) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      clMain = clMainReposDay.saveAll(clMain);	
      clMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clMain = clMainReposMon.saveAll(clMain);	
      clMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clMain = clMainReposHist.saveAll(clMain);
      clMainReposHist.flush();
    }
    else {
      clMain = clMainRepos.saveAll(clMain);
      clMainRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<ClMain> clMain, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (clMain == null || clMain.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      clMainReposDay.deleteAll(clMain);	
      clMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clMainReposMon.deleteAll(clMain);	
      clMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clMainReposHist.deleteAll(clMain);
      clMainReposHist.flush();
    }
    else {
      clMainRepos.deleteAll(clMain);
      clMainRepos.flush();
    }
  }

}
