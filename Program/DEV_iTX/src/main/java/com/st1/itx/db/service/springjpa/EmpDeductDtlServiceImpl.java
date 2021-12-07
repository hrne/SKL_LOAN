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
import com.st1.itx.db.domain.EmpDeductDtl;
import com.st1.itx.db.domain.EmpDeductDtlId;
import com.st1.itx.db.repository.online.EmpDeductDtlRepository;
import com.st1.itx.db.repository.day.EmpDeductDtlRepositoryDay;
import com.st1.itx.db.repository.mon.EmpDeductDtlRepositoryMon;
import com.st1.itx.db.repository.hist.EmpDeductDtlRepositoryHist;
import com.st1.itx.db.service.EmpDeductDtlService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("empDeductDtlService")
@Repository
public class EmpDeductDtlServiceImpl extends ASpringJpaParm implements EmpDeductDtlService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private EmpDeductDtlRepository empDeductDtlRepos;

  @Autowired
  private EmpDeductDtlRepositoryDay empDeductDtlReposDay;

  @Autowired
  private EmpDeductDtlRepositoryMon empDeductDtlReposMon;

  @Autowired
  private EmpDeductDtlRepositoryHist empDeductDtlReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(empDeductDtlRepos);
    org.junit.Assert.assertNotNull(empDeductDtlReposDay);
    org.junit.Assert.assertNotNull(empDeductDtlReposMon);
    org.junit.Assert.assertNotNull(empDeductDtlReposHist);
  }

  @Override
  public EmpDeductDtl findById(EmpDeductDtlId empDeductDtlId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + empDeductDtlId);
    Optional<EmpDeductDtl> empDeductDtl = null;
    if (dbName.equals(ContentName.onDay))
      empDeductDtl = empDeductDtlReposDay.findById(empDeductDtlId);
    else if (dbName.equals(ContentName.onMon))
      empDeductDtl = empDeductDtlReposMon.findById(empDeductDtlId);
    else if (dbName.equals(ContentName.onHist))
      empDeductDtl = empDeductDtlReposHist.findById(empDeductDtlId);
    else 
      empDeductDtl = empDeductDtlRepos.findById(empDeductDtlId);
    EmpDeductDtl obj = empDeductDtl.isPresent() ? empDeductDtl.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<EmpDeductDtl> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<EmpDeductDtl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "EntryDate", "CustNo", "AchRepayCode", "PerfMonth", "ProcCode", "RepayCode", "AcctCode", "FacmNo", "BormNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "EntryDate", "CustNo", "AchRepayCode", "PerfMonth", "ProcCode", "RepayCode", "AcctCode", "FacmNo", "BormNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = empDeductDtlReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = empDeductDtlReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = empDeductDtlReposHist.findAll(pageable);
    else 
      slice = empDeductDtlRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public EmpDeductDtl mediaSeqFirst(int mediaDate_0, String mediaKind_1, int mediaSeq_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("mediaSeqFirst " + dbName + " : " + "mediaDate_0 : " + mediaDate_0 + " mediaKind_1 : " +  mediaKind_1 + " mediaSeq_2 : " +  mediaSeq_2);
    Optional<EmpDeductDtl> empDeductDtlT = null;
    if (dbName.equals(ContentName.onDay))
      empDeductDtlT = empDeductDtlReposDay.findTopByMediaDateIsAndMediaKindIsAndMediaSeqIs(mediaDate_0, mediaKind_1, mediaSeq_2);
    else if (dbName.equals(ContentName.onMon))
      empDeductDtlT = empDeductDtlReposMon.findTopByMediaDateIsAndMediaKindIsAndMediaSeqIs(mediaDate_0, mediaKind_1, mediaSeq_2);
    else if (dbName.equals(ContentName.onHist))
      empDeductDtlT = empDeductDtlReposHist.findTopByMediaDateIsAndMediaKindIsAndMediaSeqIs(mediaDate_0, mediaKind_1, mediaSeq_2);
    else 
      empDeductDtlT = empDeductDtlRepos.findTopByMediaDateIsAndMediaKindIsAndMediaSeqIs(mediaDate_0, mediaKind_1, mediaSeq_2);

    return empDeductDtlT.isPresent() ? empDeductDtlT.get() : null;
  }

  @Override
  public Slice<EmpDeductDtl> entryDateRng(int entryDate_1, int entryDate_2, List<String> procCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<EmpDeductDtl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("entryDateRng " + dbName + " : " + "entryDate_1 : " + entryDate_1 + " entryDate_2 : " +  entryDate_2 + " procCode_3 : " +  procCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = empDeductDtlReposDay.findAllByErrMsgIsNullAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndProcCodeInOrderByEntryDateAscCustNoAscAchRepayCodeDesc(entryDate_1, entryDate_2, procCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = empDeductDtlReposMon.findAllByErrMsgIsNullAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndProcCodeInOrderByEntryDateAscCustNoAscAchRepayCodeDesc(entryDate_1, entryDate_2, procCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = empDeductDtlReposHist.findAllByErrMsgIsNullAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndProcCodeInOrderByEntryDateAscCustNoAscAchRepayCodeDesc(entryDate_1, entryDate_2, procCode_3, pageable);
    else 
      slice = empDeductDtlRepos.findAllByErrMsgIsNullAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndProcCodeInOrderByEntryDateAscCustNoAscAchRepayCodeDesc(entryDate_1, entryDate_2, procCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<EmpDeductDtl> mediaSeqEq(int mediaDate_0, String mediaKind_1, int mediaSeq_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<EmpDeductDtl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("mediaSeqEq " + dbName + " : " + "mediaDate_0 : " + mediaDate_0 + " mediaKind_1 : " +  mediaKind_1 + " mediaSeq_2 : " +  mediaSeq_2);
    if (dbName.equals(ContentName.onDay))
      slice = empDeductDtlReposDay.findAllByMediaDateIsAndMediaKindIsAndMediaSeqIs(mediaDate_0, mediaKind_1, mediaSeq_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = empDeductDtlReposMon.findAllByMediaDateIsAndMediaKindIsAndMediaSeqIs(mediaDate_0, mediaKind_1, mediaSeq_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = empDeductDtlReposHist.findAllByMediaDateIsAndMediaKindIsAndMediaSeqIs(mediaDate_0, mediaKind_1, mediaSeq_2, pageable);
    else 
      slice = empDeductDtlRepos.findAllByMediaDateIsAndMediaKindIsAndMediaSeqIs(mediaDate_0, mediaKind_1, mediaSeq_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<EmpDeductDtl> mediaDateRng(int mediaDate_0, String mediaKind_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<EmpDeductDtl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("mediaDateRng " + dbName + " : " + "mediaDate_0 : " + mediaDate_0 + " mediaKind_1 : " +  mediaKind_1);
    if (dbName.equals(ContentName.onDay))
      slice = empDeductDtlReposDay.findAllByMediaDateIsAndMediaKindIsOrderByEntryDateAscCustNoAscAchRepayCodeDesc(mediaDate_0, mediaKind_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = empDeductDtlReposMon.findAllByMediaDateIsAndMediaKindIsOrderByEntryDateAscCustNoAscAchRepayCodeDesc(mediaDate_0, mediaKind_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = empDeductDtlReposHist.findAllByMediaDateIsAndMediaKindIsOrderByEntryDateAscCustNoAscAchRepayCodeDesc(mediaDate_0, mediaKind_1, pageable);
    else 
      slice = empDeductDtlRepos.findAllByMediaDateIsAndMediaKindIsOrderByEntryDateAscCustNoAscAchRepayCodeDesc(mediaDate_0, mediaKind_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public EmpDeductDtl holdById(EmpDeductDtlId empDeductDtlId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + empDeductDtlId);
    Optional<EmpDeductDtl> empDeductDtl = null;
    if (dbName.equals(ContentName.onDay))
      empDeductDtl = empDeductDtlReposDay.findByEmpDeductDtlId(empDeductDtlId);
    else if (dbName.equals(ContentName.onMon))
      empDeductDtl = empDeductDtlReposMon.findByEmpDeductDtlId(empDeductDtlId);
    else if (dbName.equals(ContentName.onHist))
      empDeductDtl = empDeductDtlReposHist.findByEmpDeductDtlId(empDeductDtlId);
    else 
      empDeductDtl = empDeductDtlRepos.findByEmpDeductDtlId(empDeductDtlId);
    return empDeductDtl.isPresent() ? empDeductDtl.get() : null;
  }

  @Override
  public EmpDeductDtl holdById(EmpDeductDtl empDeductDtl, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + empDeductDtl.getEmpDeductDtlId());
    Optional<EmpDeductDtl> empDeductDtlT = null;
    if (dbName.equals(ContentName.onDay))
      empDeductDtlT = empDeductDtlReposDay.findByEmpDeductDtlId(empDeductDtl.getEmpDeductDtlId());
    else if (dbName.equals(ContentName.onMon))
      empDeductDtlT = empDeductDtlReposMon.findByEmpDeductDtlId(empDeductDtl.getEmpDeductDtlId());
    else if (dbName.equals(ContentName.onHist))
      empDeductDtlT = empDeductDtlReposHist.findByEmpDeductDtlId(empDeductDtl.getEmpDeductDtlId());
    else 
      empDeductDtlT = empDeductDtlRepos.findByEmpDeductDtlId(empDeductDtl.getEmpDeductDtlId());
    return empDeductDtlT.isPresent() ? empDeductDtlT.get() : null;
  }

  @Override
  public EmpDeductDtl insert(EmpDeductDtl empDeductDtl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + empDeductDtl.getEmpDeductDtlId());
    if (this.findById(empDeductDtl.getEmpDeductDtlId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      empDeductDtl.setCreateEmpNo(empNot);

    if(empDeductDtl.getLastUpdateEmpNo() == null || empDeductDtl.getLastUpdateEmpNo().isEmpty())
      empDeductDtl.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return empDeductDtlReposDay.saveAndFlush(empDeductDtl);	
    else if (dbName.equals(ContentName.onMon))
      return empDeductDtlReposMon.saveAndFlush(empDeductDtl);
    else if (dbName.equals(ContentName.onHist))
      return empDeductDtlReposHist.saveAndFlush(empDeductDtl);
    else 
    return empDeductDtlRepos.saveAndFlush(empDeductDtl);
  }

  @Override
  public EmpDeductDtl update(EmpDeductDtl empDeductDtl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + empDeductDtl.getEmpDeductDtlId());
    if (!empNot.isEmpty())
      empDeductDtl.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return empDeductDtlReposDay.saveAndFlush(empDeductDtl);	
    else if (dbName.equals(ContentName.onMon))
      return empDeductDtlReposMon.saveAndFlush(empDeductDtl);
    else if (dbName.equals(ContentName.onHist))
      return empDeductDtlReposHist.saveAndFlush(empDeductDtl);
    else 
    return empDeductDtlRepos.saveAndFlush(empDeductDtl);
  }

  @Override
  public EmpDeductDtl update2(EmpDeductDtl empDeductDtl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + empDeductDtl.getEmpDeductDtlId());
    if (!empNot.isEmpty())
      empDeductDtl.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      empDeductDtlReposDay.saveAndFlush(empDeductDtl);	
    else if (dbName.equals(ContentName.onMon))
      empDeductDtlReposMon.saveAndFlush(empDeductDtl);
    else if (dbName.equals(ContentName.onHist))
        empDeductDtlReposHist.saveAndFlush(empDeductDtl);
    else 
      empDeductDtlRepos.saveAndFlush(empDeductDtl);	
    return this.findById(empDeductDtl.getEmpDeductDtlId());
  }

  @Override
  public void delete(EmpDeductDtl empDeductDtl, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + empDeductDtl.getEmpDeductDtlId());
    if (dbName.equals(ContentName.onDay)) {
      empDeductDtlReposDay.delete(empDeductDtl);	
      empDeductDtlReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      empDeductDtlReposMon.delete(empDeductDtl);	
      empDeductDtlReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      empDeductDtlReposHist.delete(empDeductDtl);
      empDeductDtlReposHist.flush();
    }
    else {
      empDeductDtlRepos.delete(empDeductDtl);
      empDeductDtlRepos.flush();
    }
   }

  @Override
  public void insertAll(List<EmpDeductDtl> empDeductDtl, TitaVo... titaVo) throws DBException {
    if (empDeductDtl == null || empDeductDtl.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (EmpDeductDtl t : empDeductDtl){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      empDeductDtl = empDeductDtlReposDay.saveAll(empDeductDtl);	
      empDeductDtlReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      empDeductDtl = empDeductDtlReposMon.saveAll(empDeductDtl);	
      empDeductDtlReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      empDeductDtl = empDeductDtlReposHist.saveAll(empDeductDtl);
      empDeductDtlReposHist.flush();
    }
    else {
      empDeductDtl = empDeductDtlRepos.saveAll(empDeductDtl);
      empDeductDtlRepos.flush();
    }
    }

  @Override
  public void updateAll(List<EmpDeductDtl> empDeductDtl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (empDeductDtl == null || empDeductDtl.size() == 0)
      throw new DBException(6);

    for (EmpDeductDtl t : empDeductDtl) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      empDeductDtl = empDeductDtlReposDay.saveAll(empDeductDtl);	
      empDeductDtlReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      empDeductDtl = empDeductDtlReposMon.saveAll(empDeductDtl);	
      empDeductDtlReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      empDeductDtl = empDeductDtlReposHist.saveAll(empDeductDtl);
      empDeductDtlReposHist.flush();
    }
    else {
      empDeductDtl = empDeductDtlRepos.saveAll(empDeductDtl);
      empDeductDtlRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<EmpDeductDtl> empDeductDtl, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (empDeductDtl == null || empDeductDtl.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      empDeductDtlReposDay.deleteAll(empDeductDtl);	
      empDeductDtlReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      empDeductDtlReposMon.deleteAll(empDeductDtl);	
      empDeductDtlReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      empDeductDtlReposHist.deleteAll(empDeductDtl);
      empDeductDtlReposHist.flush();
    }
    else {
      empDeductDtlRepos.deleteAll(empDeductDtl);
      empDeductDtlRepos.flush();
    }
  }

}
