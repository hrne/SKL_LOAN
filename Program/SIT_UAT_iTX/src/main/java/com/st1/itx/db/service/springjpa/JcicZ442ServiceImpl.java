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
import com.st1.itx.db.domain.JcicZ442;
import com.st1.itx.db.domain.JcicZ442Id;
import com.st1.itx.db.repository.online.JcicZ442Repository;
import com.st1.itx.db.repository.day.JcicZ442RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ442RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ442RepositoryHist;
import com.st1.itx.db.service.JcicZ442Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ442Service")
@Repository
public class JcicZ442ServiceImpl implements JcicZ442Service, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(JcicZ442ServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ442Repository jcicZ442Repos;

  @Autowired
  private JcicZ442RepositoryDay jcicZ442ReposDay;

  @Autowired
  private JcicZ442RepositoryMon jcicZ442ReposMon;

  @Autowired
  private JcicZ442RepositoryHist jcicZ442ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ442Repos);
    org.junit.Assert.assertNotNull(jcicZ442ReposDay);
    org.junit.Assert.assertNotNull(jcicZ442ReposMon);
    org.junit.Assert.assertNotNull(jcicZ442ReposHist);
  }

  @Override
  public JcicZ442 findById(JcicZ442Id jcicZ442Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + jcicZ442Id);
    Optional<JcicZ442> jcicZ442 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ442 = jcicZ442ReposDay.findById(jcicZ442Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ442 = jcicZ442ReposMon.findById(jcicZ442Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ442 = jcicZ442ReposHist.findById(jcicZ442Id);
    else 
      jcicZ442 = jcicZ442Repos.findById(jcicZ442Id);
    JcicZ442 obj = jcicZ442.isPresent() ? jcicZ442.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ442> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ442> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "BankId", "MaxMainCode"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ442ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ442ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ442ReposHist.findAll(pageable);
    else 
      slice = jcicZ442Repos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ442> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ442> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ442ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ442ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ442ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else 
      slice = jcicZ442Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ442> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ442> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("RcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ442ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ442ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ442ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else 
      slice = jcicZ442Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ442> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ442> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ442ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ442ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ442ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else 
      slice = jcicZ442Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ442 holdById(JcicZ442Id jcicZ442Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ442Id);
    Optional<JcicZ442> jcicZ442 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ442 = jcicZ442ReposDay.findByJcicZ442Id(jcicZ442Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ442 = jcicZ442ReposMon.findByJcicZ442Id(jcicZ442Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ442 = jcicZ442ReposHist.findByJcicZ442Id(jcicZ442Id);
    else 
      jcicZ442 = jcicZ442Repos.findByJcicZ442Id(jcicZ442Id);
    return jcicZ442.isPresent() ? jcicZ442.get() : null;
  }

  @Override
  public JcicZ442 holdById(JcicZ442 jcicZ442, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ442.getJcicZ442Id());
    Optional<JcicZ442> jcicZ442T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ442T = jcicZ442ReposDay.findByJcicZ442Id(jcicZ442.getJcicZ442Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ442T = jcicZ442ReposMon.findByJcicZ442Id(jcicZ442.getJcicZ442Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ442T = jcicZ442ReposHist.findByJcicZ442Id(jcicZ442.getJcicZ442Id());
    else 
      jcicZ442T = jcicZ442Repos.findByJcicZ442Id(jcicZ442.getJcicZ442Id());
    return jcicZ442T.isPresent() ? jcicZ442T.get() : null;
  }

  @Override
  public JcicZ442 insert(JcicZ442 jcicZ442, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + jcicZ442.getJcicZ442Id());
    if (this.findById(jcicZ442.getJcicZ442Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ442.setCreateEmpNo(empNot);

    if(jcicZ442.getLastUpdateEmpNo() == null || jcicZ442.getLastUpdateEmpNo().isEmpty())
      jcicZ442.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ442ReposDay.saveAndFlush(jcicZ442);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ442ReposMon.saveAndFlush(jcicZ442);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ442ReposHist.saveAndFlush(jcicZ442);
    else 
    return jcicZ442Repos.saveAndFlush(jcicZ442);
  }

  @Override
  public JcicZ442 update(JcicZ442 jcicZ442, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ442.getJcicZ442Id());
    if (!empNot.isEmpty())
      jcicZ442.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ442ReposDay.saveAndFlush(jcicZ442);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ442ReposMon.saveAndFlush(jcicZ442);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ442ReposHist.saveAndFlush(jcicZ442);
    else 
    return jcicZ442Repos.saveAndFlush(jcicZ442);
  }

  @Override
  public JcicZ442 update2(JcicZ442 jcicZ442, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ442.getJcicZ442Id());
    if (!empNot.isEmpty())
      jcicZ442.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ442ReposDay.saveAndFlush(jcicZ442);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ442ReposMon.saveAndFlush(jcicZ442);
    else if (dbName.equals(ContentName.onHist))
        jcicZ442ReposHist.saveAndFlush(jcicZ442);
    else 
      jcicZ442Repos.saveAndFlush(jcicZ442);	
    return this.findById(jcicZ442.getJcicZ442Id());
  }

  @Override
  public void delete(JcicZ442 jcicZ442, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + jcicZ442.getJcicZ442Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ442ReposDay.delete(jcicZ442);	
      jcicZ442ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ442ReposMon.delete(jcicZ442);	
      jcicZ442ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ442ReposHist.delete(jcicZ442);
      jcicZ442ReposHist.flush();
    }
    else {
      jcicZ442Repos.delete(jcicZ442);
      jcicZ442Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ442> jcicZ442, TitaVo... titaVo) throws DBException {
    if (jcicZ442 == null || jcicZ442.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (JcicZ442 t : jcicZ442){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ442 = jcicZ442ReposDay.saveAll(jcicZ442);	
      jcicZ442ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ442 = jcicZ442ReposMon.saveAll(jcicZ442);	
      jcicZ442ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ442 = jcicZ442ReposHist.saveAll(jcicZ442);
      jcicZ442ReposHist.flush();
    }
    else {
      jcicZ442 = jcicZ442Repos.saveAll(jcicZ442);
      jcicZ442Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ442> jcicZ442, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (jcicZ442 == null || jcicZ442.size() == 0)
      throw new DBException(6);

    for (JcicZ442 t : jcicZ442) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ442 = jcicZ442ReposDay.saveAll(jcicZ442);	
      jcicZ442ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ442 = jcicZ442ReposMon.saveAll(jcicZ442);	
      jcicZ442ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ442 = jcicZ442ReposHist.saveAll(jcicZ442);
      jcicZ442ReposHist.flush();
    }
    else {
      jcicZ442 = jcicZ442Repos.saveAll(jcicZ442);
      jcicZ442Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ442> jcicZ442, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ442 == null || jcicZ442.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ442ReposDay.deleteAll(jcicZ442);	
      jcicZ442ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ442ReposMon.deleteAll(jcicZ442);	
      jcicZ442ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ442ReposHist.deleteAll(jcicZ442);
      jcicZ442ReposHist.flush();
    }
    else {
      jcicZ442Repos.deleteAll(jcicZ442);
      jcicZ442Repos.flush();
    }
  }

}
