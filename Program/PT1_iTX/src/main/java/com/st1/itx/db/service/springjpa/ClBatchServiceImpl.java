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
import com.st1.itx.db.domain.ClBatch;
import com.st1.itx.db.domain.ClBatchId;
import com.st1.itx.db.repository.online.ClBatchRepository;
import com.st1.itx.db.repository.day.ClBatchRepositoryDay;
import com.st1.itx.db.repository.mon.ClBatchRepositoryMon;
import com.st1.itx.db.repository.hist.ClBatchRepositoryHist;
import com.st1.itx.db.service.ClBatchService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clBatchService")
@Repository
public class ClBatchServiceImpl extends ASpringJpaParm implements ClBatchService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private ClBatchRepository clBatchRepos;

  @Autowired
  private ClBatchRepositoryDay clBatchReposDay;

  @Autowired
  private ClBatchRepositoryMon clBatchReposMon;

  @Autowired
  private ClBatchRepositoryHist clBatchReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(clBatchRepos);
    org.junit.Assert.assertNotNull(clBatchReposDay);
    org.junit.Assert.assertNotNull(clBatchReposMon);
    org.junit.Assert.assertNotNull(clBatchReposHist);
  }

  @Override
  public ClBatch findById(ClBatchId clBatchId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + clBatchId);
    Optional<ClBatch> clBatch = null;
    if (dbName.equals(ContentName.onDay))
      clBatch = clBatchReposDay.findById(clBatchId);
    else if (dbName.equals(ContentName.onMon))
      clBatch = clBatchReposMon.findById(clBatchId);
    else if (dbName.equals(ContentName.onHist))
      clBatch = clBatchReposHist.findById(clBatchId);
    else 
      clBatch = clBatchRepos.findById(clBatchId);
    ClBatch obj = clBatch.isPresent() ? clBatch.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<ClBatch> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClBatch> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "GroupNo", "Seq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "GroupNo", "Seq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = clBatchReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clBatchReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clBatchReposHist.findAll(pageable);
    else 
      slice = clBatchRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ClBatch> findGroupNo(String groupNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ClBatch> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findGroupNo " + dbName + " : " + "groupNo_0 : " + groupNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = clBatchReposDay.findAllByGroupNoIsOrderBySeqAsc(groupNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = clBatchReposMon.findAllByGroupNoIsOrderBySeqAsc(groupNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = clBatchReposHist.findAllByGroupNoIsOrderBySeqAsc(groupNo_0, pageable);
    else 
      slice = clBatchRepos.findAllByGroupNoIsOrderBySeqAsc(groupNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ClBatch holdById(ClBatchId clBatchId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clBatchId);
    Optional<ClBatch> clBatch = null;
    if (dbName.equals(ContentName.onDay))
      clBatch = clBatchReposDay.findByClBatchId(clBatchId);
    else if (dbName.equals(ContentName.onMon))
      clBatch = clBatchReposMon.findByClBatchId(clBatchId);
    else if (dbName.equals(ContentName.onHist))
      clBatch = clBatchReposHist.findByClBatchId(clBatchId);
    else 
      clBatch = clBatchRepos.findByClBatchId(clBatchId);
    return clBatch.isPresent() ? clBatch.get() : null;
  }

  @Override
  public ClBatch holdById(ClBatch clBatch, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + clBatch.getClBatchId());
    Optional<ClBatch> clBatchT = null;
    if (dbName.equals(ContentName.onDay))
      clBatchT = clBatchReposDay.findByClBatchId(clBatch.getClBatchId());
    else if (dbName.equals(ContentName.onMon))
      clBatchT = clBatchReposMon.findByClBatchId(clBatch.getClBatchId());
    else if (dbName.equals(ContentName.onHist))
      clBatchT = clBatchReposHist.findByClBatchId(clBatch.getClBatchId());
    else 
      clBatchT = clBatchRepos.findByClBatchId(clBatch.getClBatchId());
    return clBatchT.isPresent() ? clBatchT.get() : null;
  }

  @Override
  public ClBatch insert(ClBatch clBatch, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + clBatch.getClBatchId());
    if (this.findById(clBatch.getClBatchId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      clBatch.setCreateEmpNo(empNot);

    if(clBatch.getLastUpdateEmpNo() == null || clBatch.getLastUpdateEmpNo().isEmpty())
      clBatch.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clBatchReposDay.saveAndFlush(clBatch);	
    else if (dbName.equals(ContentName.onMon))
      return clBatchReposMon.saveAndFlush(clBatch);
    else if (dbName.equals(ContentName.onHist))
      return clBatchReposHist.saveAndFlush(clBatch);
    else 
    return clBatchRepos.saveAndFlush(clBatch);
  }

  @Override
  public ClBatch update(ClBatch clBatch, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + clBatch.getClBatchId());
    if (!empNot.isEmpty())
      clBatch.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return clBatchReposDay.saveAndFlush(clBatch);	
    else if (dbName.equals(ContentName.onMon))
      return clBatchReposMon.saveAndFlush(clBatch);
    else if (dbName.equals(ContentName.onHist))
      return clBatchReposHist.saveAndFlush(clBatch);
    else 
    return clBatchRepos.saveAndFlush(clBatch);
  }

  @Override
  public ClBatch update2(ClBatch clBatch, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + clBatch.getClBatchId());
    if (!empNot.isEmpty())
      clBatch.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      clBatchReposDay.saveAndFlush(clBatch);	
    else if (dbName.equals(ContentName.onMon))
      clBatchReposMon.saveAndFlush(clBatch);
    else if (dbName.equals(ContentName.onHist))
        clBatchReposHist.saveAndFlush(clBatch);
    else 
      clBatchRepos.saveAndFlush(clBatch);	
    return this.findById(clBatch.getClBatchId());
  }

  @Override
  public void delete(ClBatch clBatch, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + clBatch.getClBatchId());
    if (dbName.equals(ContentName.onDay)) {
      clBatchReposDay.delete(clBatch);	
      clBatchReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clBatchReposMon.delete(clBatch);	
      clBatchReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clBatchReposHist.delete(clBatch);
      clBatchReposHist.flush();
    }
    else {
      clBatchRepos.delete(clBatch);
      clBatchRepos.flush();
    }
   }

  @Override
  public void insertAll(List<ClBatch> clBatch, TitaVo... titaVo) throws DBException {
    if (clBatch == null || clBatch.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (ClBatch t : clBatch){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      clBatch = clBatchReposDay.saveAll(clBatch);	
      clBatchReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clBatch = clBatchReposMon.saveAll(clBatch);	
      clBatchReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clBatch = clBatchReposHist.saveAll(clBatch);
      clBatchReposHist.flush();
    }
    else {
      clBatch = clBatchRepos.saveAll(clBatch);
      clBatchRepos.flush();
    }
    }

  @Override
  public void updateAll(List<ClBatch> clBatch, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (clBatch == null || clBatch.size() == 0)
      throw new DBException(6);

    for (ClBatch t : clBatch) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      clBatch = clBatchReposDay.saveAll(clBatch);	
      clBatchReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clBatch = clBatchReposMon.saveAll(clBatch);	
      clBatchReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clBatch = clBatchReposHist.saveAll(clBatch);
      clBatchReposHist.flush();
    }
    else {
      clBatch = clBatchRepos.saveAll(clBatch);
      clBatchRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<ClBatch> clBatch, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (clBatch == null || clBatch.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      clBatchReposDay.deleteAll(clBatch);	
      clBatchReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      clBatchReposMon.deleteAll(clBatch);	
      clBatchReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      clBatchReposHist.deleteAll(clBatch);
      clBatchReposHist.flush();
    }
    else {
      clBatchRepos.deleteAll(clBatch);
      clBatchRepos.flush();
    }
  }

}
