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
import com.st1.itx.db.domain.CustNotice;
import com.st1.itx.db.domain.CustNoticeId;
import com.st1.itx.db.repository.online.CustNoticeRepository;
import com.st1.itx.db.repository.day.CustNoticeRepositoryDay;
import com.st1.itx.db.repository.mon.CustNoticeRepositoryMon;
import com.st1.itx.db.repository.hist.CustNoticeRepositoryHist;
import com.st1.itx.db.service.CustNoticeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("custNoticeService")
@Repository
public class CustNoticeServiceImpl implements CustNoticeService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(CustNoticeServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CustNoticeRepository custNoticeRepos;

  @Autowired
  private CustNoticeRepositoryDay custNoticeReposDay;

  @Autowired
  private CustNoticeRepositoryMon custNoticeReposMon;

  @Autowired
  private CustNoticeRepositoryHist custNoticeReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(custNoticeRepos);
    org.junit.Assert.assertNotNull(custNoticeReposDay);
    org.junit.Assert.assertNotNull(custNoticeReposMon);
    org.junit.Assert.assertNotNull(custNoticeReposHist);
  }

  @Override
  public CustNotice findById(CustNoticeId custNoticeId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + custNoticeId);
    Optional<CustNotice> custNotice = null;
    if (dbName.equals(ContentName.onDay))
      custNotice = custNoticeReposDay.findById(custNoticeId);
    else if (dbName.equals(ContentName.onMon))
      custNotice = custNoticeReposMon.findById(custNoticeId);
    else if (dbName.equals(ContentName.onHist))
      custNotice = custNoticeReposHist.findById(custNoticeId);
    else 
      custNotice = custNoticeRepos.findById(custNoticeId);
    CustNotice obj = custNotice.isPresent() ? custNotice.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CustNotice> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustNotice> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "FormNo"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = custNoticeReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custNoticeReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custNoticeReposHist.findAll(pageable);
    else 
      slice = custNoticeRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CustNotice> findCustNo(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustNotice> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findCustNo " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = custNoticeReposDay.findAllByCustNoIs(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custNoticeReposMon.findAllByCustNoIs(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custNoticeReposHist.findAllByCustNoIs(custNo_0, pageable);
    else 
      slice = custNoticeRepos.findAllByCustNoIs(custNo_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CustNotice> findFormNo(String formNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustNotice> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findFormNo " + dbName + " : " + "formNo_0 : " + formNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = custNoticeReposDay.findAllByFormNoIs(formNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custNoticeReposMon.findAllByFormNoIs(formNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custNoticeReposHist.findAllByFormNoIs(formNo_0, pageable);
    else 
      slice = custNoticeRepos.findAllByFormNoIs(formNo_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CustNotice> facmNoEq(int custNo_0, int facmNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustNotice> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("facmNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " facmNo_2 : " +  facmNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = custNoticeReposDay.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = custNoticeReposMon.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = custNoticeReposHist.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);
    else 
      slice = custNoticeRepos.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CustNotice holdById(CustNoticeId custNoticeId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + custNoticeId);
    Optional<CustNotice> custNotice = null;
    if (dbName.equals(ContentName.onDay))
      custNotice = custNoticeReposDay.findByCustNoticeId(custNoticeId);
    else if (dbName.equals(ContentName.onMon))
      custNotice = custNoticeReposMon.findByCustNoticeId(custNoticeId);
    else if (dbName.equals(ContentName.onHist))
      custNotice = custNoticeReposHist.findByCustNoticeId(custNoticeId);
    else 
      custNotice = custNoticeRepos.findByCustNoticeId(custNoticeId);
    return custNotice.isPresent() ? custNotice.get() : null;
  }

  @Override
  public CustNotice holdById(CustNotice custNotice, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + custNotice.getCustNoticeId());
    Optional<CustNotice> custNoticeT = null;
    if (dbName.equals(ContentName.onDay))
      custNoticeT = custNoticeReposDay.findByCustNoticeId(custNotice.getCustNoticeId());
    else if (dbName.equals(ContentName.onMon))
      custNoticeT = custNoticeReposMon.findByCustNoticeId(custNotice.getCustNoticeId());
    else if (dbName.equals(ContentName.onHist))
      custNoticeT = custNoticeReposHist.findByCustNoticeId(custNotice.getCustNoticeId());
    else 
      custNoticeT = custNoticeRepos.findByCustNoticeId(custNotice.getCustNoticeId());
    return custNoticeT.isPresent() ? custNoticeT.get() : null;
  }

  @Override
  public CustNotice insert(CustNotice custNotice, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + custNotice.getCustNoticeId());
    if (this.findById(custNotice.getCustNoticeId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      custNotice.setCreateEmpNo(empNot);

    if(custNotice.getLastUpdateEmpNo() == null || custNotice.getLastUpdateEmpNo().isEmpty())
      custNotice.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return custNoticeReposDay.saveAndFlush(custNotice);	
    else if (dbName.equals(ContentName.onMon))
      return custNoticeReposMon.saveAndFlush(custNotice);
    else if (dbName.equals(ContentName.onHist))
      return custNoticeReposHist.saveAndFlush(custNotice);
    else 
    return custNoticeRepos.saveAndFlush(custNotice);
  }

  @Override
  public CustNotice update(CustNotice custNotice, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + custNotice.getCustNoticeId());
    if (!empNot.isEmpty())
      custNotice.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return custNoticeReposDay.saveAndFlush(custNotice);	
    else if (dbName.equals(ContentName.onMon))
      return custNoticeReposMon.saveAndFlush(custNotice);
    else if (dbName.equals(ContentName.onHist))
      return custNoticeReposHist.saveAndFlush(custNotice);
    else 
    return custNoticeRepos.saveAndFlush(custNotice);
  }

  @Override
  public CustNotice update2(CustNotice custNotice, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + custNotice.getCustNoticeId());
    if (!empNot.isEmpty())
      custNotice.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      custNoticeReposDay.saveAndFlush(custNotice);	
    else if (dbName.equals(ContentName.onMon))
      custNoticeReposMon.saveAndFlush(custNotice);
    else if (dbName.equals(ContentName.onHist))
        custNoticeReposHist.saveAndFlush(custNotice);
    else 
      custNoticeRepos.saveAndFlush(custNotice);	
    return this.findById(custNotice.getCustNoticeId());
  }

  @Override
  public void delete(CustNotice custNotice, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + custNotice.getCustNoticeId());
    if (dbName.equals(ContentName.onDay)) {
      custNoticeReposDay.delete(custNotice);	
      custNoticeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custNoticeReposMon.delete(custNotice);	
      custNoticeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custNoticeReposHist.delete(custNotice);
      custNoticeReposHist.flush();
    }
    else {
      custNoticeRepos.delete(custNotice);
      custNoticeRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CustNotice> custNotice, TitaVo... titaVo) throws DBException {
    if (custNotice == null || custNotice.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (CustNotice t : custNotice){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      custNotice = custNoticeReposDay.saveAll(custNotice);	
      custNoticeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custNotice = custNoticeReposMon.saveAll(custNotice);	
      custNoticeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custNotice = custNoticeReposHist.saveAll(custNotice);
      custNoticeReposHist.flush();
    }
    else {
      custNotice = custNoticeRepos.saveAll(custNotice);
      custNoticeRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CustNotice> custNotice, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (custNotice == null || custNotice.size() == 0)
      throw new DBException(6);

    for (CustNotice t : custNotice) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      custNotice = custNoticeReposDay.saveAll(custNotice);	
      custNoticeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custNotice = custNoticeReposMon.saveAll(custNotice);	
      custNoticeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custNotice = custNoticeReposHist.saveAll(custNotice);
      custNoticeReposHist.flush();
    }
    else {
      custNotice = custNoticeRepos.saveAll(custNotice);
      custNoticeRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CustNotice> custNotice, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (custNotice == null || custNotice.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      custNoticeReposDay.deleteAll(custNotice);	
      custNoticeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      custNoticeReposMon.deleteAll(custNotice);	
      custNoticeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      custNoticeReposHist.deleteAll(custNotice);
      custNoticeReposHist.flush();
    }
    else {
      custNoticeRepos.deleteAll(custNotice);
      custNoticeRepos.flush();
    }
  }

}
