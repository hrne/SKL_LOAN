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
import com.st1.itx.db.domain.HlThreeLaqhcp;
import com.st1.itx.db.domain.HlThreeLaqhcpId;
import com.st1.itx.db.repository.online.HlThreeLaqhcpRepository;
import com.st1.itx.db.repository.day.HlThreeLaqhcpRepositoryDay;
import com.st1.itx.db.repository.mon.HlThreeLaqhcpRepositoryMon;
import com.st1.itx.db.repository.hist.HlThreeLaqhcpRepositoryHist;
import com.st1.itx.db.service.HlThreeLaqhcpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("hlThreeLaqhcpService")
@Repository
public class HlThreeLaqhcpServiceImpl extends ASpringJpaParm implements HlThreeLaqhcpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private HlThreeLaqhcpRepository hlThreeLaqhcpRepos;

  @Autowired
  private HlThreeLaqhcpRepositoryDay hlThreeLaqhcpReposDay;

  @Autowired
  private HlThreeLaqhcpRepositoryMon hlThreeLaqhcpReposMon;

  @Autowired
  private HlThreeLaqhcpRepositoryHist hlThreeLaqhcpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(hlThreeLaqhcpRepos);
    org.junit.Assert.assertNotNull(hlThreeLaqhcpReposDay);
    org.junit.Assert.assertNotNull(hlThreeLaqhcpReposMon);
    org.junit.Assert.assertNotNull(hlThreeLaqhcpReposHist);
  }

  @Override
  public HlThreeLaqhcp findById(HlThreeLaqhcpId hlThreeLaqhcpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + hlThreeLaqhcpId);
    Optional<HlThreeLaqhcp> hlThreeLaqhcp = null;
    if (dbName.equals(ContentName.onDay))
      hlThreeLaqhcp = hlThreeLaqhcpReposDay.findById(hlThreeLaqhcpId);
    else if (dbName.equals(ContentName.onMon))
      hlThreeLaqhcp = hlThreeLaqhcpReposMon.findById(hlThreeLaqhcpId);
    else if (dbName.equals(ContentName.onHist))
      hlThreeLaqhcp = hlThreeLaqhcpReposHist.findById(hlThreeLaqhcpId);
    else 
      hlThreeLaqhcp = hlThreeLaqhcpRepos.findById(hlThreeLaqhcpId);
    HlThreeLaqhcp obj = hlThreeLaqhcp.isPresent() ? hlThreeLaqhcp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<HlThreeLaqhcp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<HlThreeLaqhcp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DeptCode", "DistCode", "UnitCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DeptCode", "DistCode", "UnitCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = hlThreeLaqhcpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = hlThreeLaqhcpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = hlThreeLaqhcpReposHist.findAll(pageable);
    else 
      slice = hlThreeLaqhcpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public HlThreeLaqhcp holdById(HlThreeLaqhcpId hlThreeLaqhcpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + hlThreeLaqhcpId);
    Optional<HlThreeLaqhcp> hlThreeLaqhcp = null;
    if (dbName.equals(ContentName.onDay))
      hlThreeLaqhcp = hlThreeLaqhcpReposDay.findByHlThreeLaqhcpId(hlThreeLaqhcpId);
    else if (dbName.equals(ContentName.onMon))
      hlThreeLaqhcp = hlThreeLaqhcpReposMon.findByHlThreeLaqhcpId(hlThreeLaqhcpId);
    else if (dbName.equals(ContentName.onHist))
      hlThreeLaqhcp = hlThreeLaqhcpReposHist.findByHlThreeLaqhcpId(hlThreeLaqhcpId);
    else 
      hlThreeLaqhcp = hlThreeLaqhcpRepos.findByHlThreeLaqhcpId(hlThreeLaqhcpId);
    return hlThreeLaqhcp.isPresent() ? hlThreeLaqhcp.get() : null;
  }

  @Override
  public HlThreeLaqhcp holdById(HlThreeLaqhcp hlThreeLaqhcp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + hlThreeLaqhcp.getHlThreeLaqhcpId());
    Optional<HlThreeLaqhcp> hlThreeLaqhcpT = null;
    if (dbName.equals(ContentName.onDay))
      hlThreeLaqhcpT = hlThreeLaqhcpReposDay.findByHlThreeLaqhcpId(hlThreeLaqhcp.getHlThreeLaqhcpId());
    else if (dbName.equals(ContentName.onMon))
      hlThreeLaqhcpT = hlThreeLaqhcpReposMon.findByHlThreeLaqhcpId(hlThreeLaqhcp.getHlThreeLaqhcpId());
    else if (dbName.equals(ContentName.onHist))
      hlThreeLaqhcpT = hlThreeLaqhcpReposHist.findByHlThreeLaqhcpId(hlThreeLaqhcp.getHlThreeLaqhcpId());
    else 
      hlThreeLaqhcpT = hlThreeLaqhcpRepos.findByHlThreeLaqhcpId(hlThreeLaqhcp.getHlThreeLaqhcpId());
    return hlThreeLaqhcpT.isPresent() ? hlThreeLaqhcpT.get() : null;
  }

  @Override
  public HlThreeLaqhcp insert(HlThreeLaqhcp hlThreeLaqhcp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + hlThreeLaqhcp.getHlThreeLaqhcpId());
    if (this.findById(hlThreeLaqhcp.getHlThreeLaqhcpId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      hlThreeLaqhcp.setCreateEmpNo(empNot);

    if(hlThreeLaqhcp.getLastUpdateEmpNo() == null || hlThreeLaqhcp.getLastUpdateEmpNo().isEmpty())
      hlThreeLaqhcp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return hlThreeLaqhcpReposDay.saveAndFlush(hlThreeLaqhcp);	
    else if (dbName.equals(ContentName.onMon))
      return hlThreeLaqhcpReposMon.saveAndFlush(hlThreeLaqhcp);
    else if (dbName.equals(ContentName.onHist))
      return hlThreeLaqhcpReposHist.saveAndFlush(hlThreeLaqhcp);
    else 
    return hlThreeLaqhcpRepos.saveAndFlush(hlThreeLaqhcp);
  }

  @Override
  public HlThreeLaqhcp update(HlThreeLaqhcp hlThreeLaqhcp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + hlThreeLaqhcp.getHlThreeLaqhcpId());
    if (!empNot.isEmpty())
      hlThreeLaqhcp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return hlThreeLaqhcpReposDay.saveAndFlush(hlThreeLaqhcp);	
    else if (dbName.equals(ContentName.onMon))
      return hlThreeLaqhcpReposMon.saveAndFlush(hlThreeLaqhcp);
    else if (dbName.equals(ContentName.onHist))
      return hlThreeLaqhcpReposHist.saveAndFlush(hlThreeLaqhcp);
    else 
    return hlThreeLaqhcpRepos.saveAndFlush(hlThreeLaqhcp);
  }

  @Override
  public HlThreeLaqhcp update2(HlThreeLaqhcp hlThreeLaqhcp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + hlThreeLaqhcp.getHlThreeLaqhcpId());
    if (!empNot.isEmpty())
      hlThreeLaqhcp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      hlThreeLaqhcpReposDay.saveAndFlush(hlThreeLaqhcp);	
    else if (dbName.equals(ContentName.onMon))
      hlThreeLaqhcpReposMon.saveAndFlush(hlThreeLaqhcp);
    else if (dbName.equals(ContentName.onHist))
        hlThreeLaqhcpReposHist.saveAndFlush(hlThreeLaqhcp);
    else 
      hlThreeLaqhcpRepos.saveAndFlush(hlThreeLaqhcp);	
    return this.findById(hlThreeLaqhcp.getHlThreeLaqhcpId());
  }

  @Override
  public void delete(HlThreeLaqhcp hlThreeLaqhcp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + hlThreeLaqhcp.getHlThreeLaqhcpId());
    if (dbName.equals(ContentName.onDay)) {
      hlThreeLaqhcpReposDay.delete(hlThreeLaqhcp);	
      hlThreeLaqhcpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      hlThreeLaqhcpReposMon.delete(hlThreeLaqhcp);	
      hlThreeLaqhcpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      hlThreeLaqhcpReposHist.delete(hlThreeLaqhcp);
      hlThreeLaqhcpReposHist.flush();
    }
    else {
      hlThreeLaqhcpRepos.delete(hlThreeLaqhcp);
      hlThreeLaqhcpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<HlThreeLaqhcp> hlThreeLaqhcp, TitaVo... titaVo) throws DBException {
    if (hlThreeLaqhcp == null || hlThreeLaqhcp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (HlThreeLaqhcp t : hlThreeLaqhcp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      hlThreeLaqhcp = hlThreeLaqhcpReposDay.saveAll(hlThreeLaqhcp);	
      hlThreeLaqhcpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      hlThreeLaqhcp = hlThreeLaqhcpReposMon.saveAll(hlThreeLaqhcp);	
      hlThreeLaqhcpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      hlThreeLaqhcp = hlThreeLaqhcpReposHist.saveAll(hlThreeLaqhcp);
      hlThreeLaqhcpReposHist.flush();
    }
    else {
      hlThreeLaqhcp = hlThreeLaqhcpRepos.saveAll(hlThreeLaqhcp);
      hlThreeLaqhcpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<HlThreeLaqhcp> hlThreeLaqhcp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (hlThreeLaqhcp == null || hlThreeLaqhcp.size() == 0)
      throw new DBException(6);

    for (HlThreeLaqhcp t : hlThreeLaqhcp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      hlThreeLaqhcp = hlThreeLaqhcpReposDay.saveAll(hlThreeLaqhcp);	
      hlThreeLaqhcpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      hlThreeLaqhcp = hlThreeLaqhcpReposMon.saveAll(hlThreeLaqhcp);	
      hlThreeLaqhcpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      hlThreeLaqhcp = hlThreeLaqhcpReposHist.saveAll(hlThreeLaqhcp);
      hlThreeLaqhcpReposHist.flush();
    }
    else {
      hlThreeLaqhcp = hlThreeLaqhcpRepos.saveAll(hlThreeLaqhcp);
      hlThreeLaqhcpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<HlThreeLaqhcp> hlThreeLaqhcp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (hlThreeLaqhcp == null || hlThreeLaqhcp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      hlThreeLaqhcpReposDay.deleteAll(hlThreeLaqhcp);	
      hlThreeLaqhcpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      hlThreeLaqhcpReposMon.deleteAll(hlThreeLaqhcp);	
      hlThreeLaqhcpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      hlThreeLaqhcpReposHist.deleteAll(hlThreeLaqhcp);
      hlThreeLaqhcpReposHist.flush();
    }
    else {
      hlThreeLaqhcpRepos.deleteAll(hlThreeLaqhcp);
      hlThreeLaqhcpRepos.flush();
    }
  }

}
