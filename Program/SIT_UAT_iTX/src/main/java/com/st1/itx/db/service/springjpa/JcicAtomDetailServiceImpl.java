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
import com.st1.itx.db.domain.JcicAtomDetail;
import com.st1.itx.db.domain.JcicAtomDetailId;
import com.st1.itx.db.repository.online.JcicAtomDetailRepository;
import com.st1.itx.db.repository.day.JcicAtomDetailRepositoryDay;
import com.st1.itx.db.repository.mon.JcicAtomDetailRepositoryMon;
import com.st1.itx.db.repository.hist.JcicAtomDetailRepositoryHist;
import com.st1.itx.db.service.JcicAtomDetailService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicAtomDetailService")
@Repository
public class JcicAtomDetailServiceImpl implements JcicAtomDetailService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(JcicAtomDetailServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicAtomDetailRepository jcicAtomDetailRepos;

  @Autowired
  private JcicAtomDetailRepositoryDay jcicAtomDetailReposDay;

  @Autowired
  private JcicAtomDetailRepositoryMon jcicAtomDetailReposMon;

  @Autowired
  private JcicAtomDetailRepositoryHist jcicAtomDetailReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicAtomDetailRepos);
    org.junit.Assert.assertNotNull(jcicAtomDetailReposDay);
    org.junit.Assert.assertNotNull(jcicAtomDetailReposMon);
    org.junit.Assert.assertNotNull(jcicAtomDetailReposHist);
  }

  @Override
  public JcicAtomDetail findById(JcicAtomDetailId jcicAtomDetailId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + jcicAtomDetailId);
    Optional<JcicAtomDetail> jcicAtomDetail = null;
    if (dbName.equals(ContentName.onDay))
      jcicAtomDetail = jcicAtomDetailReposDay.findById(jcicAtomDetailId);
    else if (dbName.equals(ContentName.onMon))
      jcicAtomDetail = jcicAtomDetailReposMon.findById(jcicAtomDetailId);
    else if (dbName.equals(ContentName.onHist))
      jcicAtomDetail = jcicAtomDetailReposHist.findById(jcicAtomDetailId);
    else 
      jcicAtomDetail = jcicAtomDetailRepos.findById(jcicAtomDetailId);
    JcicAtomDetail obj = jcicAtomDetail.isPresent() ? jcicAtomDetail.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicAtomDetail> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicAtomDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "FunctionCode", "DataOrder"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicAtomDetailReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicAtomDetailReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicAtomDetailReposHist.findAll(pageable);
    else 
      slice = jcicAtomDetailRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicAtomDetail> findByFunctionCode(String functionCode_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicAtomDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findByFunctionCode " + dbName + " : " + "functionCode_0 : " + functionCode_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicAtomDetailReposDay.findAllByFunctionCodeIsOrderByFunctionCodeAscDataOrderAsc(functionCode_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicAtomDetailReposMon.findAllByFunctionCodeIsOrderByFunctionCodeAscDataOrderAsc(functionCode_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicAtomDetailReposHist.findAllByFunctionCodeIsOrderByFunctionCodeAscDataOrderAsc(functionCode_0, pageable);
    else 
      slice = jcicAtomDetailRepos.findAllByFunctionCodeIsOrderByFunctionCodeAscDataOrderAsc(functionCode_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicAtomDetail holdById(JcicAtomDetailId jcicAtomDetailId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicAtomDetailId);
    Optional<JcicAtomDetail> jcicAtomDetail = null;
    if (dbName.equals(ContentName.onDay))
      jcicAtomDetail = jcicAtomDetailReposDay.findByJcicAtomDetailId(jcicAtomDetailId);
    else if (dbName.equals(ContentName.onMon))
      jcicAtomDetail = jcicAtomDetailReposMon.findByJcicAtomDetailId(jcicAtomDetailId);
    else if (dbName.equals(ContentName.onHist))
      jcicAtomDetail = jcicAtomDetailReposHist.findByJcicAtomDetailId(jcicAtomDetailId);
    else 
      jcicAtomDetail = jcicAtomDetailRepos.findByJcicAtomDetailId(jcicAtomDetailId);
    return jcicAtomDetail.isPresent() ? jcicAtomDetail.get() : null;
  }

  @Override
  public JcicAtomDetail holdById(JcicAtomDetail jcicAtomDetail, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicAtomDetail.getJcicAtomDetailId());
    Optional<JcicAtomDetail> jcicAtomDetailT = null;
    if (dbName.equals(ContentName.onDay))
      jcicAtomDetailT = jcicAtomDetailReposDay.findByJcicAtomDetailId(jcicAtomDetail.getJcicAtomDetailId());
    else if (dbName.equals(ContentName.onMon))
      jcicAtomDetailT = jcicAtomDetailReposMon.findByJcicAtomDetailId(jcicAtomDetail.getJcicAtomDetailId());
    else if (dbName.equals(ContentName.onHist))
      jcicAtomDetailT = jcicAtomDetailReposHist.findByJcicAtomDetailId(jcicAtomDetail.getJcicAtomDetailId());
    else 
      jcicAtomDetailT = jcicAtomDetailRepos.findByJcicAtomDetailId(jcicAtomDetail.getJcicAtomDetailId());
    return jcicAtomDetailT.isPresent() ? jcicAtomDetailT.get() : null;
  }

  @Override
  public JcicAtomDetail insert(JcicAtomDetail jcicAtomDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Insert..." + dbName + " " + jcicAtomDetail.getJcicAtomDetailId());
    if (this.findById(jcicAtomDetail.getJcicAtomDetailId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicAtomDetail.setCreateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicAtomDetailReposDay.saveAndFlush(jcicAtomDetail);	
    else if (dbName.equals(ContentName.onMon))
      return jcicAtomDetailReposMon.saveAndFlush(jcicAtomDetail);
    else if (dbName.equals(ContentName.onHist))
      return jcicAtomDetailReposHist.saveAndFlush(jcicAtomDetail);
    else 
    return jcicAtomDetailRepos.saveAndFlush(jcicAtomDetail);
  }

  @Override
  public JcicAtomDetail update(JcicAtomDetail jcicAtomDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicAtomDetail.getJcicAtomDetailId());
    if (!empNot.isEmpty())
      jcicAtomDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicAtomDetailReposDay.saveAndFlush(jcicAtomDetail);	
    else if (dbName.equals(ContentName.onMon))
      return jcicAtomDetailReposMon.saveAndFlush(jcicAtomDetail);
    else if (dbName.equals(ContentName.onHist))
      return jcicAtomDetailReposHist.saveAndFlush(jcicAtomDetail);
    else 
    return jcicAtomDetailRepos.saveAndFlush(jcicAtomDetail);
  }

  @Override
  public JcicAtomDetail update2(JcicAtomDetail jcicAtomDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicAtomDetail.getJcicAtomDetailId());
    if (!empNot.isEmpty())
      jcicAtomDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicAtomDetailReposDay.saveAndFlush(jcicAtomDetail);	
    else if (dbName.equals(ContentName.onMon))
      jcicAtomDetailReposMon.saveAndFlush(jcicAtomDetail);
    else if (dbName.equals(ContentName.onHist))
        jcicAtomDetailReposHist.saveAndFlush(jcicAtomDetail);
    else 
      jcicAtomDetailRepos.saveAndFlush(jcicAtomDetail);	
    return this.findById(jcicAtomDetail.getJcicAtomDetailId());
  }

  @Override
  public void delete(JcicAtomDetail jcicAtomDetail, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + jcicAtomDetail.getJcicAtomDetailId());
    if (dbName.equals(ContentName.onDay)) {
      jcicAtomDetailReposDay.delete(jcicAtomDetail);	
      jcicAtomDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicAtomDetailReposMon.delete(jcicAtomDetail);	
      jcicAtomDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicAtomDetailReposHist.delete(jcicAtomDetail);
      jcicAtomDetailReposHist.flush();
    }
    else {
      jcicAtomDetailRepos.delete(jcicAtomDetail);
      jcicAtomDetailRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicAtomDetail> jcicAtomDetail, TitaVo... titaVo) throws DBException {
    if (jcicAtomDetail == null || jcicAtomDetail.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}    logger.info("InsertAll...");
    for (JcicAtomDetail t : jcicAtomDetail) 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicAtomDetail = jcicAtomDetailReposDay.saveAll(jcicAtomDetail);	
      jcicAtomDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicAtomDetail = jcicAtomDetailReposMon.saveAll(jcicAtomDetail);	
      jcicAtomDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicAtomDetail = jcicAtomDetailReposHist.saveAll(jcicAtomDetail);
      jcicAtomDetailReposHist.flush();
    }
    else {
      jcicAtomDetail = jcicAtomDetailRepos.saveAll(jcicAtomDetail);
      jcicAtomDetailRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicAtomDetail> jcicAtomDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (jcicAtomDetail == null || jcicAtomDetail.size() == 0)
      throw new DBException(6);

    for (JcicAtomDetail t : jcicAtomDetail) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicAtomDetail = jcicAtomDetailReposDay.saveAll(jcicAtomDetail);	
      jcicAtomDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicAtomDetail = jcicAtomDetailReposMon.saveAll(jcicAtomDetail);	
      jcicAtomDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicAtomDetail = jcicAtomDetailReposHist.saveAll(jcicAtomDetail);
      jcicAtomDetailReposHist.flush();
    }
    else {
      jcicAtomDetail = jcicAtomDetailRepos.saveAll(jcicAtomDetail);
      jcicAtomDetailRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicAtomDetail> jcicAtomDetail, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicAtomDetail == null || jcicAtomDetail.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicAtomDetailReposDay.deleteAll(jcicAtomDetail);	
      jcicAtomDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicAtomDetailReposMon.deleteAll(jcicAtomDetail);	
      jcicAtomDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicAtomDetailReposHist.deleteAll(jcicAtomDetail);
      jcicAtomDetailReposHist.flush();
    }
    else {
      jcicAtomDetailRepos.deleteAll(jcicAtomDetail);
      jcicAtomDetailRepos.flush();
    }
  }

}
