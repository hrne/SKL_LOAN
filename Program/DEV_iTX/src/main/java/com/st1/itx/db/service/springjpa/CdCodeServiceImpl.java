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
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.repository.online.CdCodeRepository;
import com.st1.itx.db.repository.day.CdCodeRepositoryDay;
import com.st1.itx.db.repository.mon.CdCodeRepositoryMon;
import com.st1.itx.db.repository.hist.CdCodeRepositoryHist;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdCodeService")
@Repository
public class CdCodeServiceImpl implements CdCodeService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(CdCodeServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdCodeRepository cdCodeRepos;

  @Autowired
  private CdCodeRepositoryDay cdCodeReposDay;

  @Autowired
  private CdCodeRepositoryMon cdCodeReposMon;

  @Autowired
  private CdCodeRepositoryHist cdCodeReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdCodeRepos);
    org.junit.Assert.assertNotNull(cdCodeReposDay);
    org.junit.Assert.assertNotNull(cdCodeReposMon);
    org.junit.Assert.assertNotNull(cdCodeReposHist);
  }

  @Override
  public CdCode findById(CdCodeId cdCodeId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + cdCodeId);
    Optional<CdCode> cdCode = null;
    if (dbName.equals(ContentName.onDay))
      cdCode = cdCodeReposDay.findById(cdCodeId);
    else if (dbName.equals(ContentName.onMon))
      cdCode = cdCodeReposMon.findById(cdCodeId);
    else if (dbName.equals(ContentName.onHist))
      cdCode = cdCodeReposHist.findById(cdCodeId);
    else 
      cdCode = cdCodeRepos.findById(cdCodeId);
    CdCode obj = cdCode.isPresent() ? cdCode.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdCode> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdCode> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DefCode", "Code"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdCodeReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCodeReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCodeReposHist.findAll(pageable);
    else 
      slice = cdCodeRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdCode> defCodeEq(String defCode_0, String code_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdCode> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("defCodeEq " + dbName + " : " + "defCode_0 : " + defCode_0 + " code_1 : " +  code_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdCodeReposDay.findAllByDefCodeIsAndCodeLikeOrderByCodeAsc(defCode_0, code_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCodeReposMon.findAllByDefCodeIsAndCodeLikeOrderByCodeAsc(defCode_0, code_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCodeReposHist.findAllByDefCodeIsAndCodeLikeOrderByCodeAsc(defCode_0, code_1, pageable);
    else 
      slice = cdCodeRepos.findAllByDefCodeIsAndCodeLikeOrderByCodeAsc(defCode_0, code_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdCode> defCodeEq2(String defCode_0, int defType_1, String code_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdCode> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("defCodeEq2 " + dbName + " : " + "defCode_0 : " + defCode_0 + " defType_1 : " +  defType_1 + " code_2 : " +  code_2);
    if (dbName.equals(ContentName.onDay))
      slice = cdCodeReposDay.findAllByDefCodeIsAndDefTypeIsAndCodeLikeOrderByCodeAsc(defCode_0, defType_1, code_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCodeReposMon.findAllByDefCodeIsAndDefTypeIsAndCodeLikeOrderByCodeAsc(defCode_0, defType_1, code_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCodeReposHist.findAllByDefCodeIsAndDefTypeIsAndCodeLikeOrderByCodeAsc(defCode_0, defType_1, code_2, pageable);
    else 
      slice = cdCodeRepos.findAllByDefCodeIsAndDefTypeIsAndCodeLikeOrderByCodeAsc(defCode_0, defType_1, code_2, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdCode> DefTypeEq(String defCode_0, int defType_1, String code_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdCode> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("DefTypeEq " + dbName + " : " + "defCode_0 : " + defCode_0 + " defType_1 : " +  defType_1 + " code_2 : " +  code_2);
    if (dbName.equals(ContentName.onDay))
      slice = cdCodeReposDay.findAllByDefCodeNotAndDefTypeIsAndCodeLikeOrderByDefCodeAscCodeAsc(defCode_0, defType_1, code_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCodeReposMon.findAllByDefCodeNotAndDefTypeIsAndCodeLikeOrderByDefCodeAscCodeAsc(defCode_0, defType_1, code_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCodeReposHist.findAllByDefCodeNotAndDefTypeIsAndCodeLikeOrderByDefCodeAscCodeAsc(defCode_0, defType_1, code_2, pageable);
    else 
      slice = cdCodeRepos.findAllByDefCodeNotAndDefTypeIsAndCodeLikeOrderByDefCodeAscCodeAsc(defCode_0, defType_1, code_2, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdCode> getDefList(int defType_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdCode> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("getDefList " + dbName + " : " + "defType_0 : " + defType_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdCodeReposDay.findAllByDefTypeIsOrderByDefCodeAscCodeAsc(defType_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCodeReposMon.findAllByDefTypeIsOrderByDefCodeAscCodeAsc(defType_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCodeReposHist.findAllByDefTypeIsOrderByDefCodeAscCodeAsc(defType_0, pageable);
    else 
      slice = cdCodeRepos.findAllByDefTypeIsOrderByDefCodeAscCodeAsc(defType_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdCode> getCodeList(int defType_0, String defCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdCode> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("getCodeList " + dbName + " : " + "defType_0 : " + defType_0 + " defCode_1 : " +  defCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdCodeReposDay.findAllByDefTypeIsAndDefCodeIsOrderByDefCodeAscCodeAsc(defType_0, defCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCodeReposMon.findAllByDefTypeIsAndDefCodeIsOrderByDefCodeAscCodeAsc(defType_0, defCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCodeReposHist.findAllByDefTypeIsAndDefCodeIsOrderByDefCodeAscCodeAsc(defType_0, defCode_1, pageable);
    else 
      slice = cdCodeRepos.findAllByDefTypeIsAndDefCodeIsOrderByDefCodeAscCodeAsc(defType_0, defCode_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdCode getItemFirst(int defType_0, String defCode_1, String code_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("getItemFirst " + dbName + " : " + "defType_0 : " + defType_0 + " defCode_1 : " +  defCode_1 + " code_2 : " +  code_2);
    Optional<CdCode> cdCodeT = null;
    if (dbName.equals(ContentName.onDay))
      cdCodeT = cdCodeReposDay.findTopByDefTypeIsAndDefCodeIsAndCodeIsOrderByDefCodeAscCodeAsc(defType_0, defCode_1, code_2);
    else if (dbName.equals(ContentName.onMon))
      cdCodeT = cdCodeReposMon.findTopByDefTypeIsAndDefCodeIsAndCodeIsOrderByDefCodeAscCodeAsc(defType_0, defCode_1, code_2);
    else if (dbName.equals(ContentName.onHist))
      cdCodeT = cdCodeReposHist.findTopByDefTypeIsAndDefCodeIsAndCodeIsOrderByDefCodeAscCodeAsc(defType_0, defCode_1, code_2);
    else 
      cdCodeT = cdCodeRepos.findTopByDefTypeIsAndDefCodeIsAndCodeIsOrderByDefCodeAscCodeAsc(defType_0, defCode_1, code_2);
    return cdCodeT.isPresent() ? cdCodeT.get() : null;
  }

  @Override
  public Slice<CdCode> getCodeList2(int defType_0, String defCode_1, List<String> code_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdCode> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("getCodeList2 " + dbName + " : " + "defType_0 : " + defType_0 + " defCode_1 : " +  defCode_1 + " code_2 : " +  code_2);
    if (dbName.equals(ContentName.onDay))
      slice = cdCodeReposDay.findAllByDefTypeIsAndDefCodeIsAndCodeInOrderByDefCodeAscCodeAsc(defType_0, defCode_1, code_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCodeReposMon.findAllByDefTypeIsAndDefCodeIsAndCodeInOrderByDefCodeAscCodeAsc(defType_0, defCode_1, code_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCodeReposHist.findAllByDefTypeIsAndDefCodeIsAndCodeInOrderByDefCodeAscCodeAsc(defType_0, defCode_1, code_2, pageable);
    else 
      slice = cdCodeRepos.findAllByDefTypeIsAndDefCodeIsAndCodeInOrderByDefCodeAscCodeAsc(defType_0, defCode_1, code_2, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdCode holdById(CdCodeId cdCodeId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + cdCodeId);
    Optional<CdCode> cdCode = null;
    if (dbName.equals(ContentName.onDay))
      cdCode = cdCodeReposDay.findByCdCodeId(cdCodeId);
    else if (dbName.equals(ContentName.onMon))
      cdCode = cdCodeReposMon.findByCdCodeId(cdCodeId);
    else if (dbName.equals(ContentName.onHist))
      cdCode = cdCodeReposHist.findByCdCodeId(cdCodeId);
    else 
      cdCode = cdCodeRepos.findByCdCodeId(cdCodeId);
    return cdCode.isPresent() ? cdCode.get() : null;
  }

  @Override
  public CdCode holdById(CdCode cdCode, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + cdCode.getCdCodeId());
    Optional<CdCode> cdCodeT = null;
    if (dbName.equals(ContentName.onDay))
      cdCodeT = cdCodeReposDay.findByCdCodeId(cdCode.getCdCodeId());
    else if (dbName.equals(ContentName.onMon))
      cdCodeT = cdCodeReposMon.findByCdCodeId(cdCode.getCdCodeId());
    else if (dbName.equals(ContentName.onHist))
      cdCodeT = cdCodeReposHist.findByCdCodeId(cdCode.getCdCodeId());
    else 
      cdCodeT = cdCodeRepos.findByCdCodeId(cdCode.getCdCodeId());
    return cdCodeT.isPresent() ? cdCodeT.get() : null;
  }

  @Override
  public CdCode insert(CdCode cdCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + cdCode.getCdCodeId());
    if (this.findById(cdCode.getCdCodeId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdCode.setCreateEmpNo(empNot);

    if(cdCode.getLastUpdateEmpNo() == null || cdCode.getLastUpdateEmpNo().isEmpty())
      cdCode.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdCodeReposDay.saveAndFlush(cdCode);	
    else if (dbName.equals(ContentName.onMon))
      return cdCodeReposMon.saveAndFlush(cdCode);
    else if (dbName.equals(ContentName.onHist))
      return cdCodeReposHist.saveAndFlush(cdCode);
    else 
    return cdCodeRepos.saveAndFlush(cdCode);
  }

  @Override
  public CdCode update(CdCode cdCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + cdCode.getCdCodeId());
    if (!empNot.isEmpty())
      cdCode.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdCodeReposDay.saveAndFlush(cdCode);	
    else if (dbName.equals(ContentName.onMon))
      return cdCodeReposMon.saveAndFlush(cdCode);
    else if (dbName.equals(ContentName.onHist))
      return cdCodeReposHist.saveAndFlush(cdCode);
    else 
    return cdCodeRepos.saveAndFlush(cdCode);
  }

  @Override
  public CdCode update2(CdCode cdCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + cdCode.getCdCodeId());
    if (!empNot.isEmpty())
      cdCode.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdCodeReposDay.saveAndFlush(cdCode);	
    else if (dbName.equals(ContentName.onMon))
      cdCodeReposMon.saveAndFlush(cdCode);
    else if (dbName.equals(ContentName.onHist))
        cdCodeReposHist.saveAndFlush(cdCode);
    else 
      cdCodeRepos.saveAndFlush(cdCode);	
    return this.findById(cdCode.getCdCodeId());
  }

  @Override
  public void delete(CdCode cdCode, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + cdCode.getCdCodeId());
    if (dbName.equals(ContentName.onDay)) {
      cdCodeReposDay.delete(cdCode);	
      cdCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdCodeReposMon.delete(cdCode);	
      cdCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdCodeReposHist.delete(cdCode);
      cdCodeReposHist.flush();
    }
    else {
      cdCodeRepos.delete(cdCode);
      cdCodeRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdCode> cdCode, TitaVo... titaVo) throws DBException {
    if (cdCode == null || cdCode.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (CdCode t : cdCode){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdCode = cdCodeReposDay.saveAll(cdCode);	
      cdCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdCode = cdCodeReposMon.saveAll(cdCode);	
      cdCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdCode = cdCodeReposHist.saveAll(cdCode);
      cdCodeReposHist.flush();
    }
    else {
      cdCode = cdCodeRepos.saveAll(cdCode);
      cdCodeRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdCode> cdCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (cdCode == null || cdCode.size() == 0)
      throw new DBException(6);

    for (CdCode t : cdCode) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdCode = cdCodeReposDay.saveAll(cdCode);	
      cdCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdCode = cdCodeReposMon.saveAll(cdCode);	
      cdCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdCode = cdCodeReposHist.saveAll(cdCode);
      cdCodeReposHist.flush();
    }
    else {
      cdCode = cdCodeRepos.saveAll(cdCode);
      cdCodeRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdCode> cdCode, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdCode == null || cdCode.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdCodeReposDay.deleteAll(cdCode);	
      cdCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdCodeReposMon.deleteAll(cdCode);	
      cdCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdCodeReposHist.deleteAll(cdCode);
      cdCodeReposHist.flush();
    }
    else {
      cdCodeRepos.deleteAll(cdCode);
      cdCodeRepos.flush();
    }
  }

}
