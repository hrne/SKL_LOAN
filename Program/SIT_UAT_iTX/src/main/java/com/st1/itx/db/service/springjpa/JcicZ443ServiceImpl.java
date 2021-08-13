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
import com.st1.itx.db.domain.JcicZ443;
import com.st1.itx.db.domain.JcicZ443Id;
import com.st1.itx.db.repository.online.JcicZ443Repository;
import com.st1.itx.db.repository.day.JcicZ443RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ443RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ443RepositoryHist;
import com.st1.itx.db.service.JcicZ443Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ443Service")
@Repository
public class JcicZ443ServiceImpl implements JcicZ443Service, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(JcicZ443ServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ443Repository jcicZ443Repos;

  @Autowired
  private JcicZ443RepositoryDay jcicZ443ReposDay;

  @Autowired
  private JcicZ443RepositoryMon jcicZ443ReposMon;

  @Autowired
  private JcicZ443RepositoryHist jcicZ443ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ443Repos);
    org.junit.Assert.assertNotNull(jcicZ443ReposDay);
    org.junit.Assert.assertNotNull(jcicZ443ReposMon);
    org.junit.Assert.assertNotNull(jcicZ443ReposHist);
  }

  @Override
  public JcicZ443 findById(JcicZ443Id jcicZ443Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + jcicZ443Id);
    Optional<JcicZ443> jcicZ443 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ443 = jcicZ443ReposDay.findById(jcicZ443Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ443 = jcicZ443ReposMon.findById(jcicZ443Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ443 = jcicZ443ReposHist.findById(jcicZ443Id);
    else 
      jcicZ443 = jcicZ443Repos.findById(jcicZ443Id);
    JcicZ443 obj = jcicZ443.isPresent() ? jcicZ443.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ443> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ443> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "BankId", "MaxMainCode", "Account"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ443ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ443ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ443ReposHist.findAll(pageable);
    else 
      slice = jcicZ443Repos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ443> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ443> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ443ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ443ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ443ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else 
      slice = jcicZ443Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ443> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ443> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("RcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ443ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ443ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ443ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else 
      slice = jcicZ443Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ443> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ443> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ443ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ443ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ443ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else 
      slice = jcicZ443Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ443 holdById(JcicZ443Id jcicZ443Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ443Id);
    Optional<JcicZ443> jcicZ443 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ443 = jcicZ443ReposDay.findByJcicZ443Id(jcicZ443Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ443 = jcicZ443ReposMon.findByJcicZ443Id(jcicZ443Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ443 = jcicZ443ReposHist.findByJcicZ443Id(jcicZ443Id);
    else 
      jcicZ443 = jcicZ443Repos.findByJcicZ443Id(jcicZ443Id);
    return jcicZ443.isPresent() ? jcicZ443.get() : null;
  }

  @Override
  public JcicZ443 holdById(JcicZ443 jcicZ443, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ443.getJcicZ443Id());
    Optional<JcicZ443> jcicZ443T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ443T = jcicZ443ReposDay.findByJcicZ443Id(jcicZ443.getJcicZ443Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ443T = jcicZ443ReposMon.findByJcicZ443Id(jcicZ443.getJcicZ443Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ443T = jcicZ443ReposHist.findByJcicZ443Id(jcicZ443.getJcicZ443Id());
    else 
      jcicZ443T = jcicZ443Repos.findByJcicZ443Id(jcicZ443.getJcicZ443Id());
    return jcicZ443T.isPresent() ? jcicZ443T.get() : null;
  }

  @Override
  public JcicZ443 insert(JcicZ443 jcicZ443, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + jcicZ443.getJcicZ443Id());
    if (this.findById(jcicZ443.getJcicZ443Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ443.setCreateEmpNo(empNot);

    if(jcicZ443.getLastUpdateEmpNo() == null || jcicZ443.getLastUpdateEmpNo().isEmpty())
      jcicZ443.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ443ReposDay.saveAndFlush(jcicZ443);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ443ReposMon.saveAndFlush(jcicZ443);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ443ReposHist.saveAndFlush(jcicZ443);
    else 
    return jcicZ443Repos.saveAndFlush(jcicZ443);
  }

  @Override
  public JcicZ443 update(JcicZ443 jcicZ443, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ443.getJcicZ443Id());
    if (!empNot.isEmpty())
      jcicZ443.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ443ReposDay.saveAndFlush(jcicZ443);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ443ReposMon.saveAndFlush(jcicZ443);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ443ReposHist.saveAndFlush(jcicZ443);
    else 
    return jcicZ443Repos.saveAndFlush(jcicZ443);
  }

  @Override
  public JcicZ443 update2(JcicZ443 jcicZ443, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ443.getJcicZ443Id());
    if (!empNot.isEmpty())
      jcicZ443.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ443ReposDay.saveAndFlush(jcicZ443);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ443ReposMon.saveAndFlush(jcicZ443);
    else if (dbName.equals(ContentName.onHist))
        jcicZ443ReposHist.saveAndFlush(jcicZ443);
    else 
      jcicZ443Repos.saveAndFlush(jcicZ443);	
    return this.findById(jcicZ443.getJcicZ443Id());
  }

  @Override
  public void delete(JcicZ443 jcicZ443, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + jcicZ443.getJcicZ443Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ443ReposDay.delete(jcicZ443);	
      jcicZ443ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ443ReposMon.delete(jcicZ443);	
      jcicZ443ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ443ReposHist.delete(jcicZ443);
      jcicZ443ReposHist.flush();
    }
    else {
      jcicZ443Repos.delete(jcicZ443);
      jcicZ443Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ443> jcicZ443, TitaVo... titaVo) throws DBException {
    if (jcicZ443 == null || jcicZ443.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (JcicZ443 t : jcicZ443){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ443 = jcicZ443ReposDay.saveAll(jcicZ443);	
      jcicZ443ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ443 = jcicZ443ReposMon.saveAll(jcicZ443);	
      jcicZ443ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ443 = jcicZ443ReposHist.saveAll(jcicZ443);
      jcicZ443ReposHist.flush();
    }
    else {
      jcicZ443 = jcicZ443Repos.saveAll(jcicZ443);
      jcicZ443Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ443> jcicZ443, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (jcicZ443 == null || jcicZ443.size() == 0)
      throw new DBException(6);

    for (JcicZ443 t : jcicZ443) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ443 = jcicZ443ReposDay.saveAll(jcicZ443);	
      jcicZ443ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ443 = jcicZ443ReposMon.saveAll(jcicZ443);	
      jcicZ443ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ443 = jcicZ443ReposHist.saveAll(jcicZ443);
      jcicZ443ReposHist.flush();
    }
    else {
      jcicZ443 = jcicZ443Repos.saveAll(jcicZ443);
      jcicZ443Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ443> jcicZ443, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ443 == null || jcicZ443.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ443ReposDay.deleteAll(jcicZ443);	
      jcicZ443ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ443ReposMon.deleteAll(jcicZ443);	
      jcicZ443ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ443ReposHist.deleteAll(jcicZ443);
      jcicZ443ReposHist.flush();
    }
    else {
      jcicZ443Repos.deleteAll(jcicZ443);
      jcicZ443Repos.flush();
    }
  }

}
