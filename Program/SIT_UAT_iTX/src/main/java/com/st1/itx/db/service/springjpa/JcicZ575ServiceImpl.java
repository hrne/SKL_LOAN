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
import com.st1.itx.db.domain.JcicZ575;
import com.st1.itx.db.domain.JcicZ575Id;
import com.st1.itx.db.repository.online.JcicZ575Repository;
import com.st1.itx.db.repository.day.JcicZ575RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ575RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ575RepositoryHist;
import com.st1.itx.db.service.JcicZ575Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ575Service")
@Repository
public class JcicZ575ServiceImpl implements JcicZ575Service, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(JcicZ575ServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ575Repository jcicZ575Repos;

  @Autowired
  private JcicZ575RepositoryDay jcicZ575ReposDay;

  @Autowired
  private JcicZ575RepositoryMon jcicZ575ReposMon;

  @Autowired
  private JcicZ575RepositoryHist jcicZ575ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ575Repos);
    org.junit.Assert.assertNotNull(jcicZ575ReposDay);
    org.junit.Assert.assertNotNull(jcicZ575ReposMon);
    org.junit.Assert.assertNotNull(jcicZ575ReposHist);
  }

  @Override
  public JcicZ575 findById(JcicZ575Id jcicZ575Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + jcicZ575Id);
    Optional<JcicZ575> jcicZ575 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ575 = jcicZ575ReposDay.findById(jcicZ575Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ575 = jcicZ575ReposMon.findById(jcicZ575Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ575 = jcicZ575ReposHist.findById(jcicZ575Id);
    else 
      jcicZ575 = jcicZ575Repos.findById(jcicZ575Id);
    JcicZ575 obj = jcicZ575.isPresent() ? jcicZ575.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ575> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ575> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "BankId"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ575ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ575ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ575ReposHist.findAll(pageable);
    else 
      slice = jcicZ575Repos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ575> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ575> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ575ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ575ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ575ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else 
      slice = jcicZ575Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ575> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ575> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("RcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ575ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ575ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ575ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else 
      slice = jcicZ575Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ575> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ575> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ575ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ575ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ575ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else 
      slice = jcicZ575Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ575 holdById(JcicZ575Id jcicZ575Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ575Id);
    Optional<JcicZ575> jcicZ575 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ575 = jcicZ575ReposDay.findByJcicZ575Id(jcicZ575Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ575 = jcicZ575ReposMon.findByJcicZ575Id(jcicZ575Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ575 = jcicZ575ReposHist.findByJcicZ575Id(jcicZ575Id);
    else 
      jcicZ575 = jcicZ575Repos.findByJcicZ575Id(jcicZ575Id);
    return jcicZ575.isPresent() ? jcicZ575.get() : null;
  }

  @Override
  public JcicZ575 holdById(JcicZ575 jcicZ575, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ575.getJcicZ575Id());
    Optional<JcicZ575> jcicZ575T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ575T = jcicZ575ReposDay.findByJcicZ575Id(jcicZ575.getJcicZ575Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ575T = jcicZ575ReposMon.findByJcicZ575Id(jcicZ575.getJcicZ575Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ575T = jcicZ575ReposHist.findByJcicZ575Id(jcicZ575.getJcicZ575Id());
    else 
      jcicZ575T = jcicZ575Repos.findByJcicZ575Id(jcicZ575.getJcicZ575Id());
    return jcicZ575T.isPresent() ? jcicZ575T.get() : null;
  }

  @Override
  public JcicZ575 insert(JcicZ575 jcicZ575, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + jcicZ575.getJcicZ575Id());
    if (this.findById(jcicZ575.getJcicZ575Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ575.setCreateEmpNo(empNot);

    if(jcicZ575.getLastUpdateEmpNo() == null || jcicZ575.getLastUpdateEmpNo().isEmpty())
      jcicZ575.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ575ReposDay.saveAndFlush(jcicZ575);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ575ReposMon.saveAndFlush(jcicZ575);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ575ReposHist.saveAndFlush(jcicZ575);
    else 
    return jcicZ575Repos.saveAndFlush(jcicZ575);
  }

  @Override
  public JcicZ575 update(JcicZ575 jcicZ575, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ575.getJcicZ575Id());
    if (!empNot.isEmpty())
      jcicZ575.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ575ReposDay.saveAndFlush(jcicZ575);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ575ReposMon.saveAndFlush(jcicZ575);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ575ReposHist.saveAndFlush(jcicZ575);
    else 
    return jcicZ575Repos.saveAndFlush(jcicZ575);
  }

  @Override
  public JcicZ575 update2(JcicZ575 jcicZ575, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ575.getJcicZ575Id());
    if (!empNot.isEmpty())
      jcicZ575.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ575ReposDay.saveAndFlush(jcicZ575);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ575ReposMon.saveAndFlush(jcicZ575);
    else if (dbName.equals(ContentName.onHist))
        jcicZ575ReposHist.saveAndFlush(jcicZ575);
    else 
      jcicZ575Repos.saveAndFlush(jcicZ575);	
    return this.findById(jcicZ575.getJcicZ575Id());
  }

  @Override
  public void delete(JcicZ575 jcicZ575, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + jcicZ575.getJcicZ575Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ575ReposDay.delete(jcicZ575);	
      jcicZ575ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ575ReposMon.delete(jcicZ575);	
      jcicZ575ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ575ReposHist.delete(jcicZ575);
      jcicZ575ReposHist.flush();
    }
    else {
      jcicZ575Repos.delete(jcicZ575);
      jcicZ575Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ575> jcicZ575, TitaVo... titaVo) throws DBException {
    if (jcicZ575 == null || jcicZ575.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (JcicZ575 t : jcicZ575){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ575 = jcicZ575ReposDay.saveAll(jcicZ575);	
      jcicZ575ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ575 = jcicZ575ReposMon.saveAll(jcicZ575);	
      jcicZ575ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ575 = jcicZ575ReposHist.saveAll(jcicZ575);
      jcicZ575ReposHist.flush();
    }
    else {
      jcicZ575 = jcicZ575Repos.saveAll(jcicZ575);
      jcicZ575Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ575> jcicZ575, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (jcicZ575 == null || jcicZ575.size() == 0)
      throw new DBException(6);

    for (JcicZ575 t : jcicZ575) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ575 = jcicZ575ReposDay.saveAll(jcicZ575);	
      jcicZ575ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ575 = jcicZ575ReposMon.saveAll(jcicZ575);	
      jcicZ575ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ575 = jcicZ575ReposHist.saveAll(jcicZ575);
      jcicZ575ReposHist.flush();
    }
    else {
      jcicZ575 = jcicZ575Repos.saveAll(jcicZ575);
      jcicZ575Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ575> jcicZ575, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ575 == null || jcicZ575.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ575ReposDay.deleteAll(jcicZ575);	
      jcicZ575ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ575ReposMon.deleteAll(jcicZ575);	
      jcicZ575ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ575ReposHist.deleteAll(jcicZ575);
      jcicZ575ReposHist.flush();
    }
    else {
      jcicZ575Repos.deleteAll(jcicZ575);
      jcicZ575Repos.flush();
    }
  }

}
