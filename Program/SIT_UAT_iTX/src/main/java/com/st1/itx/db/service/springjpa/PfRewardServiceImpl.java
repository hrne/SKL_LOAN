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
import com.st1.itx.db.domain.PfReward;
import com.st1.itx.db.repository.online.PfRewardRepository;
import com.st1.itx.db.repository.day.PfRewardRepositoryDay;
import com.st1.itx.db.repository.mon.PfRewardRepositoryMon;
import com.st1.itx.db.repository.hist.PfRewardRepositoryHist;
import com.st1.itx.db.service.PfRewardService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("pfRewardService")
@Repository
public class PfRewardServiceImpl extends ASpringJpaParm implements PfRewardService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private PfRewardRepository pfRewardRepos;

  @Autowired
  private PfRewardRepositoryDay pfRewardReposDay;

  @Autowired
  private PfRewardRepositoryMon pfRewardReposMon;

  @Autowired
  private PfRewardRepositoryHist pfRewardReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(pfRewardRepos);
    org.junit.Assert.assertNotNull(pfRewardReposDay);
    org.junit.Assert.assertNotNull(pfRewardReposMon);
    org.junit.Assert.assertNotNull(pfRewardReposHist);
  }

  @Override
  public PfReward findById(Long logNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + logNo);
    Optional<PfReward> pfReward = null;
    if (dbName.equals(ContentName.onDay))
      pfReward = pfRewardReposDay.findById(logNo);
    else if (dbName.equals(ContentName.onMon))
      pfReward = pfRewardReposMon.findById(logNo);
    else if (dbName.equals(ContentName.onHist))
      pfReward = pfRewardReposHist.findById(logNo);
    else 
      pfReward = pfRewardRepos.findById(logNo);
    PfReward obj = pfReward.isPresent() ? pfReward.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<PfReward> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfReward> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = pfRewardReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfRewardReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfRewardReposHist.findAll(pageable);
    else 
      slice = pfRewardRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfReward> findFacmNoRange(int custNo_0, int facmNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfReward> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findFacmNoRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " facmNo_2 : " +  facmNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = pfRewardReposDay.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfRewardReposMon.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfRewardReposHist.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);
    else 
      slice = pfRewardRepos.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfReward> findByCustNo(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfReward> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByCustNo " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = pfRewardReposDay.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfRewardReposMon.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfRewardReposHist.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);
    else 
      slice = pfRewardRepos.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfReward> findByCustNoAndFacmNo(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfReward> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByCustNoAndFacmNo " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = pfRewardReposDay.findAllByCustNoIsAndFacmNoIs(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfRewardReposMon.findAllByCustNoIsAndFacmNoIs(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfRewardReposHist.findAllByCustNoIsAndFacmNoIs(custNo_0, facmNo_1, pageable);
    else 
      slice = pfRewardRepos.findAllByCustNoIsAndFacmNoIs(custNo_0, facmNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfReward> findByIntroducer(String introducer_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfReward> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByIntroducer " + dbName + " : " + "introducer_0 : " + introducer_0);
    if (dbName.equals(ContentName.onDay))
      slice = pfRewardReposDay.findAllByIntroducerIsOrderByCustNoAscFacmNoAscBormNoAsc(introducer_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfRewardReposMon.findAllByIntroducerIsOrderByCustNoAscFacmNoAscBormNoAsc(introducer_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfRewardReposHist.findAllByIntroducerIsOrderByCustNoAscFacmNoAscBormNoAsc(introducer_0, pageable);
    else 
      slice = pfRewardRepos.findAllByIntroducerIsOrderByCustNoAscFacmNoAscBormNoAsc(introducer_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfReward> findByWorkMonth(int workMonth_0, int workMonth_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfReward> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByWorkMonth " + dbName + " : " + "workMonth_0 : " + workMonth_0 + " workMonth_1 : " +  workMonth_1);
    if (dbName.equals(ContentName.onDay))
      slice = pfRewardReposDay.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, workMonth_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfRewardReposMon.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, workMonth_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfRewardReposHist.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, workMonth_1, pageable);
    else 
      slice = pfRewardRepos.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, workMonth_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfReward> findByItdWm(String introducer_0, int workMonth_1, int workMonth_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfReward> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByItdWm " + dbName + " : " + "introducer_0 : " + introducer_0 + " workMonth_1 : " +  workMonth_1 + " workMonth_2 : " +  workMonth_2);
    if (dbName.equals(ContentName.onDay))
      slice = pfRewardReposDay.findAllByIntroducerIsAndWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(introducer_0, workMonth_1, workMonth_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfRewardReposMon.findAllByIntroducerIsAndWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(introducer_0, workMonth_1, workMonth_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfRewardReposHist.findAllByIntroducerIsAndWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(introducer_0, workMonth_1, workMonth_2, pageable);
    else 
      slice = pfRewardRepos.findAllByIntroducerIsAndWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(introducer_0, workMonth_1, workMonth_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfReward> findByPerfDate(int perfDate_0, int perfDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfReward> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByPerfDate " + dbName + " : " + "perfDate_0 : " + perfDate_0 + " perfDate_1 : " +  perfDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = pfRewardReposDay.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(perfDate_0, perfDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfRewardReposMon.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(perfDate_0, perfDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfRewardReposHist.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(perfDate_0, perfDate_1, pageable);
    else 
      slice = pfRewardRepos.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(perfDate_0, perfDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PfReward findByTxFirst(int custNo_0, int facmNo_1, int bormNo_2, int perfDate_3, int repayType_4, String pieceCode_5, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findByTxFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2 + " perfDate_3 : " +  perfDate_3 + " repayType_4 : " +  repayType_4 + " pieceCode_5 : " +  pieceCode_5);
    Optional<PfReward> pfRewardT = null;
    if (dbName.equals(ContentName.onDay))
      pfRewardT = pfRewardReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPerfDateIsAndRepayTypeIsAndPieceCodeIs(custNo_0, facmNo_1, bormNo_2, perfDate_3, repayType_4, pieceCode_5);
    else if (dbName.equals(ContentName.onMon))
      pfRewardT = pfRewardReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPerfDateIsAndRepayTypeIsAndPieceCodeIs(custNo_0, facmNo_1, bormNo_2, perfDate_3, repayType_4, pieceCode_5);
    else if (dbName.equals(ContentName.onHist))
      pfRewardT = pfRewardReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPerfDateIsAndRepayTypeIsAndPieceCodeIs(custNo_0, facmNo_1, bormNo_2, perfDate_3, repayType_4, pieceCode_5);
    else 
      pfRewardT = pfRewardRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPerfDateIsAndRepayTypeIsAndPieceCodeIs(custNo_0, facmNo_1, bormNo_2, perfDate_3, repayType_4, pieceCode_5);

    return pfRewardT.isPresent() ? pfRewardT.get() : null;
  }

  @Override
  public Slice<PfReward> findBormNoEq(int custNo_0, int facmNo_1, int bormNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfReward> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findBormNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = pfRewardReposDay.findAllByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, bormNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfRewardReposMon.findAllByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, bormNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfRewardReposHist.findAllByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, bormNo_2, pageable);
    else 
      slice = pfRewardRepos.findAllByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, bormNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PfReward holdById(Long logNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + logNo);
    Optional<PfReward> pfReward = null;
    if (dbName.equals(ContentName.onDay))
      pfReward = pfRewardReposDay.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onMon))
      pfReward = pfRewardReposMon.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onHist))
      pfReward = pfRewardReposHist.findByLogNo(logNo);
    else 
      pfReward = pfRewardRepos.findByLogNo(logNo);
    return pfReward.isPresent() ? pfReward.get() : null;
  }

  @Override
  public PfReward holdById(PfReward pfReward, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + pfReward.getLogNo());
    Optional<PfReward> pfRewardT = null;
    if (dbName.equals(ContentName.onDay))
      pfRewardT = pfRewardReposDay.findByLogNo(pfReward.getLogNo());
    else if (dbName.equals(ContentName.onMon))
      pfRewardT = pfRewardReposMon.findByLogNo(pfReward.getLogNo());
    else if (dbName.equals(ContentName.onHist))
      pfRewardT = pfRewardReposHist.findByLogNo(pfReward.getLogNo());
    else 
      pfRewardT = pfRewardRepos.findByLogNo(pfReward.getLogNo());
    return pfRewardT.isPresent() ? pfRewardT.get() : null;
  }

  @Override
  public PfReward insert(PfReward pfReward, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + pfReward.getLogNo());
    if (this.findById(pfReward.getLogNo()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      pfReward.setCreateEmpNo(empNot);

    if(pfReward.getLastUpdateEmpNo() == null || pfReward.getLastUpdateEmpNo().isEmpty())
      pfReward.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfRewardReposDay.saveAndFlush(pfReward);	
    else if (dbName.equals(ContentName.onMon))
      return pfRewardReposMon.saveAndFlush(pfReward);
    else if (dbName.equals(ContentName.onHist))
      return pfRewardReposHist.saveAndFlush(pfReward);
    else 
    return pfRewardRepos.saveAndFlush(pfReward);
  }

  @Override
  public PfReward update(PfReward pfReward, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + pfReward.getLogNo());
    if (!empNot.isEmpty())
      pfReward.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfRewardReposDay.saveAndFlush(pfReward);	
    else if (dbName.equals(ContentName.onMon))
      return pfRewardReposMon.saveAndFlush(pfReward);
    else if (dbName.equals(ContentName.onHist))
      return pfRewardReposHist.saveAndFlush(pfReward);
    else 
    return pfRewardRepos.saveAndFlush(pfReward);
  }

  @Override
  public PfReward update2(PfReward pfReward, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + pfReward.getLogNo());
    if (!empNot.isEmpty())
      pfReward.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      pfRewardReposDay.saveAndFlush(pfReward);	
    else if (dbName.equals(ContentName.onMon))
      pfRewardReposMon.saveAndFlush(pfReward);
    else if (dbName.equals(ContentName.onHist))
        pfRewardReposHist.saveAndFlush(pfReward);
    else 
      pfRewardRepos.saveAndFlush(pfReward);	
    return this.findById(pfReward.getLogNo());
  }

  @Override
  public void delete(PfReward pfReward, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + pfReward.getLogNo());
    if (dbName.equals(ContentName.onDay)) {
      pfRewardReposDay.delete(pfReward);	
      pfRewardReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfRewardReposMon.delete(pfReward);	
      pfRewardReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfRewardReposHist.delete(pfReward);
      pfRewardReposHist.flush();
    }
    else {
      pfRewardRepos.delete(pfReward);
      pfRewardRepos.flush();
    }
   }

  @Override
  public void insertAll(List<PfReward> pfReward, TitaVo... titaVo) throws DBException {
    if (pfReward == null || pfReward.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (PfReward t : pfReward){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      pfReward = pfRewardReposDay.saveAll(pfReward);	
      pfRewardReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfReward = pfRewardReposMon.saveAll(pfReward);	
      pfRewardReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfReward = pfRewardReposHist.saveAll(pfReward);
      pfRewardReposHist.flush();
    }
    else {
      pfReward = pfRewardRepos.saveAll(pfReward);
      pfRewardRepos.flush();
    }
    }

  @Override
  public void updateAll(List<PfReward> pfReward, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (pfReward == null || pfReward.size() == 0)
      throw new DBException(6);

    for (PfReward t : pfReward) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      pfReward = pfRewardReposDay.saveAll(pfReward);	
      pfRewardReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfReward = pfRewardReposMon.saveAll(pfReward);	
      pfRewardReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfReward = pfRewardReposHist.saveAll(pfReward);
      pfRewardReposHist.flush();
    }
    else {
      pfReward = pfRewardRepos.saveAll(pfReward);
      pfRewardRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<PfReward> pfReward, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (pfReward == null || pfReward.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      pfRewardReposDay.deleteAll(pfReward);	
      pfRewardReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfRewardReposMon.deleteAll(pfReward);	
      pfRewardReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfRewardReposHist.deleteAll(pfReward);
      pfRewardReposHist.flush();
    }
    else {
      pfRewardRepos.deleteAll(pfReward);
      pfRewardRepos.flush();
    }
  }

}
