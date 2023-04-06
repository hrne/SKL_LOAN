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
import com.st1.itx.db.domain.ClOtherRightsFac;
import com.st1.itx.db.domain.ClOtherRightsFacId;
import com.st1.itx.db.repository.online.ClOtherRightsFacRepository;
import com.st1.itx.db.repository.day.ClOtherRightsFacRepositoryDay;
import com.st1.itx.db.repository.mon.ClOtherRightsFacRepositoryMon;
import com.st1.itx.db.repository.hist.ClOtherRightsFacRepositoryHist;
import com.st1.itx.db.service.ClOtherRightsFacService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clOtherRightsFacService")
@Repository
public class ClOtherRightsFacServiceImpl extends ASpringJpaParm implements ClOtherRightsFacService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private ClOtherRightsFacRepository clOtherRightsFacRepos;

  @Autowired
  private ClOtherRightsFacRepositoryDay clOtherRightsFacReposDay;

  @Autowired
  private ClOtherRightsFacRepositoryMon clOtherRightsFacReposMon;

  @Autowired
  private ClOtherRightsFacRepositoryHist clOtherRightsFacReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(clOtherRightsFacRepos);
    org.junit.Assert.assertNotNull(clOtherRightsFacReposDay);
    org.junit.Assert.assertNotNull(clOtherRightsFacReposMon);
    org.junit.Assert.assertNotNull(clOtherRightsFacReposHist);
  }

  @Override
  public ClOtherRightsFac findById(ClOtherRightsFacId clOtherRightsFacId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + clOtherRightsFacId);
    Optional<ClOtherRightsFac> clOtherRightsFac = null;
    if (dbName.equals(ContentName.onDay))
      clOtherRightsFac = clOtherRightsFacReposDay.findById(clOtherRightsFacId);
    else if (dbName.equals(ContentName.onMon))
      clOtherRightsFac = clOtherRightsFacReposMon.findById(clOtherRightsFacId);
    else if (dbName.equals(ContentName.onHist))
      clOtherRightsFac = clOtherRightsFacReposHist.findById(clOtherRightsFacId);
    else 
      clOtherRightsFac = clOtherRightsFacRepos.findById(clOtherRightsFacId);
    ClOtherRightsFac obj = clOtherRightsFac.isPresent() ? clOtherRightsFac.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<ClOtherRightsFac> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClOtherRightsFac> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "Seq", "CustNo", "FacmNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "Seq", "CustNo", "FacmNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = clOtherRightsFacReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clOtherRightsFacReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clOtherRightsFacReposHist.findAll(pageable);
    else 
      slice = clOtherRightsFacRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClOtherRightsFac> findClCode1(int clCode1_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClOtherRightsFac> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findClCode1 " + dbName + " : " + "clCode1_0 : " + clCode1_0);
    if (dbName.equals(ContentName.onDay))
      slice = clOtherRightsFacReposDay.findAllByClCode1IsOrderByClCode2AscClNoAscSeqAsc(clCode1_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clOtherRightsFacReposMon.findAllByClCode1IsOrderByClCode2AscClNoAscSeqAsc(clCode1_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clOtherRightsFacReposHist.findAllByClCode1IsOrderByClCode2AscClNoAscSeqAsc(clCode1_0, pageable);
    else 
      slice = clOtherRightsFacRepos.findAllByClCode1IsOrderByClCode2AscClNoAscSeqAsc(clCode1_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClOtherRightsFac> findClCode2(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClOtherRightsFac> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findClCode2 " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1);
    if (dbName.equals(ContentName.onDay))
      slice = clOtherRightsFacReposDay.findAllByClCode1IsAndClCode2IsOrderByClNoAscSeqAsc(clCode1_0, clCode2_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clOtherRightsFacReposMon.findAllByClCode1IsAndClCode2IsOrderByClNoAscSeqAsc(clCode1_0, clCode2_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clOtherRightsFacReposHist.findAllByClCode1IsAndClCode2IsOrderByClNoAscSeqAsc(clCode1_0, clCode2_1, pageable);
    else 
      slice = clOtherRightsFacRepos.findAllByClCode1IsAndClCode2IsOrderByClNoAscSeqAsc(clCode1_0, clCode2_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClOtherRightsFac> findClNo(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClOtherRightsFac> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findClNo " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = clOtherRightsFacReposDay.findAllByClCode1IsAndClCode2IsAndClNoIsOrderBySeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clOtherRightsFacReposMon.findAllByClCode1IsAndClCode2IsAndClNoIsOrderBySeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clOtherRightsFacReposHist.findAllByClCode1IsAndClCode2IsAndClNoIsOrderBySeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);
    else 
      slice = clOtherRightsFacRepos.findAllByClCode1IsAndClCode2IsAndClNoIsOrderBySeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClOtherRightsFac> findClCodeRange(int clCode1_0, int clCode1_1, int clCode2_2, int clCode2_3, int clNo_4, int clNo_5, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClOtherRightsFac> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findClCodeRange " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode1_1 : " +  clCode1_1 + " clCode2_2 : " +  clCode2_2 + " clCode2_3 : " +  clCode2_3 + " clNo_4 : " +  clNo_4 + " clNo_5 : " +  clNo_5);
    if (dbName.equals(ContentName.onDay))
      slice = clOtherRightsFacReposDay.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscSeqAsc(clCode1_0, clCode1_1, clCode2_2, clCode2_3, clNo_4, clNo_5, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clOtherRightsFacReposMon.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscSeqAsc(clCode1_0, clCode1_1, clCode2_2, clCode2_3, clNo_4, clNo_5, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clOtherRightsFacReposHist.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscSeqAsc(clCode1_0, clCode1_1, clCode2_2, clCode2_3, clNo_4, clNo_5, pageable);
    else 
      slice = clOtherRightsFacRepos.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscSeqAsc(clCode1_0, clCode1_1, clCode2_2, clCode2_3, clNo_4, clNo_5, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClOtherRightsFac> findClNoSeq(int clCode1_0, int clCode2_1, int clNo_2, String seq_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClOtherRightsFac> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findClNoSeq " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2 + " seq_3 : " +  seq_3);
    if (dbName.equals(ContentName.onDay))
      slice = clOtherRightsFacReposDay.findAllByClCode1IsAndClCode2IsAndClNoIsAndSeqIsOrderByApproveNoAsc(clCode1_0, clCode2_1, clNo_2, seq_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clOtherRightsFacReposMon.findAllByClCode1IsAndClCode2IsAndClNoIsAndSeqIsOrderByApproveNoAsc(clCode1_0, clCode2_1, clNo_2, seq_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clOtherRightsFacReposHist.findAllByClCode1IsAndClCode2IsAndClNoIsAndSeqIsOrderByApproveNoAsc(clCode1_0, clCode2_1, clNo_2, seq_3, pageable);
    else 
      slice = clOtherRightsFacRepos.findAllByClCode1IsAndClCode2IsAndClNoIsAndSeqIsOrderByApproveNoAsc(clCode1_0, clCode2_1, clNo_2, seq_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ClOtherRightsFac holdById(ClOtherRightsFacId clOtherRightsFacId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clOtherRightsFacId);
    Optional<ClOtherRightsFac> clOtherRightsFac = null;
    if (dbName.equals(ContentName.onDay))
      clOtherRightsFac = clOtherRightsFacReposDay.findByClOtherRightsFacId(clOtherRightsFacId);
    else if (dbName.equals(ContentName.onMon))
      clOtherRightsFac = clOtherRightsFacReposMon.findByClOtherRightsFacId(clOtherRightsFacId);
    else if (dbName.equals(ContentName.onHist))
      clOtherRightsFac = clOtherRightsFacReposHist.findByClOtherRightsFacId(clOtherRightsFacId);
    else 
      clOtherRightsFac = clOtherRightsFacRepos.findByClOtherRightsFacId(clOtherRightsFacId);
    return clOtherRightsFac.isPresent() ? clOtherRightsFac.get() : null;
  }

  @Override
  public ClOtherRightsFac holdById(ClOtherRightsFac clOtherRightsFac, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clOtherRightsFac.getClOtherRightsFacId());
    Optional<ClOtherRightsFac> clOtherRightsFacT = null;
    if (dbName.equals(ContentName.onDay))
      clOtherRightsFacT = clOtherRightsFacReposDay.findByClOtherRightsFacId(clOtherRightsFac.getClOtherRightsFacId());
    else if (dbName.equals(ContentName.onMon))
      clOtherRightsFacT = clOtherRightsFacReposMon.findByClOtherRightsFacId(clOtherRightsFac.getClOtherRightsFacId());
    else if (dbName.equals(ContentName.onHist))
      clOtherRightsFacT = clOtherRightsFacReposHist.findByClOtherRightsFacId(clOtherRightsFac.getClOtherRightsFacId());
    else 
      clOtherRightsFacT = clOtherRightsFacRepos.findByClOtherRightsFacId(clOtherRightsFac.getClOtherRightsFacId());
    return clOtherRightsFacT.isPresent() ? clOtherRightsFacT.get() : null;
  }

  @Override
  public ClOtherRightsFac insert(ClOtherRightsFac clOtherRightsFac, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + clOtherRightsFac.getClOtherRightsFacId());
    if (this.findById(clOtherRightsFac.getClOtherRightsFacId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      clOtherRightsFac.setCreateEmpNo(empNot);

    if(clOtherRightsFac.getLastUpdateEmpNo() == null || clOtherRightsFac.getLastUpdateEmpNo().isEmpty())
      clOtherRightsFac.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clOtherRightsFacReposDay.saveAndFlush(clOtherRightsFac);	
    else if (dbName.equals(ContentName.onMon))
      return clOtherRightsFacReposMon.saveAndFlush(clOtherRightsFac);
    else if (dbName.equals(ContentName.onHist))
      return clOtherRightsFacReposHist.saveAndFlush(clOtherRightsFac);
    else 
    return clOtherRightsFacRepos.saveAndFlush(clOtherRightsFac);
  }

  @Override
  public ClOtherRightsFac update(ClOtherRightsFac clOtherRightsFac, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + clOtherRightsFac.getClOtherRightsFacId());
    if (!empNot.isEmpty())
      clOtherRightsFac.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clOtherRightsFacReposDay.saveAndFlush(clOtherRightsFac);	
    else if (dbName.equals(ContentName.onMon))
      return clOtherRightsFacReposMon.saveAndFlush(clOtherRightsFac);
    else if (dbName.equals(ContentName.onHist))
      return clOtherRightsFacReposHist.saveAndFlush(clOtherRightsFac);
    else 
    return clOtherRightsFacRepos.saveAndFlush(clOtherRightsFac);
  }

  @Override
  public ClOtherRightsFac update2(ClOtherRightsFac clOtherRightsFac, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + clOtherRightsFac.getClOtherRightsFacId());
    if (!empNot.isEmpty())
      clOtherRightsFac.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      clOtherRightsFacReposDay.saveAndFlush(clOtherRightsFac);	
    else if (dbName.equals(ContentName.onMon))
      clOtherRightsFacReposMon.saveAndFlush(clOtherRightsFac);
    else if (dbName.equals(ContentName.onHist))
        clOtherRightsFacReposHist.saveAndFlush(clOtherRightsFac);
    else 
      clOtherRightsFacRepos.saveAndFlush(clOtherRightsFac);	
    return this.findById(clOtherRightsFac.getClOtherRightsFacId());
  }

  @Override
  public void delete(ClOtherRightsFac clOtherRightsFac, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + clOtherRightsFac.getClOtherRightsFacId());
    if (dbName.equals(ContentName.onDay)) {
      clOtherRightsFacReposDay.delete(clOtherRightsFac);	
      clOtherRightsFacReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clOtherRightsFacReposMon.delete(clOtherRightsFac);	
      clOtherRightsFacReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clOtherRightsFacReposHist.delete(clOtherRightsFac);
      clOtherRightsFacReposHist.flush();
    }
    else {
      clOtherRightsFacRepos.delete(clOtherRightsFac);
      clOtherRightsFacRepos.flush();
    }
   }

  @Override
  public void insertAll(List<ClOtherRightsFac> clOtherRightsFac, TitaVo... titaVo) throws DBException {
    if (clOtherRightsFac == null || clOtherRightsFac.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (ClOtherRightsFac t : clOtherRightsFac){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      clOtherRightsFac = clOtherRightsFacReposDay.saveAll(clOtherRightsFac);	
      clOtherRightsFacReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clOtherRightsFac = clOtherRightsFacReposMon.saveAll(clOtherRightsFac);	
      clOtherRightsFacReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clOtherRightsFac = clOtherRightsFacReposHist.saveAll(clOtherRightsFac);
      clOtherRightsFacReposHist.flush();
    }
    else {
      clOtherRightsFac = clOtherRightsFacRepos.saveAll(clOtherRightsFac);
      clOtherRightsFacRepos.flush();
    }
    }

  @Override
  public void updateAll(List<ClOtherRightsFac> clOtherRightsFac, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (clOtherRightsFac == null || clOtherRightsFac.size() == 0)
      throw new DBException(6);

    for (ClOtherRightsFac t : clOtherRightsFac) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      clOtherRightsFac = clOtherRightsFacReposDay.saveAll(clOtherRightsFac);	
      clOtherRightsFacReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clOtherRightsFac = clOtherRightsFacReposMon.saveAll(clOtherRightsFac);	
      clOtherRightsFacReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clOtherRightsFac = clOtherRightsFacReposHist.saveAll(clOtherRightsFac);
      clOtherRightsFacReposHist.flush();
    }
    else {
      clOtherRightsFac = clOtherRightsFacRepos.saveAll(clOtherRightsFac);
      clOtherRightsFacRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<ClOtherRightsFac> clOtherRightsFac, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (clOtherRightsFac == null || clOtherRightsFac.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      clOtherRightsFacReposDay.deleteAll(clOtherRightsFac);	
      clOtherRightsFacReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clOtherRightsFacReposMon.deleteAll(clOtherRightsFac);	
      clOtherRightsFacReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clOtherRightsFacReposHist.deleteAll(clOtherRightsFac);
      clOtherRightsFacReposHist.flush();
    }
    else {
      clOtherRightsFacRepos.deleteAll(clOtherRightsFac);
      clOtherRightsFacRepos.flush();
    }
  }

}
