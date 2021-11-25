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
import com.st1.itx.db.domain.Ifrs9LoanData;
import com.st1.itx.db.domain.Ifrs9LoanDataId;
import com.st1.itx.db.repository.online.Ifrs9LoanDataRepository;
import com.st1.itx.db.repository.day.Ifrs9LoanDataRepositoryDay;
import com.st1.itx.db.repository.mon.Ifrs9LoanDataRepositoryMon;
import com.st1.itx.db.repository.hist.Ifrs9LoanDataRepositoryHist;
import com.st1.itx.db.service.Ifrs9LoanDataService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("ifrs9LoanDataService")
@Repository
public class Ifrs9LoanDataServiceImpl extends ASpringJpaParm implements Ifrs9LoanDataService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private Ifrs9LoanDataRepository ifrs9LoanDataRepos;

  @Autowired
  private Ifrs9LoanDataRepositoryDay ifrs9LoanDataReposDay;

  @Autowired
  private Ifrs9LoanDataRepositoryMon ifrs9LoanDataReposMon;

  @Autowired
  private Ifrs9LoanDataRepositoryHist ifrs9LoanDataReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(ifrs9LoanDataRepos);
    org.junit.Assert.assertNotNull(ifrs9LoanDataReposDay);
    org.junit.Assert.assertNotNull(ifrs9LoanDataReposMon);
    org.junit.Assert.assertNotNull(ifrs9LoanDataReposHist);
  }

  @Override
  public Ifrs9LoanData findById(Ifrs9LoanDataId ifrs9LoanDataId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + ifrs9LoanDataId);
    Optional<Ifrs9LoanData> ifrs9LoanData = null;
    if (dbName.equals(ContentName.onDay))
      ifrs9LoanData = ifrs9LoanDataReposDay.findById(ifrs9LoanDataId);
    else if (dbName.equals(ContentName.onMon))
      ifrs9LoanData = ifrs9LoanDataReposMon.findById(ifrs9LoanDataId);
    else if (dbName.equals(ContentName.onHist))
      ifrs9LoanData = ifrs9LoanDataReposHist.findById(ifrs9LoanDataId);
    else 
      ifrs9LoanData = ifrs9LoanDataRepos.findById(ifrs9LoanDataId);
    Ifrs9LoanData obj = ifrs9LoanData.isPresent() ? ifrs9LoanData.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<Ifrs9LoanData> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<Ifrs9LoanData> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = ifrs9LoanDataReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = ifrs9LoanDataReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = ifrs9LoanDataReposHist.findAll(pageable);
    else 
      slice = ifrs9LoanDataRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Ifrs9LoanData holdById(Ifrs9LoanDataId ifrs9LoanDataId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + ifrs9LoanDataId);
    Optional<Ifrs9LoanData> ifrs9LoanData = null;
    if (dbName.equals(ContentName.onDay))
      ifrs9LoanData = ifrs9LoanDataReposDay.findByIfrs9LoanDataId(ifrs9LoanDataId);
    else if (dbName.equals(ContentName.onMon))
      ifrs9LoanData = ifrs9LoanDataReposMon.findByIfrs9LoanDataId(ifrs9LoanDataId);
    else if (dbName.equals(ContentName.onHist))
      ifrs9LoanData = ifrs9LoanDataReposHist.findByIfrs9LoanDataId(ifrs9LoanDataId);
    else 
      ifrs9LoanData = ifrs9LoanDataRepos.findByIfrs9LoanDataId(ifrs9LoanDataId);
    return ifrs9LoanData.isPresent() ? ifrs9LoanData.get() : null;
  }

  @Override
  public Ifrs9LoanData holdById(Ifrs9LoanData ifrs9LoanData, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + ifrs9LoanData.getIfrs9LoanDataId());
    Optional<Ifrs9LoanData> ifrs9LoanDataT = null;
    if (dbName.equals(ContentName.onDay))
      ifrs9LoanDataT = ifrs9LoanDataReposDay.findByIfrs9LoanDataId(ifrs9LoanData.getIfrs9LoanDataId());
    else if (dbName.equals(ContentName.onMon))
      ifrs9LoanDataT = ifrs9LoanDataReposMon.findByIfrs9LoanDataId(ifrs9LoanData.getIfrs9LoanDataId());
    else if (dbName.equals(ContentName.onHist))
      ifrs9LoanDataT = ifrs9LoanDataReposHist.findByIfrs9LoanDataId(ifrs9LoanData.getIfrs9LoanDataId());
    else 
      ifrs9LoanDataT = ifrs9LoanDataRepos.findByIfrs9LoanDataId(ifrs9LoanData.getIfrs9LoanDataId());
    return ifrs9LoanDataT.isPresent() ? ifrs9LoanDataT.get() : null;
  }

  @Override
  public Ifrs9LoanData insert(Ifrs9LoanData ifrs9LoanData, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + ifrs9LoanData.getIfrs9LoanDataId());
    if (this.findById(ifrs9LoanData.getIfrs9LoanDataId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      ifrs9LoanData.setCreateEmpNo(empNot);

    if(ifrs9LoanData.getLastUpdateEmpNo() == null || ifrs9LoanData.getLastUpdateEmpNo().isEmpty())
      ifrs9LoanData.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return ifrs9LoanDataReposDay.saveAndFlush(ifrs9LoanData);	
    else if (dbName.equals(ContentName.onMon))
      return ifrs9LoanDataReposMon.saveAndFlush(ifrs9LoanData);
    else if (dbName.equals(ContentName.onHist))
      return ifrs9LoanDataReposHist.saveAndFlush(ifrs9LoanData);
    else 
    return ifrs9LoanDataRepos.saveAndFlush(ifrs9LoanData);
  }

  @Override
  public Ifrs9LoanData update(Ifrs9LoanData ifrs9LoanData, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + ifrs9LoanData.getIfrs9LoanDataId());
    if (!empNot.isEmpty())
      ifrs9LoanData.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return ifrs9LoanDataReposDay.saveAndFlush(ifrs9LoanData);	
    else if (dbName.equals(ContentName.onMon))
      return ifrs9LoanDataReposMon.saveAndFlush(ifrs9LoanData);
    else if (dbName.equals(ContentName.onHist))
      return ifrs9LoanDataReposHist.saveAndFlush(ifrs9LoanData);
    else 
    return ifrs9LoanDataRepos.saveAndFlush(ifrs9LoanData);
  }

  @Override
  public Ifrs9LoanData update2(Ifrs9LoanData ifrs9LoanData, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + ifrs9LoanData.getIfrs9LoanDataId());
    if (!empNot.isEmpty())
      ifrs9LoanData.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      ifrs9LoanDataReposDay.saveAndFlush(ifrs9LoanData);	
    else if (dbName.equals(ContentName.onMon))
      ifrs9LoanDataReposMon.saveAndFlush(ifrs9LoanData);
    else if (dbName.equals(ContentName.onHist))
        ifrs9LoanDataReposHist.saveAndFlush(ifrs9LoanData);
    else 
      ifrs9LoanDataRepos.saveAndFlush(ifrs9LoanData);	
    return this.findById(ifrs9LoanData.getIfrs9LoanDataId());
  }

  @Override
  public void delete(Ifrs9LoanData ifrs9LoanData, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + ifrs9LoanData.getIfrs9LoanDataId());
    if (dbName.equals(ContentName.onDay)) {
      ifrs9LoanDataReposDay.delete(ifrs9LoanData);	
      ifrs9LoanDataReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ifrs9LoanDataReposMon.delete(ifrs9LoanData);	
      ifrs9LoanDataReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ifrs9LoanDataReposHist.delete(ifrs9LoanData);
      ifrs9LoanDataReposHist.flush();
    }
    else {
      ifrs9LoanDataRepos.delete(ifrs9LoanData);
      ifrs9LoanDataRepos.flush();
    }
   }

  @Override
  public void insertAll(List<Ifrs9LoanData> ifrs9LoanData, TitaVo... titaVo) throws DBException {
    if (ifrs9LoanData == null || ifrs9LoanData.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (Ifrs9LoanData t : ifrs9LoanData){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      ifrs9LoanData = ifrs9LoanDataReposDay.saveAll(ifrs9LoanData);	
      ifrs9LoanDataReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ifrs9LoanData = ifrs9LoanDataReposMon.saveAll(ifrs9LoanData);	
      ifrs9LoanDataReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ifrs9LoanData = ifrs9LoanDataReposHist.saveAll(ifrs9LoanData);
      ifrs9LoanDataReposHist.flush();
    }
    else {
      ifrs9LoanData = ifrs9LoanDataRepos.saveAll(ifrs9LoanData);
      ifrs9LoanDataRepos.flush();
    }
    }

  @Override
  public void updateAll(List<Ifrs9LoanData> ifrs9LoanData, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (ifrs9LoanData == null || ifrs9LoanData.size() == 0)
      throw new DBException(6);

    for (Ifrs9LoanData t : ifrs9LoanData) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      ifrs9LoanData = ifrs9LoanDataReposDay.saveAll(ifrs9LoanData);	
      ifrs9LoanDataReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ifrs9LoanData = ifrs9LoanDataReposMon.saveAll(ifrs9LoanData);	
      ifrs9LoanDataReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ifrs9LoanData = ifrs9LoanDataReposHist.saveAll(ifrs9LoanData);
      ifrs9LoanDataReposHist.flush();
    }
    else {
      ifrs9LoanData = ifrs9LoanDataRepos.saveAll(ifrs9LoanData);
      ifrs9LoanDataRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<Ifrs9LoanData> ifrs9LoanData, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (ifrs9LoanData == null || ifrs9LoanData.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      ifrs9LoanDataReposDay.deleteAll(ifrs9LoanData);	
      ifrs9LoanDataReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      ifrs9LoanDataReposMon.deleteAll(ifrs9LoanData);	
      ifrs9LoanDataReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      ifrs9LoanDataReposHist.deleteAll(ifrs9LoanData);
      ifrs9LoanDataReposHist.flush();
    }
    else {
      ifrs9LoanDataRepos.deleteAll(ifrs9LoanData);
      ifrs9LoanDataRepos.flush();
    }
  }

  @Override
  public void Usp_L7_Ifrs9LoanData_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      ifrs9LoanDataReposDay.uspL7Ifrs9loandataUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      ifrs9LoanDataReposMon.uspL7Ifrs9loandataUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      ifrs9LoanDataReposHist.uspL7Ifrs9loandataUpd(TBSDYF, EmpNo);
   else
      ifrs9LoanDataRepos.uspL7Ifrs9loandataUpd(TBSDYF, EmpNo);
  }

}
