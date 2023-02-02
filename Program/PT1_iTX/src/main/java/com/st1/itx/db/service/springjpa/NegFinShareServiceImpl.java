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
import com.st1.itx.db.domain.NegFinShare;
import com.st1.itx.db.domain.NegFinShareId;
import com.st1.itx.db.repository.online.NegFinShareRepository;
import com.st1.itx.db.repository.day.NegFinShareRepositoryDay;
import com.st1.itx.db.repository.mon.NegFinShareRepositoryMon;
import com.st1.itx.db.repository.hist.NegFinShareRepositoryHist;
import com.st1.itx.db.service.NegFinShareService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("negFinShareService")
@Repository
public class NegFinShareServiceImpl extends ASpringJpaParm implements NegFinShareService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private NegFinShareRepository negFinShareRepos;

  @Autowired
  private NegFinShareRepositoryDay negFinShareReposDay;

  @Autowired
  private NegFinShareRepositoryMon negFinShareReposMon;

  @Autowired
  private NegFinShareRepositoryHist negFinShareReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(negFinShareRepos);
    org.junit.Assert.assertNotNull(negFinShareReposDay);
    org.junit.Assert.assertNotNull(negFinShareReposMon);
    org.junit.Assert.assertNotNull(negFinShareReposHist);
  }

  @Override
  public NegFinShare findById(NegFinShareId negFinShareId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + negFinShareId);
    Optional<NegFinShare> negFinShare = null;
    if (dbName.equals(ContentName.onDay))
      negFinShare = negFinShareReposDay.findById(negFinShareId);
    else if (dbName.equals(ContentName.onMon))
      negFinShare = negFinShareReposMon.findById(negFinShareId);
    else if (dbName.equals(ContentName.onHist))
      negFinShare = negFinShareReposHist.findById(negFinShareId);
    else 
      negFinShare = negFinShareRepos.findById(negFinShareId);
    NegFinShare obj = negFinShare.isPresent() ? negFinShare.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<NegFinShare> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegFinShare> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "CaseSeq", "FinCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "CaseSeq", "FinCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = negFinShareReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negFinShareReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negFinShareReposHist.findAll(pageable);
    else 
      slice = negFinShareRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegFinShare> findFinCodeAll(int custNo_0, int caseSeq_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegFinShare> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findFinCodeAll " + dbName + " : " + "custNo_0 : " + custNo_0 + " caseSeq_1 : " +  caseSeq_1);
    if (dbName.equals(ContentName.onDay))
      slice = negFinShareReposDay.findAllByCustNoIsAndCaseSeqIsOrderByCustNoDescCaseSeqDesc(custNo_0, caseSeq_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negFinShareReposMon.findAllByCustNoIsAndCaseSeqIsOrderByCustNoDescCaseSeqDesc(custNo_0, caseSeq_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negFinShareReposHist.findAllByCustNoIsAndCaseSeqIsOrderByCustNoDescCaseSeqDesc(custNo_0, caseSeq_1, pageable);
    else 
      slice = negFinShareRepos.findAllByCustNoIsAndCaseSeqIsOrderByCustNoDescCaseSeqDesc(custNo_0, caseSeq_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public NegFinShare holdById(NegFinShareId negFinShareId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + negFinShareId);
    Optional<NegFinShare> negFinShare = null;
    if (dbName.equals(ContentName.onDay))
      negFinShare = negFinShareReposDay.findByNegFinShareId(negFinShareId);
    else if (dbName.equals(ContentName.onMon))
      negFinShare = negFinShareReposMon.findByNegFinShareId(negFinShareId);
    else if (dbName.equals(ContentName.onHist))
      negFinShare = negFinShareReposHist.findByNegFinShareId(negFinShareId);
    else 
      negFinShare = negFinShareRepos.findByNegFinShareId(negFinShareId);
    return negFinShare.isPresent() ? negFinShare.get() : null;
  }

  @Override
  public NegFinShare holdById(NegFinShare negFinShare, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + negFinShare.getNegFinShareId());
    Optional<NegFinShare> negFinShareT = null;
    if (dbName.equals(ContentName.onDay))
      negFinShareT = negFinShareReposDay.findByNegFinShareId(negFinShare.getNegFinShareId());
    else if (dbName.equals(ContentName.onMon))
      negFinShareT = negFinShareReposMon.findByNegFinShareId(negFinShare.getNegFinShareId());
    else if (dbName.equals(ContentName.onHist))
      negFinShareT = negFinShareReposHist.findByNegFinShareId(negFinShare.getNegFinShareId());
    else 
      negFinShareT = negFinShareRepos.findByNegFinShareId(negFinShare.getNegFinShareId());
    return negFinShareT.isPresent() ? negFinShareT.get() : null;
  }

  @Override
  public NegFinShare insert(NegFinShare negFinShare, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + negFinShare.getNegFinShareId());
    if (this.findById(negFinShare.getNegFinShareId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      negFinShare.setCreateEmpNo(empNot);

    if(negFinShare.getLastUpdateEmpNo() == null || negFinShare.getLastUpdateEmpNo().isEmpty())
      negFinShare.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return negFinShareReposDay.saveAndFlush(negFinShare);	
    else if (dbName.equals(ContentName.onMon))
      return negFinShareReposMon.saveAndFlush(negFinShare);
    else if (dbName.equals(ContentName.onHist))
      return negFinShareReposHist.saveAndFlush(negFinShare);
    else 
    return negFinShareRepos.saveAndFlush(negFinShare);
  }

  @Override
  public NegFinShare update(NegFinShare negFinShare, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + negFinShare.getNegFinShareId());
    if (!empNot.isEmpty())
      negFinShare.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return negFinShareReposDay.saveAndFlush(negFinShare);	
    else if (dbName.equals(ContentName.onMon))
      return negFinShareReposMon.saveAndFlush(negFinShare);
    else if (dbName.equals(ContentName.onHist))
      return negFinShareReposHist.saveAndFlush(negFinShare);
    else 
    return negFinShareRepos.saveAndFlush(negFinShare);
  }

  @Override
  public NegFinShare update2(NegFinShare negFinShare, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + negFinShare.getNegFinShareId());
    if (!empNot.isEmpty())
      negFinShare.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      negFinShareReposDay.saveAndFlush(negFinShare);	
    else if (dbName.equals(ContentName.onMon))
      negFinShareReposMon.saveAndFlush(negFinShare);
    else if (dbName.equals(ContentName.onHist))
        negFinShareReposHist.saveAndFlush(negFinShare);
    else 
      negFinShareRepos.saveAndFlush(negFinShare);	
    return this.findById(negFinShare.getNegFinShareId());
  }

  @Override
  public void delete(NegFinShare negFinShare, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + negFinShare.getNegFinShareId());
    if (dbName.equals(ContentName.onDay)) {
      negFinShareReposDay.delete(negFinShare);	
      negFinShareReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negFinShareReposMon.delete(negFinShare);	
      negFinShareReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negFinShareReposHist.delete(negFinShare);
      negFinShareReposHist.flush();
    }
    else {
      negFinShareRepos.delete(negFinShare);
      negFinShareRepos.flush();
    }
   }

  @Override
  public void insertAll(List<NegFinShare> negFinShare, TitaVo... titaVo) throws DBException {
    if (negFinShare == null || negFinShare.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (NegFinShare t : negFinShare){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      negFinShare = negFinShareReposDay.saveAll(negFinShare);	
      negFinShareReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negFinShare = negFinShareReposMon.saveAll(negFinShare);	
      negFinShareReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negFinShare = negFinShareReposHist.saveAll(negFinShare);
      negFinShareReposHist.flush();
    }
    else {
      negFinShare = negFinShareRepos.saveAll(negFinShare);
      negFinShareRepos.flush();
    }
    }

  @Override
  public void updateAll(List<NegFinShare> negFinShare, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (negFinShare == null || negFinShare.size() == 0)
      throw new DBException(6);

    for (NegFinShare t : negFinShare) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      negFinShare = negFinShareReposDay.saveAll(negFinShare);	
      negFinShareReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negFinShare = negFinShareReposMon.saveAll(negFinShare);	
      negFinShareReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negFinShare = negFinShareReposHist.saveAll(negFinShare);
      negFinShareReposHist.flush();
    }
    else {
      negFinShare = negFinShareRepos.saveAll(negFinShare);
      negFinShareRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<NegFinShare> negFinShare, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (negFinShare == null || negFinShare.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      negFinShareReposDay.deleteAll(negFinShare);	
      negFinShareReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negFinShareReposMon.deleteAll(negFinShare);	
      negFinShareReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negFinShareReposHist.deleteAll(negFinShare);
      negFinShareReposHist.flush();
    }
    else {
      negFinShareRepos.deleteAll(negFinShare);
      negFinShareRepos.flush();
    }
  }

}
