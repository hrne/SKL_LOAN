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
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdAcCodeId;
import com.st1.itx.db.repository.online.CdAcCodeRepository;
import com.st1.itx.db.repository.day.CdAcCodeRepositoryDay;
import com.st1.itx.db.repository.mon.CdAcCodeRepositoryMon;
import com.st1.itx.db.repository.hist.CdAcCodeRepositoryHist;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdAcCodeService")
@Repository
public class CdAcCodeServiceImpl extends ASpringJpaParm implements CdAcCodeService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdAcCodeRepository cdAcCodeRepos;

  @Autowired
  private CdAcCodeRepositoryDay cdAcCodeReposDay;

  @Autowired
  private CdAcCodeRepositoryMon cdAcCodeReposMon;

  @Autowired
  private CdAcCodeRepositoryHist cdAcCodeReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdAcCodeRepos);
    org.junit.Assert.assertNotNull(cdAcCodeReposDay);
    org.junit.Assert.assertNotNull(cdAcCodeReposMon);
    org.junit.Assert.assertNotNull(cdAcCodeReposHist);
  }

  @Override
  public CdAcCode findById(CdAcCodeId cdAcCodeId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + cdAcCodeId);
    Optional<CdAcCode> cdAcCode = null;
    if (dbName.equals(ContentName.onDay))
      cdAcCode = cdAcCodeReposDay.findById(cdAcCodeId);
    else if (dbName.equals(ContentName.onMon))
      cdAcCode = cdAcCodeReposMon.findById(cdAcCodeId);
    else if (dbName.equals(ContentName.onHist))
      cdAcCode = cdAcCodeReposHist.findById(cdAcCodeId);
    else 
      cdAcCode = cdAcCodeRepos.findById(cdAcCodeId);
    CdAcCode obj = cdAcCode.isPresent() ? cdAcCode.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdAcCode> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdAcCode> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AcNoCode", "AcSubCode", "AcDtlCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcNoCode", "AcSubCode", "AcDtlCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdAcCodeReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdAcCodeReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdAcCodeReposHist.findAll(pageable);
    else 
      slice = cdAcCodeRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdAcCode acCodeAcctFirst(String acctCode_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("acCodeAcctFirst " + dbName + " : " + "acctCode_0 : " + acctCode_0);
    Optional<CdAcCode> cdAcCodeT = null;
    if (dbName.equals(ContentName.onDay))
      cdAcCodeT = cdAcCodeReposDay.findTopByAcctCodeIs(acctCode_0);
    else if (dbName.equals(ContentName.onMon))
      cdAcCodeT = cdAcCodeReposMon.findTopByAcctCodeIs(acctCode_0);
    else if (dbName.equals(ContentName.onHist))
      cdAcCodeT = cdAcCodeReposHist.findTopByAcctCodeIs(acctCode_0);
    else 
      cdAcCodeT = cdAcCodeRepos.findTopByAcctCodeIs(acctCode_0);

    return cdAcCodeT.isPresent() ? cdAcCodeT.get() : null;
  }

  @Override
  public Slice<CdAcCode> findAcCode(String acNoCode_0, String acNoCode_1, String acSubCode_2, String acSubCode_3, String acDtlCode_4, String acDtlCode_5, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdAcCode> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findAcCode " + dbName + " : " + "acNoCode_0 : " + acNoCode_0 + " acNoCode_1 : " +  acNoCode_1 + " acSubCode_2 : " +  acSubCode_2 + " acSubCode_3 : " +  acSubCode_3 + " acDtlCode_4 : " +  acDtlCode_4 + " acDtlCode_5 : " +  acDtlCode_5);
    if (dbName.equals(ContentName.onDay))
      slice = cdAcCodeReposDay.findAllByAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndAcSubCodeGreaterThanEqualAndAcSubCodeLessThanEqualAndAcDtlCodeGreaterThanEqualAndAcDtlCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acNoCode_0, acNoCode_1, acSubCode_2, acSubCode_3, acDtlCode_4, acDtlCode_5, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdAcCodeReposMon.findAllByAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndAcSubCodeGreaterThanEqualAndAcSubCodeLessThanEqualAndAcDtlCodeGreaterThanEqualAndAcDtlCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acNoCode_0, acNoCode_1, acSubCode_2, acSubCode_3, acDtlCode_4, acDtlCode_5, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdAcCodeReposHist.findAllByAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndAcSubCodeGreaterThanEqualAndAcSubCodeLessThanEqualAndAcDtlCodeGreaterThanEqualAndAcDtlCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acNoCode_0, acNoCode_1, acSubCode_2, acSubCode_3, acDtlCode_4, acDtlCode_5, pageable);
    else 
      slice = cdAcCodeRepos.findAllByAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndAcSubCodeGreaterThanEqualAndAcSubCodeLessThanEqualAndAcDtlCodeGreaterThanEqualAndAcDtlCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acNoCode_0, acNoCode_1, acSubCode_2, acSubCode_3, acDtlCode_4, acDtlCode_5, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdAcCode> findAcCodeOld(String acNoCodeOld_0, String acNoCodeOld_1, String acSubCode_2, String acSubCode_3, String acDtlCode_4, String acDtlCode_5, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdAcCode> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findAcCodeOld " + dbName + " : " + "acNoCodeOld_0 : " + acNoCodeOld_0 + " acNoCodeOld_1 : " +  acNoCodeOld_1 + " acSubCode_2 : " +  acSubCode_2 + " acSubCode_3 : " +  acSubCode_3 + " acDtlCode_4 : " +  acDtlCode_4 + " acDtlCode_5 : " +  acDtlCode_5);
    if (dbName.equals(ContentName.onDay))
      slice = cdAcCodeReposDay.findAllByAcNoCodeOldGreaterThanEqualAndAcNoCodeOldLessThanEqualAndAcSubCodeGreaterThanEqualAndAcSubCodeLessThanEqualAndAcDtlCodeGreaterThanEqualAndAcDtlCodeLessThanEqualOrderByAcNoCodeOldAscAcSubCodeAscAcDtlCodeAsc(acNoCodeOld_0, acNoCodeOld_1, acSubCode_2, acSubCode_3, acDtlCode_4, acDtlCode_5, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdAcCodeReposMon.findAllByAcNoCodeOldGreaterThanEqualAndAcNoCodeOldLessThanEqualAndAcSubCodeGreaterThanEqualAndAcSubCodeLessThanEqualAndAcDtlCodeGreaterThanEqualAndAcDtlCodeLessThanEqualOrderByAcNoCodeOldAscAcSubCodeAscAcDtlCodeAsc(acNoCodeOld_0, acNoCodeOld_1, acSubCode_2, acSubCode_3, acDtlCode_4, acDtlCode_5, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdAcCodeReposHist.findAllByAcNoCodeOldGreaterThanEqualAndAcNoCodeOldLessThanEqualAndAcSubCodeGreaterThanEqualAndAcSubCodeLessThanEqualAndAcDtlCodeGreaterThanEqualAndAcDtlCodeLessThanEqualOrderByAcNoCodeOldAscAcSubCodeAscAcDtlCodeAsc(acNoCodeOld_0, acNoCodeOld_1, acSubCode_2, acSubCode_3, acDtlCode_4, acDtlCode_5, pageable);
    else 
      slice = cdAcCodeRepos.findAllByAcNoCodeOldGreaterThanEqualAndAcNoCodeOldLessThanEqualAndAcSubCodeGreaterThanEqualAndAcSubCodeLessThanEqualAndAcDtlCodeGreaterThanEqualAndAcDtlCodeLessThanEqualOrderByAcNoCodeOldAscAcSubCodeAscAcDtlCodeAsc(acNoCodeOld_0, acNoCodeOld_1, acSubCode_2, acSubCode_3, acDtlCode_4, acDtlCode_5, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdAcCode holdById(CdAcCodeId cdAcCodeId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdAcCodeId);
    Optional<CdAcCode> cdAcCode = null;
    if (dbName.equals(ContentName.onDay))
      cdAcCode = cdAcCodeReposDay.findByCdAcCodeId(cdAcCodeId);
    else if (dbName.equals(ContentName.onMon))
      cdAcCode = cdAcCodeReposMon.findByCdAcCodeId(cdAcCodeId);
    else if (dbName.equals(ContentName.onHist))
      cdAcCode = cdAcCodeReposHist.findByCdAcCodeId(cdAcCodeId);
    else 
      cdAcCode = cdAcCodeRepos.findByCdAcCodeId(cdAcCodeId);
    return cdAcCode.isPresent() ? cdAcCode.get() : null;
  }

  @Override
  public CdAcCode holdById(CdAcCode cdAcCode, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdAcCode.getCdAcCodeId());
    Optional<CdAcCode> cdAcCodeT = null;
    if (dbName.equals(ContentName.onDay))
      cdAcCodeT = cdAcCodeReposDay.findByCdAcCodeId(cdAcCode.getCdAcCodeId());
    else if (dbName.equals(ContentName.onMon))
      cdAcCodeT = cdAcCodeReposMon.findByCdAcCodeId(cdAcCode.getCdAcCodeId());
    else if (dbName.equals(ContentName.onHist))
      cdAcCodeT = cdAcCodeReposHist.findByCdAcCodeId(cdAcCode.getCdAcCodeId());
    else 
      cdAcCodeT = cdAcCodeRepos.findByCdAcCodeId(cdAcCode.getCdAcCodeId());
    return cdAcCodeT.isPresent() ? cdAcCodeT.get() : null;
  }

  @Override
  public CdAcCode insert(CdAcCode cdAcCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + cdAcCode.getCdAcCodeId());
    if (this.findById(cdAcCode.getCdAcCodeId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdAcCode.setCreateEmpNo(empNot);

    if(cdAcCode.getLastUpdateEmpNo() == null || cdAcCode.getLastUpdateEmpNo().isEmpty())
      cdAcCode.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdAcCodeReposDay.saveAndFlush(cdAcCode);	
    else if (dbName.equals(ContentName.onMon))
      return cdAcCodeReposMon.saveAndFlush(cdAcCode);
    else if (dbName.equals(ContentName.onHist))
      return cdAcCodeReposHist.saveAndFlush(cdAcCode);
    else 
    return cdAcCodeRepos.saveAndFlush(cdAcCode);
  }

  @Override
  public CdAcCode update(CdAcCode cdAcCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdAcCode.getCdAcCodeId());
    if (!empNot.isEmpty())
      cdAcCode.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdAcCodeReposDay.saveAndFlush(cdAcCode);	
    else if (dbName.equals(ContentName.onMon))
      return cdAcCodeReposMon.saveAndFlush(cdAcCode);
    else if (dbName.equals(ContentName.onHist))
      return cdAcCodeReposHist.saveAndFlush(cdAcCode);
    else 
    return cdAcCodeRepos.saveAndFlush(cdAcCode);
  }

  @Override
  public CdAcCode update2(CdAcCode cdAcCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdAcCode.getCdAcCodeId());
    if (!empNot.isEmpty())
      cdAcCode.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdAcCodeReposDay.saveAndFlush(cdAcCode);	
    else if (dbName.equals(ContentName.onMon))
      cdAcCodeReposMon.saveAndFlush(cdAcCode);
    else if (dbName.equals(ContentName.onHist))
        cdAcCodeReposHist.saveAndFlush(cdAcCode);
    else 
      cdAcCodeRepos.saveAndFlush(cdAcCode);	
    return this.findById(cdAcCode.getCdAcCodeId());
  }

  @Override
  public void delete(CdAcCode cdAcCode, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdAcCode.getCdAcCodeId());
    if (dbName.equals(ContentName.onDay)) {
      cdAcCodeReposDay.delete(cdAcCode);	
      cdAcCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdAcCodeReposMon.delete(cdAcCode);	
      cdAcCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdAcCodeReposHist.delete(cdAcCode);
      cdAcCodeReposHist.flush();
    }
    else {
      cdAcCodeRepos.delete(cdAcCode);
      cdAcCodeRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdAcCode> cdAcCode, TitaVo... titaVo) throws DBException {
    if (cdAcCode == null || cdAcCode.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CdAcCode t : cdAcCode){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdAcCode = cdAcCodeReposDay.saveAll(cdAcCode);	
      cdAcCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdAcCode = cdAcCodeReposMon.saveAll(cdAcCode);	
      cdAcCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdAcCode = cdAcCodeReposHist.saveAll(cdAcCode);
      cdAcCodeReposHist.flush();
    }
    else {
      cdAcCode = cdAcCodeRepos.saveAll(cdAcCode);
      cdAcCodeRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdAcCode> cdAcCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (cdAcCode == null || cdAcCode.size() == 0)
      throw new DBException(6);

    for (CdAcCode t : cdAcCode) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdAcCode = cdAcCodeReposDay.saveAll(cdAcCode);	
      cdAcCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdAcCode = cdAcCodeReposMon.saveAll(cdAcCode);	
      cdAcCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdAcCode = cdAcCodeReposHist.saveAll(cdAcCode);
      cdAcCodeReposHist.flush();
    }
    else {
      cdAcCode = cdAcCodeRepos.saveAll(cdAcCode);
      cdAcCodeRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdAcCode> cdAcCode, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdAcCode == null || cdAcCode.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdAcCodeReposDay.deleteAll(cdAcCode);	
      cdAcCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdAcCodeReposMon.deleteAll(cdAcCode);	
      cdAcCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdAcCodeReposHist.deleteAll(cdAcCode);
      cdAcCodeReposHist.flush();
    }
    else {
      cdAcCodeRepos.deleteAll(cdAcCode);
      cdAcCodeRepos.flush();
    }
  }

}
