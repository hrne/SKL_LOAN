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
import com.st1.itx.db.domain.Ifrs9FacData;
import com.st1.itx.db.domain.Ifrs9FacDataId;
import com.st1.itx.db.repository.online.Ifrs9FacDataRepository;
import com.st1.itx.db.repository.day.Ifrs9FacDataRepositoryDay;
import com.st1.itx.db.repository.mon.Ifrs9FacDataRepositoryMon;
import com.st1.itx.db.repository.hist.Ifrs9FacDataRepositoryHist;
import com.st1.itx.db.service.Ifrs9FacDataService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("ifrs9FacDataService")
@Repository
public class Ifrs9FacDataServiceImpl extends ASpringJpaParm implements Ifrs9FacDataService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private Ifrs9FacDataRepository ifrs9FacDataRepos;

  @Autowired
  private Ifrs9FacDataRepositoryDay ifrs9FacDataReposDay;

  @Autowired
  private Ifrs9FacDataRepositoryMon ifrs9FacDataReposMon;

  @Autowired
  private Ifrs9FacDataRepositoryHist ifrs9FacDataReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(ifrs9FacDataRepos);
    org.junit.Assert.assertNotNull(ifrs9FacDataReposDay);
    org.junit.Assert.assertNotNull(ifrs9FacDataReposMon);
    org.junit.Assert.assertNotNull(ifrs9FacDataReposHist);
  }

  @Override
  public Ifrs9FacData findById(Ifrs9FacDataId ifrs9FacDataId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + ifrs9FacDataId);
    Optional<Ifrs9FacData> ifrs9FacData = null;
    if (dbName.equals(ContentName.onDay))
      ifrs9FacData = ifrs9FacDataReposDay.findById(ifrs9FacDataId);
    else if (dbName.equals(ContentName.onMon))
      ifrs9FacData = ifrs9FacDataReposMon.findById(ifrs9FacDataId);
    else if (dbName.equals(ContentName.onHist))
      ifrs9FacData = ifrs9FacDataReposHist.findById(ifrs9FacDataId);
    else 
      ifrs9FacData = ifrs9FacDataRepos.findById(ifrs9FacDataId);
    Ifrs9FacData obj = ifrs9FacData.isPresent() ? ifrs9FacData.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<Ifrs9FacData> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<Ifrs9FacData> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = ifrs9FacDataReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = ifrs9FacDataReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = ifrs9FacDataReposHist.findAll(pageable);
    else 
      slice = ifrs9FacDataRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Ifrs9FacData holdById(Ifrs9FacDataId ifrs9FacDataId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + ifrs9FacDataId);
    Optional<Ifrs9FacData> ifrs9FacData = null;
    if (dbName.equals(ContentName.onDay))
      ifrs9FacData = ifrs9FacDataReposDay.findByIfrs9FacDataId(ifrs9FacDataId);
    else if (dbName.equals(ContentName.onMon))
      ifrs9FacData = ifrs9FacDataReposMon.findByIfrs9FacDataId(ifrs9FacDataId);
    else if (dbName.equals(ContentName.onHist))
      ifrs9FacData = ifrs9FacDataReposHist.findByIfrs9FacDataId(ifrs9FacDataId);
    else 
      ifrs9FacData = ifrs9FacDataRepos.findByIfrs9FacDataId(ifrs9FacDataId);
    return ifrs9FacData.isPresent() ? ifrs9FacData.get() : null;
  }

  @Override
  public Ifrs9FacData holdById(Ifrs9FacData ifrs9FacData, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + ifrs9FacData.getIfrs9FacDataId());
    Optional<Ifrs9FacData> ifrs9FacDataT = null;
    if (dbName.equals(ContentName.onDay))
      ifrs9FacDataT = ifrs9FacDataReposDay.findByIfrs9FacDataId(ifrs9FacData.getIfrs9FacDataId());
    else if (dbName.equals(ContentName.onMon))
      ifrs9FacDataT = ifrs9FacDataReposMon.findByIfrs9FacDataId(ifrs9FacData.getIfrs9FacDataId());
    else if (dbName.equals(ContentName.onHist))
      ifrs9FacDataT = ifrs9FacDataReposHist.findByIfrs9FacDataId(ifrs9FacData.getIfrs9FacDataId());
    else 
      ifrs9FacDataT = ifrs9FacDataRepos.findByIfrs9FacDataId(ifrs9FacData.getIfrs9FacDataId());
    return ifrs9FacDataT.isPresent() ? ifrs9FacDataT.get() : null;
  }

  @Override
  public Ifrs9FacData insert(Ifrs9FacData ifrs9FacData, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + ifrs9FacData.getIfrs9FacDataId());
    if (this.findById(ifrs9FacData.getIfrs9FacDataId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      ifrs9FacData.setCreateEmpNo(empNot);

    if(ifrs9FacData.getLastUpdateEmpNo() == null || ifrs9FacData.getLastUpdateEmpNo().isEmpty())
      ifrs9FacData.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return ifrs9FacDataReposDay.saveAndFlush(ifrs9FacData);	
    else if (dbName.equals(ContentName.onMon))
      return ifrs9FacDataReposMon.saveAndFlush(ifrs9FacData);
    else if (dbName.equals(ContentName.onHist))
      return ifrs9FacDataReposHist.saveAndFlush(ifrs9FacData);
    else 
    return ifrs9FacDataRepos.saveAndFlush(ifrs9FacData);
  }

  @Override
  public Ifrs9FacData update(Ifrs9FacData ifrs9FacData, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + ifrs9FacData.getIfrs9FacDataId());
    if (!empNot.isEmpty())
      ifrs9FacData.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return ifrs9FacDataReposDay.saveAndFlush(ifrs9FacData);	
    else if (dbName.equals(ContentName.onMon))
      return ifrs9FacDataReposMon.saveAndFlush(ifrs9FacData);
    else if (dbName.equals(ContentName.onHist))
      return ifrs9FacDataReposHist.saveAndFlush(ifrs9FacData);
    else 
    return ifrs9FacDataRepos.saveAndFlush(ifrs9FacData);
  }

  @Override
  public Ifrs9FacData update2(Ifrs9FacData ifrs9FacData, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + ifrs9FacData.getIfrs9FacDataId());
    if (!empNot.isEmpty())
      ifrs9FacData.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      ifrs9FacDataReposDay.saveAndFlush(ifrs9FacData);	
    else if (dbName.equals(ContentName.onMon))
      ifrs9FacDataReposMon.saveAndFlush(ifrs9FacData);
    else if (dbName.equals(ContentName.onHist))
        ifrs9FacDataReposHist.saveAndFlush(ifrs9FacData);
    else 
      ifrs9FacDataRepos.saveAndFlush(ifrs9FacData);	
    return this.findById(ifrs9FacData.getIfrs9FacDataId());
  }

  @Override
  public void delete(Ifrs9FacData ifrs9FacData, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + ifrs9FacData.getIfrs9FacDataId());
    if (dbName.equals(ContentName.onDay)) {
      ifrs9FacDataReposDay.delete(ifrs9FacData);	
      ifrs9FacDataReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ifrs9FacDataReposMon.delete(ifrs9FacData);	
      ifrs9FacDataReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ifrs9FacDataReposHist.delete(ifrs9FacData);
      ifrs9FacDataReposHist.flush();
    }
    else {
      ifrs9FacDataRepos.delete(ifrs9FacData);
      ifrs9FacDataRepos.flush();
    }
   }

  @Override
  public void insertAll(List<Ifrs9FacData> ifrs9FacData, TitaVo... titaVo) throws DBException {
    if (ifrs9FacData == null || ifrs9FacData.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (Ifrs9FacData t : ifrs9FacData){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      ifrs9FacData = ifrs9FacDataReposDay.saveAll(ifrs9FacData);	
      ifrs9FacDataReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ifrs9FacData = ifrs9FacDataReposMon.saveAll(ifrs9FacData);	
      ifrs9FacDataReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ifrs9FacData = ifrs9FacDataReposHist.saveAll(ifrs9FacData);
      ifrs9FacDataReposHist.flush();
    }
    else {
      ifrs9FacData = ifrs9FacDataRepos.saveAll(ifrs9FacData);
      ifrs9FacDataRepos.flush();
    }
    }

  @Override
  public void updateAll(List<Ifrs9FacData> ifrs9FacData, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (ifrs9FacData == null || ifrs9FacData.size() == 0)
      throw new DBException(6);

    for (Ifrs9FacData t : ifrs9FacData) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      ifrs9FacData = ifrs9FacDataReposDay.saveAll(ifrs9FacData);	
      ifrs9FacDataReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ifrs9FacData = ifrs9FacDataReposMon.saveAll(ifrs9FacData);	
      ifrs9FacDataReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ifrs9FacData = ifrs9FacDataReposHist.saveAll(ifrs9FacData);
      ifrs9FacDataReposHist.flush();
    }
    else {
      ifrs9FacData = ifrs9FacDataRepos.saveAll(ifrs9FacData);
      ifrs9FacDataRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<Ifrs9FacData> ifrs9FacData, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (ifrs9FacData == null || ifrs9FacData.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      ifrs9FacDataReposDay.deleteAll(ifrs9FacData);	
      ifrs9FacDataReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ifrs9FacDataReposMon.deleteAll(ifrs9FacData);	
      ifrs9FacDataReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ifrs9FacDataReposHist.deleteAll(ifrs9FacData);
      ifrs9FacDataReposHist.flush();
    }
    else {
      ifrs9FacDataRepos.deleteAll(ifrs9FacData);
      ifrs9FacDataRepos.flush();
    }
  }

  @Override
  public void Usp_L7_Ifrs9FacData_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      ifrs9FacDataReposDay.uspL7Ifrs9facdataUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      ifrs9FacDataReposMon.uspL7Ifrs9facdataUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      ifrs9FacDataReposHist.uspL7Ifrs9facdataUpd(TBSDYF, EmpNo);
   else
      ifrs9FacDataRepos.uspL7Ifrs9facdataUpd(TBSDYF, EmpNo);
  }

}
