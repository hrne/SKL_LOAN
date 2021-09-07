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
import com.st1.itx.db.domain.ClImmRankDetail;
import com.st1.itx.db.domain.ClImmRankDetailId;
import com.st1.itx.db.repository.online.ClImmRankDetailRepository;
import com.st1.itx.db.repository.day.ClImmRankDetailRepositoryDay;
import com.st1.itx.db.repository.mon.ClImmRankDetailRepositoryMon;
import com.st1.itx.db.repository.hist.ClImmRankDetailRepositoryHist;
import com.st1.itx.db.service.ClImmRankDetailService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clImmRankDetailService")
@Repository
public class ClImmRankDetailServiceImpl extends ASpringJpaParm implements ClImmRankDetailService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private ClImmRankDetailRepository clImmRankDetailRepos;

  @Autowired
  private ClImmRankDetailRepositoryDay clImmRankDetailReposDay;

  @Autowired
  private ClImmRankDetailRepositoryMon clImmRankDetailReposMon;

  @Autowired
  private ClImmRankDetailRepositoryHist clImmRankDetailReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(clImmRankDetailRepos);
    org.junit.Assert.assertNotNull(clImmRankDetailReposDay);
    org.junit.Assert.assertNotNull(clImmRankDetailReposMon);
    org.junit.Assert.assertNotNull(clImmRankDetailReposHist);
  }

  @Override
  public ClImmRankDetail findById(ClImmRankDetailId clImmRankDetailId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + clImmRankDetailId);
    Optional<ClImmRankDetail> clImmRankDetail = null;
    if (dbName.equals(ContentName.onDay))
      clImmRankDetail = clImmRankDetailReposDay.findById(clImmRankDetailId);
    else if (dbName.equals(ContentName.onMon))
      clImmRankDetail = clImmRankDetailReposMon.findById(clImmRankDetailId);
    else if (dbName.equals(ContentName.onHist))
      clImmRankDetail = clImmRankDetailReposHist.findById(clImmRankDetailId);
    else 
      clImmRankDetail = clImmRankDetailRepos.findById(clImmRankDetailId);
    ClImmRankDetail obj = clImmRankDetail.isPresent() ? clImmRankDetail.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<ClImmRankDetail> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClImmRankDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "SettingSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "SettingSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = clImmRankDetailReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clImmRankDetailReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clImmRankDetailReposHist.findAll(pageable);
    else 
      slice = clImmRankDetailRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ClImmRankDetail holdById(ClImmRankDetailId clImmRankDetailId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clImmRankDetailId);
    Optional<ClImmRankDetail> clImmRankDetail = null;
    if (dbName.equals(ContentName.onDay))
      clImmRankDetail = clImmRankDetailReposDay.findByClImmRankDetailId(clImmRankDetailId);
    else if (dbName.equals(ContentName.onMon))
      clImmRankDetail = clImmRankDetailReposMon.findByClImmRankDetailId(clImmRankDetailId);
    else if (dbName.equals(ContentName.onHist))
      clImmRankDetail = clImmRankDetailReposHist.findByClImmRankDetailId(clImmRankDetailId);
    else 
      clImmRankDetail = clImmRankDetailRepos.findByClImmRankDetailId(clImmRankDetailId);
    return clImmRankDetail.isPresent() ? clImmRankDetail.get() : null;
  }

  @Override
  public ClImmRankDetail holdById(ClImmRankDetail clImmRankDetail, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clImmRankDetail.getClImmRankDetailId());
    Optional<ClImmRankDetail> clImmRankDetailT = null;
    if (dbName.equals(ContentName.onDay))
      clImmRankDetailT = clImmRankDetailReposDay.findByClImmRankDetailId(clImmRankDetail.getClImmRankDetailId());
    else if (dbName.equals(ContentName.onMon))
      clImmRankDetailT = clImmRankDetailReposMon.findByClImmRankDetailId(clImmRankDetail.getClImmRankDetailId());
    else if (dbName.equals(ContentName.onHist))
      clImmRankDetailT = clImmRankDetailReposHist.findByClImmRankDetailId(clImmRankDetail.getClImmRankDetailId());
    else 
      clImmRankDetailT = clImmRankDetailRepos.findByClImmRankDetailId(clImmRankDetail.getClImmRankDetailId());
    return clImmRankDetailT.isPresent() ? clImmRankDetailT.get() : null;
  }

  @Override
  public ClImmRankDetail insert(ClImmRankDetail clImmRankDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + clImmRankDetail.getClImmRankDetailId());
    if (this.findById(clImmRankDetail.getClImmRankDetailId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      clImmRankDetail.setCreateEmpNo(empNot);

    if(clImmRankDetail.getLastUpdateEmpNo() == null || clImmRankDetail.getLastUpdateEmpNo().isEmpty())
      clImmRankDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clImmRankDetailReposDay.saveAndFlush(clImmRankDetail);	
    else if (dbName.equals(ContentName.onMon))
      return clImmRankDetailReposMon.saveAndFlush(clImmRankDetail);
    else if (dbName.equals(ContentName.onHist))
      return clImmRankDetailReposHist.saveAndFlush(clImmRankDetail);
    else 
    return clImmRankDetailRepos.saveAndFlush(clImmRankDetail);
  }

  @Override
  public ClImmRankDetail update(ClImmRankDetail clImmRankDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + clImmRankDetail.getClImmRankDetailId());
    if (!empNot.isEmpty())
      clImmRankDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clImmRankDetailReposDay.saveAndFlush(clImmRankDetail);	
    else if (dbName.equals(ContentName.onMon))
      return clImmRankDetailReposMon.saveAndFlush(clImmRankDetail);
    else if (dbName.equals(ContentName.onHist))
      return clImmRankDetailReposHist.saveAndFlush(clImmRankDetail);
    else 
    return clImmRankDetailRepos.saveAndFlush(clImmRankDetail);
  }

  @Override
  public ClImmRankDetail update2(ClImmRankDetail clImmRankDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + clImmRankDetail.getClImmRankDetailId());
    if (!empNot.isEmpty())
      clImmRankDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      clImmRankDetailReposDay.saveAndFlush(clImmRankDetail);	
    else if (dbName.equals(ContentName.onMon))
      clImmRankDetailReposMon.saveAndFlush(clImmRankDetail);
    else if (dbName.equals(ContentName.onHist))
        clImmRankDetailReposHist.saveAndFlush(clImmRankDetail);
    else 
      clImmRankDetailRepos.saveAndFlush(clImmRankDetail);	
    return this.findById(clImmRankDetail.getClImmRankDetailId());
  }

  @Override
  public void delete(ClImmRankDetail clImmRankDetail, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + clImmRankDetail.getClImmRankDetailId());
    if (dbName.equals(ContentName.onDay)) {
      clImmRankDetailReposDay.delete(clImmRankDetail);	
      clImmRankDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clImmRankDetailReposMon.delete(clImmRankDetail);	
      clImmRankDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clImmRankDetailReposHist.delete(clImmRankDetail);
      clImmRankDetailReposHist.flush();
    }
    else {
      clImmRankDetailRepos.delete(clImmRankDetail);
      clImmRankDetailRepos.flush();
    }
   }

  @Override
  public void insertAll(List<ClImmRankDetail> clImmRankDetail, TitaVo... titaVo) throws DBException {
    if (clImmRankDetail == null || clImmRankDetail.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (ClImmRankDetail t : clImmRankDetail){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      clImmRankDetail = clImmRankDetailReposDay.saveAll(clImmRankDetail);	
      clImmRankDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clImmRankDetail = clImmRankDetailReposMon.saveAll(clImmRankDetail);	
      clImmRankDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clImmRankDetail = clImmRankDetailReposHist.saveAll(clImmRankDetail);
      clImmRankDetailReposHist.flush();
    }
    else {
      clImmRankDetail = clImmRankDetailRepos.saveAll(clImmRankDetail);
      clImmRankDetailRepos.flush();
    }
    }

  @Override
  public void updateAll(List<ClImmRankDetail> clImmRankDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (clImmRankDetail == null || clImmRankDetail.size() == 0)
      throw new DBException(6);

    for (ClImmRankDetail t : clImmRankDetail) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      clImmRankDetail = clImmRankDetailReposDay.saveAll(clImmRankDetail);	
      clImmRankDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clImmRankDetail = clImmRankDetailReposMon.saveAll(clImmRankDetail);	
      clImmRankDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clImmRankDetail = clImmRankDetailReposHist.saveAll(clImmRankDetail);
      clImmRankDetailReposHist.flush();
    }
    else {
      clImmRankDetail = clImmRankDetailRepos.saveAll(clImmRankDetail);
      clImmRankDetailRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<ClImmRankDetail> clImmRankDetail, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (clImmRankDetail == null || clImmRankDetail.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      clImmRankDetailReposDay.deleteAll(clImmRankDetail);	
      clImmRankDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clImmRankDetailReposMon.deleteAll(clImmRankDetail);	
      clImmRankDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clImmRankDetailReposHist.deleteAll(clImmRankDetail);
      clImmRankDetailReposHist.flush();
    }
    else {
      clImmRankDetailRepos.deleteAll(clImmRankDetail);
      clImmRankDetailRepos.flush();
    }
  }

}
