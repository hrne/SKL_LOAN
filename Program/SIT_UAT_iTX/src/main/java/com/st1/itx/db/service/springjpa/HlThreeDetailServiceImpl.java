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
import com.st1.itx.db.domain.HlThreeDetail;
import com.st1.itx.db.domain.HlThreeDetailId;
import com.st1.itx.db.repository.online.HlThreeDetailRepository;
import com.st1.itx.db.repository.day.HlThreeDetailRepositoryDay;
import com.st1.itx.db.repository.mon.HlThreeDetailRepositoryMon;
import com.st1.itx.db.repository.hist.HlThreeDetailRepositoryHist;
import com.st1.itx.db.service.HlThreeDetailService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("hlThreeDetailService")
@Repository
public class HlThreeDetailServiceImpl extends ASpringJpaParm implements HlThreeDetailService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private HlThreeDetailRepository hlThreeDetailRepos;

  @Autowired
  private HlThreeDetailRepositoryDay hlThreeDetailReposDay;

  @Autowired
  private HlThreeDetailRepositoryMon hlThreeDetailReposMon;

  @Autowired
  private HlThreeDetailRepositoryHist hlThreeDetailReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(hlThreeDetailRepos);
    org.junit.Assert.assertNotNull(hlThreeDetailReposDay);
    org.junit.Assert.assertNotNull(hlThreeDetailReposMon);
    org.junit.Assert.assertNotNull(hlThreeDetailReposHist);
  }

  @Override
  public HlThreeDetail findById(HlThreeDetailId hlThreeDetailId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + hlThreeDetailId);
    Optional<HlThreeDetail> hlThreeDetail = null;
    if (dbName.equals(ContentName.onDay))
      hlThreeDetail = hlThreeDetailReposDay.findById(hlThreeDetailId);
    else if (dbName.equals(ContentName.onMon))
      hlThreeDetail = hlThreeDetailReposMon.findById(hlThreeDetailId);
    else if (dbName.equals(ContentName.onHist))
      hlThreeDetail = hlThreeDetailReposHist.findById(hlThreeDetailId);
    else 
      hlThreeDetail = hlThreeDetailRepos.findById(hlThreeDetailId);
    HlThreeDetail obj = hlThreeDetail.isPresent() ? hlThreeDetail.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<HlThreeDetail> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<HlThreeDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CusBNo", "HlCusNo", "AmountNo", "CaseNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CusBNo", "HlCusNo", "AmountNo", "CaseNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = hlThreeDetailReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = hlThreeDetailReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = hlThreeDetailReposHist.findAll(pageable);
    else 
      slice = hlThreeDetailRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public HlThreeDetail holdById(HlThreeDetailId hlThreeDetailId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + hlThreeDetailId);
    Optional<HlThreeDetail> hlThreeDetail = null;
    if (dbName.equals(ContentName.onDay))
      hlThreeDetail = hlThreeDetailReposDay.findByHlThreeDetailId(hlThreeDetailId);
    else if (dbName.equals(ContentName.onMon))
      hlThreeDetail = hlThreeDetailReposMon.findByHlThreeDetailId(hlThreeDetailId);
    else if (dbName.equals(ContentName.onHist))
      hlThreeDetail = hlThreeDetailReposHist.findByHlThreeDetailId(hlThreeDetailId);
    else 
      hlThreeDetail = hlThreeDetailRepos.findByHlThreeDetailId(hlThreeDetailId);
    return hlThreeDetail.isPresent() ? hlThreeDetail.get() : null;
  }

  @Override
  public HlThreeDetail holdById(HlThreeDetail hlThreeDetail, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + hlThreeDetail.getHlThreeDetailId());
    Optional<HlThreeDetail> hlThreeDetailT = null;
    if (dbName.equals(ContentName.onDay))
      hlThreeDetailT = hlThreeDetailReposDay.findByHlThreeDetailId(hlThreeDetail.getHlThreeDetailId());
    else if (dbName.equals(ContentName.onMon))
      hlThreeDetailT = hlThreeDetailReposMon.findByHlThreeDetailId(hlThreeDetail.getHlThreeDetailId());
    else if (dbName.equals(ContentName.onHist))
      hlThreeDetailT = hlThreeDetailReposHist.findByHlThreeDetailId(hlThreeDetail.getHlThreeDetailId());
    else 
      hlThreeDetailT = hlThreeDetailRepos.findByHlThreeDetailId(hlThreeDetail.getHlThreeDetailId());
    return hlThreeDetailT.isPresent() ? hlThreeDetailT.get() : null;
  }

  @Override
  public HlThreeDetail insert(HlThreeDetail hlThreeDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + hlThreeDetail.getHlThreeDetailId());
    if (this.findById(hlThreeDetail.getHlThreeDetailId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      hlThreeDetail.setCreateEmpNo(empNot);

    if(hlThreeDetail.getLastUpdateEmpNo() == null || hlThreeDetail.getLastUpdateEmpNo().isEmpty())
      hlThreeDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return hlThreeDetailReposDay.saveAndFlush(hlThreeDetail);	
    else if (dbName.equals(ContentName.onMon))
      return hlThreeDetailReposMon.saveAndFlush(hlThreeDetail);
    else if (dbName.equals(ContentName.onHist))
      return hlThreeDetailReposHist.saveAndFlush(hlThreeDetail);
    else 
    return hlThreeDetailRepos.saveAndFlush(hlThreeDetail);
  }

  @Override
  public HlThreeDetail update(HlThreeDetail hlThreeDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + hlThreeDetail.getHlThreeDetailId());
    if (!empNot.isEmpty())
      hlThreeDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return hlThreeDetailReposDay.saveAndFlush(hlThreeDetail);	
    else if (dbName.equals(ContentName.onMon))
      return hlThreeDetailReposMon.saveAndFlush(hlThreeDetail);
    else if (dbName.equals(ContentName.onHist))
      return hlThreeDetailReposHist.saveAndFlush(hlThreeDetail);
    else 
    return hlThreeDetailRepos.saveAndFlush(hlThreeDetail);
  }

  @Override
  public HlThreeDetail update2(HlThreeDetail hlThreeDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + hlThreeDetail.getHlThreeDetailId());
    if (!empNot.isEmpty())
      hlThreeDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      hlThreeDetailReposDay.saveAndFlush(hlThreeDetail);	
    else if (dbName.equals(ContentName.onMon))
      hlThreeDetailReposMon.saveAndFlush(hlThreeDetail);
    else if (dbName.equals(ContentName.onHist))
        hlThreeDetailReposHist.saveAndFlush(hlThreeDetail);
    else 
      hlThreeDetailRepos.saveAndFlush(hlThreeDetail);	
    return this.findById(hlThreeDetail.getHlThreeDetailId());
  }

  @Override
  public void delete(HlThreeDetail hlThreeDetail, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + hlThreeDetail.getHlThreeDetailId());
    if (dbName.equals(ContentName.onDay)) {
      hlThreeDetailReposDay.delete(hlThreeDetail);	
      hlThreeDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      hlThreeDetailReposMon.delete(hlThreeDetail);	
      hlThreeDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      hlThreeDetailReposHist.delete(hlThreeDetail);
      hlThreeDetailReposHist.flush();
    }
    else {
      hlThreeDetailRepos.delete(hlThreeDetail);
      hlThreeDetailRepos.flush();
    }
   }

  @Override
  public void insertAll(List<HlThreeDetail> hlThreeDetail, TitaVo... titaVo) throws DBException {
    if (hlThreeDetail == null || hlThreeDetail.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (HlThreeDetail t : hlThreeDetail){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      hlThreeDetail = hlThreeDetailReposDay.saveAll(hlThreeDetail);	
      hlThreeDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      hlThreeDetail = hlThreeDetailReposMon.saveAll(hlThreeDetail);	
      hlThreeDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      hlThreeDetail = hlThreeDetailReposHist.saveAll(hlThreeDetail);
      hlThreeDetailReposHist.flush();
    }
    else {
      hlThreeDetail = hlThreeDetailRepos.saveAll(hlThreeDetail);
      hlThreeDetailRepos.flush();
    }
    }

  @Override
  public void updateAll(List<HlThreeDetail> hlThreeDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (hlThreeDetail == null || hlThreeDetail.size() == 0)
      throw new DBException(6);

    for (HlThreeDetail t : hlThreeDetail) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      hlThreeDetail = hlThreeDetailReposDay.saveAll(hlThreeDetail);	
      hlThreeDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      hlThreeDetail = hlThreeDetailReposMon.saveAll(hlThreeDetail);	
      hlThreeDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      hlThreeDetail = hlThreeDetailReposHist.saveAll(hlThreeDetail);
      hlThreeDetailReposHist.flush();
    }
    else {
      hlThreeDetail = hlThreeDetailRepos.saveAll(hlThreeDetail);
      hlThreeDetailRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<HlThreeDetail> hlThreeDetail, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (hlThreeDetail == null || hlThreeDetail.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      hlThreeDetailReposDay.deleteAll(hlThreeDetail);	
      hlThreeDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      hlThreeDetailReposMon.deleteAll(hlThreeDetail);	
      hlThreeDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      hlThreeDetailReposHist.deleteAll(hlThreeDetail);
      hlThreeDetailReposHist.flush();
    }
    else {
      hlThreeDetailRepos.deleteAll(hlThreeDetail);
      hlThreeDetailRepos.flush();
    }
  }

}
