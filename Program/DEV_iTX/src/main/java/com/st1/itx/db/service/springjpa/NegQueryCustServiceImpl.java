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
import com.st1.itx.db.domain.NegQueryCust;
import com.st1.itx.db.domain.NegQueryCustId;
import com.st1.itx.db.repository.online.NegQueryCustRepository;
import com.st1.itx.db.repository.day.NegQueryCustRepositoryDay;
import com.st1.itx.db.repository.mon.NegQueryCustRepositoryMon;
import com.st1.itx.db.repository.hist.NegQueryCustRepositoryHist;
import com.st1.itx.db.service.NegQueryCustService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("negQueryCustService")
@Repository
public class NegQueryCustServiceImpl implements NegQueryCustService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(NegQueryCustServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private NegQueryCustRepository negQueryCustRepos;

  @Autowired
  private NegQueryCustRepositoryDay negQueryCustReposDay;

  @Autowired
  private NegQueryCustRepositoryMon negQueryCustReposMon;

  @Autowired
  private NegQueryCustRepositoryHist negQueryCustReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(negQueryCustRepos);
    org.junit.Assert.assertNotNull(negQueryCustReposDay);
    org.junit.Assert.assertNotNull(negQueryCustReposMon);
    org.junit.Assert.assertNotNull(negQueryCustReposHist);
  }

  @Override
  public NegQueryCust findById(NegQueryCustId negQueryCustId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + negQueryCustId);
    Optional<NegQueryCust> negQueryCust = null;
    if (dbName.equals(ContentName.onDay))
      negQueryCust = negQueryCustReposDay.findById(negQueryCustId);
    else if (dbName.equals(ContentName.onMon))
      negQueryCust = negQueryCustReposMon.findById(negQueryCustId);
    else if (dbName.equals(ContentName.onHist))
      negQueryCust = negQueryCustReposHist.findById(negQueryCustId);
    else 
      negQueryCust = negQueryCustRepos.findById(negQueryCustId);
    NegQueryCust obj = negQueryCust.isPresent() ? negQueryCust.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<NegQueryCust> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegQueryCust> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcDate", "CustId"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = negQueryCustReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negQueryCustReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negQueryCustReposHist.findAll(pageable);
    else 
      slice = negQueryCustRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegQueryCust> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegQueryCust> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = negQueryCustReposDay.findAllByCustIdIs(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negQueryCustReposMon.findAllByCustIdIs(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negQueryCustReposHist.findAllByCustIdIs(custId_0, pageable);
    else 
      slice = negQueryCustRepos.findAllByCustIdIs(custId_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegQueryCust> FirstAcDate(int acDate_0, String fileYN_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegQueryCust> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("FirstAcDate " + dbName + " : " + "acDate_0 : " + acDate_0 + " fileYN_1 : " +  fileYN_1);
    if (dbName.equals(ContentName.onDay))
      slice = negQueryCustReposDay.findAllByAcDateIsAndFileYNIsOrderBySeqNoDesc(acDate_0, fileYN_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negQueryCustReposMon.findAllByAcDateIsAndFileYNIsOrderBySeqNoDesc(acDate_0, fileYN_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negQueryCustReposHist.findAllByAcDateIsAndFileYNIsOrderBySeqNoDesc(acDate_0, fileYN_1, pageable);
    else 
      slice = negQueryCustRepos.findAllByAcDateIsAndFileYNIsOrderBySeqNoDesc(acDate_0, fileYN_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegQueryCust> AcDateEq(int acDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegQueryCust> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("AcDateEq " + dbName + " : " + "acDate_0 : " + acDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = negQueryCustReposDay.findAllByAcDateIsOrderBySeqNoDesc(acDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negQueryCustReposMon.findAllByAcDateIsOrderBySeqNoDesc(acDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negQueryCustReposHist.findAllByAcDateIsOrderBySeqNoDesc(acDate_0, pageable);
    else 
      slice = negQueryCustRepos.findAllByAcDateIsOrderBySeqNoDesc(acDate_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public NegQueryCust holdById(NegQueryCustId negQueryCustId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + negQueryCustId);
    Optional<NegQueryCust> negQueryCust = null;
    if (dbName.equals(ContentName.onDay))
      negQueryCust = negQueryCustReposDay.findByNegQueryCustId(negQueryCustId);
    else if (dbName.equals(ContentName.onMon))
      negQueryCust = negQueryCustReposMon.findByNegQueryCustId(negQueryCustId);
    else if (dbName.equals(ContentName.onHist))
      negQueryCust = negQueryCustReposHist.findByNegQueryCustId(negQueryCustId);
    else 
      negQueryCust = negQueryCustRepos.findByNegQueryCustId(negQueryCustId);
    return negQueryCust.isPresent() ? negQueryCust.get() : null;
  }

  @Override
  public NegQueryCust holdById(NegQueryCust negQueryCust, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + negQueryCust.getNegQueryCustId());
    Optional<NegQueryCust> negQueryCustT = null;
    if (dbName.equals(ContentName.onDay))
      negQueryCustT = negQueryCustReposDay.findByNegQueryCustId(negQueryCust.getNegQueryCustId());
    else if (dbName.equals(ContentName.onMon))
      negQueryCustT = negQueryCustReposMon.findByNegQueryCustId(negQueryCust.getNegQueryCustId());
    else if (dbName.equals(ContentName.onHist))
      negQueryCustT = negQueryCustReposHist.findByNegQueryCustId(negQueryCust.getNegQueryCustId());
    else 
      negQueryCustT = negQueryCustRepos.findByNegQueryCustId(negQueryCust.getNegQueryCustId());
    return negQueryCustT.isPresent() ? negQueryCustT.get() : null;
  }

  @Override
  public NegQueryCust insert(NegQueryCust negQueryCust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Insert..." + dbName + " " + negQueryCust.getNegQueryCustId());
    if (this.findById(negQueryCust.getNegQueryCustId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      negQueryCust.setCreateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return negQueryCustReposDay.saveAndFlush(negQueryCust);	
    else if (dbName.equals(ContentName.onMon))
      return negQueryCustReposMon.saveAndFlush(negQueryCust);
    else if (dbName.equals(ContentName.onHist))
      return negQueryCustReposHist.saveAndFlush(negQueryCust);
    else 
    return negQueryCustRepos.saveAndFlush(negQueryCust);
  }

  @Override
  public NegQueryCust update(NegQueryCust negQueryCust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + negQueryCust.getNegQueryCustId());
    if (!empNot.isEmpty())
      negQueryCust.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return negQueryCustReposDay.saveAndFlush(negQueryCust);	
    else if (dbName.equals(ContentName.onMon))
      return negQueryCustReposMon.saveAndFlush(negQueryCust);
    else if (dbName.equals(ContentName.onHist))
      return negQueryCustReposHist.saveAndFlush(negQueryCust);
    else 
    return negQueryCustRepos.saveAndFlush(negQueryCust);
  }

  @Override
  public NegQueryCust update2(NegQueryCust negQueryCust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + negQueryCust.getNegQueryCustId());
    if (!empNot.isEmpty())
      negQueryCust.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      negQueryCustReposDay.saveAndFlush(negQueryCust);	
    else if (dbName.equals(ContentName.onMon))
      negQueryCustReposMon.saveAndFlush(negQueryCust);
    else if (dbName.equals(ContentName.onHist))
        negQueryCustReposHist.saveAndFlush(negQueryCust);
    else 
      negQueryCustRepos.saveAndFlush(negQueryCust);	
    return this.findById(negQueryCust.getNegQueryCustId());
  }

  @Override
  public void delete(NegQueryCust negQueryCust, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + negQueryCust.getNegQueryCustId());
    if (dbName.equals(ContentName.onDay)) {
      negQueryCustReposDay.delete(negQueryCust);	
      negQueryCustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negQueryCustReposMon.delete(negQueryCust);	
      negQueryCustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negQueryCustReposHist.delete(negQueryCust);
      negQueryCustReposHist.flush();
    }
    else {
      negQueryCustRepos.delete(negQueryCust);
      negQueryCustRepos.flush();
    }
   }

  @Override
  public void insertAll(List<NegQueryCust> negQueryCust, TitaVo... titaVo) throws DBException {
    if (negQueryCust == null || negQueryCust.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}    logger.info("InsertAll...");
    for (NegQueryCust t : negQueryCust) 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      negQueryCust = negQueryCustReposDay.saveAll(negQueryCust);	
      negQueryCustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negQueryCust = negQueryCustReposMon.saveAll(negQueryCust);	
      negQueryCustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negQueryCust = negQueryCustReposHist.saveAll(negQueryCust);
      negQueryCustReposHist.flush();
    }
    else {
      negQueryCust = negQueryCustRepos.saveAll(negQueryCust);
      negQueryCustRepos.flush();
    }
    }

  @Override
  public void updateAll(List<NegQueryCust> negQueryCust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (negQueryCust == null || negQueryCust.size() == 0)
      throw new DBException(6);

    for (NegQueryCust t : negQueryCust) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      negQueryCust = negQueryCustReposDay.saveAll(negQueryCust);	
      negQueryCustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negQueryCust = negQueryCustReposMon.saveAll(negQueryCust);	
      negQueryCustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negQueryCust = negQueryCustReposHist.saveAll(negQueryCust);
      negQueryCustReposHist.flush();
    }
    else {
      negQueryCust = negQueryCustRepos.saveAll(negQueryCust);
      negQueryCustRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<NegQueryCust> negQueryCust, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (negQueryCust == null || negQueryCust.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      negQueryCustReposDay.deleteAll(negQueryCust);	
      negQueryCustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negQueryCustReposMon.deleteAll(negQueryCust);	
      negQueryCustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negQueryCustReposHist.deleteAll(negQueryCust);
      negQueryCustReposHist.flush();
    }
    else {
      negQueryCustRepos.deleteAll(negQueryCust);
      negQueryCustRepos.flush();
    }
  }

}
