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
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClFacId;
import com.st1.itx.db.repository.online.ClFacRepository;
import com.st1.itx.db.repository.day.ClFacRepositoryDay;
import com.st1.itx.db.repository.mon.ClFacRepositoryMon;
import com.st1.itx.db.repository.hist.ClFacRepositoryHist;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clFacService")
@Repository
public class ClFacServiceImpl extends ASpringJpaParm implements ClFacService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private ClFacRepository clFacRepos;

  @Autowired
  private ClFacRepositoryDay clFacReposDay;

  @Autowired
  private ClFacRepositoryMon clFacReposMon;

  @Autowired
  private ClFacRepositoryHist clFacReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(clFacRepos);
    org.junit.Assert.assertNotNull(clFacReposDay);
    org.junit.Assert.assertNotNull(clFacReposMon);
    org.junit.Assert.assertNotNull(clFacReposHist);
  }

  @Override
  public ClFac findById(ClFacId clFacId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + clFacId);
    Optional<ClFac> clFac = null;
    if (dbName.equals(ContentName.onDay))
      clFac = clFacReposDay.findById(clFacId);
    else if (dbName.equals(ContentName.onMon))
      clFac = clFacReposMon.findById(clFacId);
    else if (dbName.equals(ContentName.onHist))
      clFac = clFacReposHist.findById(clFacId);
    else 
      clFac = clFacRepos.findById(clFacId);
    ClFac obj = clFac.isPresent() ? clFac.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<ClFac> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClFac> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "ApproveNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "ApproveNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = clFacReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clFacReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clFacReposHist.findAll(pageable);
    else 
      slice = clFacRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClFac> clCode1Eq(int clCode1_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClFac> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("clCode1Eq " + dbName + " : " + "clCode1_0 : " + clCode1_0);
    if (dbName.equals(ContentName.onDay))
      slice = clFacReposDay.findAllByClCode1IsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clFacReposMon.findAllByClCode1IsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clFacReposHist.findAllByClCode1IsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, pageable);
    else 
      slice = clFacRepos.findAllByClCode1IsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClFac> clCode2Eq(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClFac> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("clCode2Eq " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1);
    if (dbName.equals(ContentName.onDay))
      slice = clFacReposDay.findAllByClCode1IsAndClCode2IsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode2_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clFacReposMon.findAllByClCode1IsAndClCode2IsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode2_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clFacReposHist.findAllByClCode1IsAndClCode2IsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode2_1, pageable);
    else 
      slice = clFacRepos.findAllByClCode1IsAndClCode2IsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode2_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClFac> clNoEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClFac> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("clNoEq " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = clFacReposDay.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clFacReposMon.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clFacReposHist.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode2_1, clNo_2, pageable);
    else 
      slice = clFacRepos.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode2_1, clNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClFac> approveNoEq(int approveNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClFac> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("approveNoEq " + dbName + " : " + "approveNo_0 : " + approveNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = clFacReposDay.findAllByApproveNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(approveNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clFacReposMon.findAllByApproveNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(approveNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clFacReposHist.findAllByApproveNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(approveNo_0, pageable);
    else 
      slice = clFacRepos.findAllByApproveNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(approveNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClFac> facmNoEq(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClFac> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("facmNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = clFacReposDay.findAllByCustNoIsAndFacmNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clFacReposMon.findAllByCustNoIsAndFacmNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clFacReposHist.findAllByCustNoIsAndFacmNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(custNo_0, facmNo_1, pageable);
    else 
      slice = clFacRepos.findAllByCustNoIsAndFacmNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(custNo_0, facmNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClFac> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClFac> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custNoEq " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = clFacReposDay.findAllByCustNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clFacReposMon.findAllByCustNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clFacReposHist.findAllByCustNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(custNo_0, pageable);
    else 
      slice = clFacRepos.findAllByCustNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClFac> findRange(int approveNo_0, int approveNo_1, int facmNo_2, int facmNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClFac> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findRange " + dbName + " : " + "approveNo_0 : " + approveNo_0 + " approveNo_1 : " +  approveNo_1 + " facmNo_2 : " +  facmNo_2 + " facmNo_3 : " +  facmNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = clFacReposDay.findAllByApproveNoGreaterThanEqualAndApproveNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(approveNo_0, approveNo_1, facmNo_2, facmNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clFacReposMon.findAllByApproveNoGreaterThanEqualAndApproveNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(approveNo_0, approveNo_1, facmNo_2, facmNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clFacReposHist.findAllByApproveNoGreaterThanEqualAndApproveNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(approveNo_0, approveNo_1, facmNo_2, facmNo_3, pageable);
    else 
      slice = clFacRepos.findAllByApproveNoGreaterThanEqualAndApproveNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(approveNo_0, approveNo_1, facmNo_2, facmNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClFac> selectForL2038(int clCode1_0, int clCode2_1, int clNo_2, int approveNo_3, int approveNo_4, int custNo_5, int custNo_6, int facmNo_7, int facmNo_8, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClFac> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectForL2038 " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2 + " approveNo_3 : " +  approveNo_3 + " approveNo_4 : " +  approveNo_4 + " custNo_5 : " +  custNo_5 + " custNo_6 : " +  custNo_6 + " facmNo_7 : " +  facmNo_7 + " facmNo_8 : " +  facmNo_8);
    if (dbName.equals(ContentName.onDay))
      slice = clFacReposDay.findAllByClCode1IsAndClCode2IsAndClNoIsAndApproveNoGreaterThanEqualAndApproveNoLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode2_1, clNo_2, approveNo_3, approveNo_4, custNo_5, custNo_6, facmNo_7, facmNo_8, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clFacReposMon.findAllByClCode1IsAndClCode2IsAndClNoIsAndApproveNoGreaterThanEqualAndApproveNoLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode2_1, clNo_2, approveNo_3, approveNo_4, custNo_5, custNo_6, facmNo_7, facmNo_8, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clFacReposHist.findAllByClCode1IsAndClCode2IsAndClNoIsAndApproveNoGreaterThanEqualAndApproveNoLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode2_1, clNo_2, approveNo_3, approveNo_4, custNo_5, custNo_6, facmNo_7, facmNo_8, pageable);
    else 
      slice = clFacRepos.findAllByClCode1IsAndClCode2IsAndClNoIsAndApproveNoGreaterThanEqualAndApproveNoLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode2_1, clNo_2, approveNo_3, approveNo_4, custNo_5, custNo_6, facmNo_7, facmNo_8, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClFac> selectForL2049(int clCode1_0, int clCode1_1, int clCode2_2, int clCode2_3, int clNo_4, int clNo_5, int custNo_6, int custNo_7, int facmNo_8, int facmNo_9, int approveNo_10, int approveNo_11, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClFac> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectForL2049 " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode1_1 : " +  clCode1_1 + " clCode2_2 : " +  clCode2_2 + " clCode2_3 : " +  clCode2_3 + " clNo_4 : " +  clNo_4 + " clNo_5 : " +  clNo_5 + " custNo_6 : " +  custNo_6 + " custNo_7 : " +  custNo_7 + " facmNo_8 : " +  facmNo_8 + " facmNo_9 : " +  facmNo_9 + " approveNo_10 : " +  approveNo_10 + " approveNo_11 : " +  approveNo_11);
    if (dbName.equals(ContentName.onDay))
      slice = clFacReposDay.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndApproveNoGreaterThanEqualAndApproveNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode1_1, clCode2_2, clCode2_3, clNo_4, clNo_5, custNo_6, custNo_7, facmNo_8, facmNo_9, approveNo_10, approveNo_11, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clFacReposMon.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndApproveNoGreaterThanEqualAndApproveNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode1_1, clCode2_2, clCode2_3, clNo_4, clNo_5, custNo_6, custNo_7, facmNo_8, facmNo_9, approveNo_10, approveNo_11, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clFacReposHist.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndApproveNoGreaterThanEqualAndApproveNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode1_1, clCode2_2, clCode2_3, clNo_4, clNo_5, custNo_6, custNo_7, facmNo_8, facmNo_9, approveNo_10, approveNo_11, pageable);
    else 
      slice = clFacRepos.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndApproveNoGreaterThanEqualAndApproveNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode1_1, clCode2_2, clCode2_3, clNo_4, clNo_5, custNo_6, custNo_7, facmNo_8, facmNo_9, approveNo_10, approveNo_11, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClFac> selectForL2017CustNo(int custNo_0, int facmNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClFac> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectForL2017CustNo " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " facmNo_2 : " +  facmNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = clFacReposDay.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clFacReposMon.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clFacReposHist.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);
    else 
      slice = clFacRepos.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ClFac mainClNoFirst(int custNo_0, int facmNo_1, String mainFlag_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("mainClNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " mainFlag_2 : " +  mainFlag_2);
    Optional<ClFac> clFacT = null;
    if (dbName.equals(ContentName.onDay))
      clFacT = clFacReposDay.findTopByCustNoIsAndFacmNoIsAndMainFlagIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(custNo_0, facmNo_1, mainFlag_2);
    else if (dbName.equals(ContentName.onMon))
      clFacT = clFacReposMon.findTopByCustNoIsAndFacmNoIsAndMainFlagIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(custNo_0, facmNo_1, mainFlag_2);
    else if (dbName.equals(ContentName.onHist))
      clFacT = clFacReposHist.findTopByCustNoIsAndFacmNoIsAndMainFlagIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(custNo_0, facmNo_1, mainFlag_2);
    else 
      clFacT = clFacRepos.findTopByCustNoIsAndFacmNoIsAndMainFlagIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(custNo_0, facmNo_1, mainFlag_2);

    return clFacT.isPresent() ? clFacT.get() : null;
  }

  @Override
  public Slice<ClFac> selectForL5064(int clCode1_0, int clCode2_1, int clNo_2, int custNo_3, int facmNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClFac> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectForL5064 " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2 + " custNo_3 : " +  custNo_3 + " facmNo_4 : " +  facmNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = clFacReposDay.findAllByClCode1IsAndClCode2IsAndClNoIsAndCustNoIsAndFacmNoIs(clCode1_0, clCode2_1, clNo_2, custNo_3, facmNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clFacReposMon.findAllByClCode1IsAndClCode2IsAndClNoIsAndCustNoIsAndFacmNoIs(clCode1_0, clCode2_1, clNo_2, custNo_3, facmNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clFacReposHist.findAllByClCode1IsAndClCode2IsAndClNoIsAndCustNoIsAndFacmNoIs(clCode1_0, clCode2_1, clNo_2, custNo_3, facmNo_4, pageable);
    else 
      slice = clFacRepos.findAllByClCode1IsAndClCode2IsAndClNoIsAndCustNoIsAndFacmNoIs(clCode1_0, clCode2_1, clNo_2, custNo_3, facmNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ClFac clMainFirst(int clCode1_0, int clCode2_1, int clNo_2, String mainFlag_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("clMainFirst " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2 + " mainFlag_3 : " +  mainFlag_3);
    Optional<ClFac> clFacT = null;
    if (dbName.equals(ContentName.onDay))
      clFacT = clFacReposDay.findTopByClCode1IsAndClCode2IsAndClNoIsAndMainFlagIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode2_1, clNo_2, mainFlag_3);
    else if (dbName.equals(ContentName.onMon))
      clFacT = clFacReposMon.findTopByClCode1IsAndClCode2IsAndClNoIsAndMainFlagIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode2_1, clNo_2, mainFlag_3);
    else if (dbName.equals(ContentName.onHist))
      clFacT = clFacReposHist.findTopByClCode1IsAndClCode2IsAndClNoIsAndMainFlagIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode2_1, clNo_2, mainFlag_3);
    else 
      clFacT = clFacRepos.findTopByClCode1IsAndClCode2IsAndClNoIsAndMainFlagIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(clCode1_0, clCode2_1, clNo_2, mainFlag_3);

    return clFacT.isPresent() ? clFacT.get() : null;
  }

  @Override
  public ClFac holdById(ClFacId clFacId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clFacId);
    Optional<ClFac> clFac = null;
    if (dbName.equals(ContentName.onDay))
      clFac = clFacReposDay.findByClFacId(clFacId);
    else if (dbName.equals(ContentName.onMon))
      clFac = clFacReposMon.findByClFacId(clFacId);
    else if (dbName.equals(ContentName.onHist))
      clFac = clFacReposHist.findByClFacId(clFacId);
    else 
      clFac = clFacRepos.findByClFacId(clFacId);
    return clFac.isPresent() ? clFac.get() : null;
  }

  @Override
  public ClFac holdById(ClFac clFac, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clFac.getClFacId());
    Optional<ClFac> clFacT = null;
    if (dbName.equals(ContentName.onDay))
      clFacT = clFacReposDay.findByClFacId(clFac.getClFacId());
    else if (dbName.equals(ContentName.onMon))
      clFacT = clFacReposMon.findByClFacId(clFac.getClFacId());
    else if (dbName.equals(ContentName.onHist))
      clFacT = clFacReposHist.findByClFacId(clFac.getClFacId());
    else 
      clFacT = clFacRepos.findByClFacId(clFac.getClFacId());
    return clFacT.isPresent() ? clFacT.get() : null;
  }

  @Override
  public ClFac insert(ClFac clFac, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + clFac.getClFacId());
    if (this.findById(clFac.getClFacId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      clFac.setCreateEmpNo(empNot);

    if(clFac.getLastUpdateEmpNo() == null || clFac.getLastUpdateEmpNo().isEmpty())
      clFac.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clFacReposDay.saveAndFlush(clFac);	
    else if (dbName.equals(ContentName.onMon))
      return clFacReposMon.saveAndFlush(clFac);
    else if (dbName.equals(ContentName.onHist))
      return clFacReposHist.saveAndFlush(clFac);
    else 
    return clFacRepos.saveAndFlush(clFac);
  }

  @Override
  public ClFac update(ClFac clFac, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + clFac.getClFacId());
    if (!empNot.isEmpty())
      clFac.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clFacReposDay.saveAndFlush(clFac);	
    else if (dbName.equals(ContentName.onMon))
      return clFacReposMon.saveAndFlush(clFac);
    else if (dbName.equals(ContentName.onHist))
      return clFacReposHist.saveAndFlush(clFac);
    else 
    return clFacRepos.saveAndFlush(clFac);
  }

  @Override
  public ClFac update2(ClFac clFac, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + clFac.getClFacId());
    if (!empNot.isEmpty())
      clFac.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      clFacReposDay.saveAndFlush(clFac);	
    else if (dbName.equals(ContentName.onMon))
      clFacReposMon.saveAndFlush(clFac);
    else if (dbName.equals(ContentName.onHist))
        clFacReposHist.saveAndFlush(clFac);
    else 
      clFacRepos.saveAndFlush(clFac);	
    return this.findById(clFac.getClFacId());
  }

  @Override
  public void delete(ClFac clFac, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + clFac.getClFacId());
    if (dbName.equals(ContentName.onDay)) {
      clFacReposDay.delete(clFac);	
      clFacReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clFacReposMon.delete(clFac);	
      clFacReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clFacReposHist.delete(clFac);
      clFacReposHist.flush();
    }
    else {
      clFacRepos.delete(clFac);
      clFacRepos.flush();
    }
   }

  @Override
  public void insertAll(List<ClFac> clFac, TitaVo... titaVo) throws DBException {
    if (clFac == null || clFac.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (ClFac t : clFac){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      clFac = clFacReposDay.saveAll(clFac);	
      clFacReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clFac = clFacReposMon.saveAll(clFac);	
      clFacReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clFac = clFacReposHist.saveAll(clFac);
      clFacReposHist.flush();
    }
    else {
      clFac = clFacRepos.saveAll(clFac);
      clFacRepos.flush();
    }
    }

  @Override
  public void updateAll(List<ClFac> clFac, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (clFac == null || clFac.size() == 0)
      throw new DBException(6);

    for (ClFac t : clFac) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      clFac = clFacReposDay.saveAll(clFac);	
      clFacReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clFac = clFacReposMon.saveAll(clFac);	
      clFacReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clFac = clFacReposHist.saveAll(clFac);
      clFacReposHist.flush();
    }
    else {
      clFac = clFacRepos.saveAll(clFac);
      clFacRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<ClFac> clFac, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (clFac == null || clFac.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      clFacReposDay.deleteAll(clFac);	
      clFacReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clFacReposMon.deleteAll(clFac);	
      clFacReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clFacReposHist.deleteAll(clFac);
      clFacReposHist.flush();
    }
    else {
      clFacRepos.deleteAll(clFac);
      clFacRepos.flush();
    }
  }

}
