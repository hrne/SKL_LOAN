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
import com.st1.itx.db.domain.Guarantor;
import com.st1.itx.db.domain.GuarantorId;
import com.st1.itx.db.repository.online.GuarantorRepository;
import com.st1.itx.db.repository.day.GuarantorRepositoryDay;
import com.st1.itx.db.repository.mon.GuarantorRepositoryMon;
import com.st1.itx.db.repository.hist.GuarantorRepositoryHist;
import com.st1.itx.db.service.GuarantorService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("guarantorService")
@Repository
public class GuarantorServiceImpl implements GuarantorService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(GuarantorServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private GuarantorRepository guarantorRepos;

  @Autowired
  private GuarantorRepositoryDay guarantorReposDay;

  @Autowired
  private GuarantorRepositoryMon guarantorReposMon;

  @Autowired
  private GuarantorRepositoryHist guarantorReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(guarantorRepos);
    org.junit.Assert.assertNotNull(guarantorReposDay);
    org.junit.Assert.assertNotNull(guarantorReposMon);
    org.junit.Assert.assertNotNull(guarantorReposHist);
  }

  @Override
  public Guarantor findById(GuarantorId guarantorId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + guarantorId);
    Optional<Guarantor> guarantor = null;
    if (dbName.equals(ContentName.onDay))
      guarantor = guarantorReposDay.findById(guarantorId);
    else if (dbName.equals(ContentName.onMon))
      guarantor = guarantorReposMon.findById(guarantorId);
    else if (dbName.equals(ContentName.onHist))
      guarantor = guarantorReposHist.findById(guarantorId);
    else 
      guarantor = guarantorRepos.findById(guarantorId);
    Guarantor obj = guarantor.isPresent() ? guarantor.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<Guarantor> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<Guarantor> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ApproveNo", "GuaUKey"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = guarantorReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = guarantorReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = guarantorReposHist.findAll(pageable);
    else 
      slice = guarantorRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<Guarantor> approveNoEq(int approveNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<Guarantor> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("approveNoEq " + dbName + " : " + "approveNo_0 : " + approveNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = guarantorReposDay.findAllByApproveNoIsOrderByGuaUKeyAsc(approveNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = guarantorReposMon.findAllByApproveNoIsOrderByGuaUKeyAsc(approveNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = guarantorReposHist.findAllByApproveNoIsOrderByGuaUKeyAsc(approveNo_0, pageable);
    else 
      slice = guarantorRepos.findAllByApproveNoIsOrderByGuaUKeyAsc(approveNo_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<Guarantor> guaUKeyEq(String guaUKey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<Guarantor> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("guaUKeyEq " + dbName + " : " + "guaUKey_0 : " + guaUKey_0);
    if (dbName.equals(ContentName.onDay))
      slice = guarantorReposDay.findAllByGuaUKeyIsOrderByApproveNoAsc(guaUKey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = guarantorReposMon.findAllByGuaUKeyIsOrderByApproveNoAsc(guaUKey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = guarantorReposHist.findAllByGuaUKeyIsOrderByApproveNoAsc(guaUKey_0, pageable);
    else 
      slice = guarantorRepos.findAllByGuaUKeyIsOrderByApproveNoAsc(guaUKey_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Guarantor holdById(GuarantorId guarantorId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + guarantorId);
    Optional<Guarantor> guarantor = null;
    if (dbName.equals(ContentName.onDay))
      guarantor = guarantorReposDay.findByGuarantorId(guarantorId);
    else if (dbName.equals(ContentName.onMon))
      guarantor = guarantorReposMon.findByGuarantorId(guarantorId);
    else if (dbName.equals(ContentName.onHist))
      guarantor = guarantorReposHist.findByGuarantorId(guarantorId);
    else 
      guarantor = guarantorRepos.findByGuarantorId(guarantorId);
    return guarantor.isPresent() ? guarantor.get() : null;
  }

  @Override
  public Guarantor holdById(Guarantor guarantor, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + guarantor.getGuarantorId());
    Optional<Guarantor> guarantorT = null;
    if (dbName.equals(ContentName.onDay))
      guarantorT = guarantorReposDay.findByGuarantorId(guarantor.getGuarantorId());
    else if (dbName.equals(ContentName.onMon))
      guarantorT = guarantorReposMon.findByGuarantorId(guarantor.getGuarantorId());
    else if (dbName.equals(ContentName.onHist))
      guarantorT = guarantorReposHist.findByGuarantorId(guarantor.getGuarantorId());
    else 
      guarantorT = guarantorRepos.findByGuarantorId(guarantor.getGuarantorId());
    return guarantorT.isPresent() ? guarantorT.get() : null;
  }

  @Override
  public Guarantor insert(Guarantor guarantor, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + guarantor.getGuarantorId());
    if (this.findById(guarantor.getGuarantorId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      guarantor.setCreateEmpNo(empNot);

    if(guarantor.getLastUpdateEmpNo() == null || guarantor.getLastUpdateEmpNo().isEmpty())
      guarantor.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return guarantorReposDay.saveAndFlush(guarantor);	
    else if (dbName.equals(ContentName.onMon))
      return guarantorReposMon.saveAndFlush(guarantor);
    else if (dbName.equals(ContentName.onHist))
      return guarantorReposHist.saveAndFlush(guarantor);
    else 
    return guarantorRepos.saveAndFlush(guarantor);
  }

  @Override
  public Guarantor update(Guarantor guarantor, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + guarantor.getGuarantorId());
    if (!empNot.isEmpty())
      guarantor.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return guarantorReposDay.saveAndFlush(guarantor);	
    else if (dbName.equals(ContentName.onMon))
      return guarantorReposMon.saveAndFlush(guarantor);
    else if (dbName.equals(ContentName.onHist))
      return guarantorReposHist.saveAndFlush(guarantor);
    else 
    return guarantorRepos.saveAndFlush(guarantor);
  }

  @Override
  public Guarantor update2(Guarantor guarantor, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + guarantor.getGuarantorId());
    if (!empNot.isEmpty())
      guarantor.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      guarantorReposDay.saveAndFlush(guarantor);	
    else if (dbName.equals(ContentName.onMon))
      guarantorReposMon.saveAndFlush(guarantor);
    else if (dbName.equals(ContentName.onHist))
        guarantorReposHist.saveAndFlush(guarantor);
    else 
      guarantorRepos.saveAndFlush(guarantor);	
    return this.findById(guarantor.getGuarantorId());
  }

  @Override
  public void delete(Guarantor guarantor, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + guarantor.getGuarantorId());
    if (dbName.equals(ContentName.onDay)) {
      guarantorReposDay.delete(guarantor);	
      guarantorReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      guarantorReposMon.delete(guarantor);	
      guarantorReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      guarantorReposHist.delete(guarantor);
      guarantorReposHist.flush();
    }
    else {
      guarantorRepos.delete(guarantor);
      guarantorRepos.flush();
    }
   }

  @Override
  public void insertAll(List<Guarantor> guarantor, TitaVo... titaVo) throws DBException {
    if (guarantor == null || guarantor.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (Guarantor t : guarantor){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      guarantor = guarantorReposDay.saveAll(guarantor);	
      guarantorReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      guarantor = guarantorReposMon.saveAll(guarantor);	
      guarantorReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      guarantor = guarantorReposHist.saveAll(guarantor);
      guarantorReposHist.flush();
    }
    else {
      guarantor = guarantorRepos.saveAll(guarantor);
      guarantorRepos.flush();
    }
    }

  @Override
  public void updateAll(List<Guarantor> guarantor, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (guarantor == null || guarantor.size() == 0)
      throw new DBException(6);

    for (Guarantor t : guarantor) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      guarantor = guarantorReposDay.saveAll(guarantor);	
      guarantorReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      guarantor = guarantorReposMon.saveAll(guarantor);	
      guarantorReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      guarantor = guarantorReposHist.saveAll(guarantor);
      guarantorReposHist.flush();
    }
    else {
      guarantor = guarantorRepos.saveAll(guarantor);
      guarantorRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<Guarantor> guarantor, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (guarantor == null || guarantor.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      guarantorReposDay.deleteAll(guarantor);	
      guarantorReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      guarantorReposMon.deleteAll(guarantor);	
      guarantorReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      guarantorReposHist.deleteAll(guarantor);
      guarantorReposHist.flush();
    }
    else {
      guarantorRepos.deleteAll(guarantor);
      guarantorRepos.flush();
    }
  }

}
