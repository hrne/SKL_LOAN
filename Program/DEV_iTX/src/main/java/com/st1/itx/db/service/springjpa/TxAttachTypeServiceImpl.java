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
import com.st1.itx.db.domain.TxAttachType;
import com.st1.itx.db.repository.online.TxAttachTypeRepository;
import com.st1.itx.db.repository.day.TxAttachTypeRepositoryDay;
import com.st1.itx.db.repository.mon.TxAttachTypeRepositoryMon;
import com.st1.itx.db.repository.hist.TxAttachTypeRepositoryHist;
import com.st1.itx.db.service.TxAttachTypeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txAttachTypeService")
@Repository
public class TxAttachTypeServiceImpl extends ASpringJpaParm implements TxAttachTypeService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxAttachTypeRepository txAttachTypeRepos;

  @Autowired
  private TxAttachTypeRepositoryDay txAttachTypeReposDay;

  @Autowired
  private TxAttachTypeRepositoryMon txAttachTypeReposMon;

  @Autowired
  private TxAttachTypeRepositoryHist txAttachTypeReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txAttachTypeRepos);
    org.junit.Assert.assertNotNull(txAttachTypeReposDay);
    org.junit.Assert.assertNotNull(txAttachTypeReposMon);
    org.junit.Assert.assertNotNull(txAttachTypeReposHist);
  }

  @Override
  public TxAttachType findById(Long typeNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + typeNo);
    Optional<TxAttachType> txAttachType = null;
    if (dbName.equals(ContentName.onDay))
      txAttachType = txAttachTypeReposDay.findById(typeNo);
    else if (dbName.equals(ContentName.onMon))
      txAttachType = txAttachTypeReposMon.findById(typeNo);
    else if (dbName.equals(ContentName.onHist))
      txAttachType = txAttachTypeReposHist.findById(typeNo);
    else 
      txAttachType = txAttachTypeRepos.findById(typeNo);
    TxAttachType obj = txAttachType.isPresent() ? txAttachType.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxAttachType> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAttachType> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "TypeNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "TypeNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txAttachTypeReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAttachTypeReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAttachTypeReposHist.findAll(pageable);
    else 
      slice = txAttachTypeRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxAttachType> findByTranNo(String tranNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAttachType> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByTranNo " + dbName + " : " + "tranNo_0 : " + tranNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = txAttachTypeReposDay.findAllByTranNoIsOrderByTypeItemAsc(tranNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAttachTypeReposMon.findAllByTranNoIsOrderByTypeItemAsc(tranNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAttachTypeReposHist.findAllByTranNoIsOrderByTypeItemAsc(tranNo_0, pageable);
    else 
      slice = txAttachTypeRepos.findAllByTranNoIsOrderByTypeItemAsc(tranNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxAttachType findByTypeItemFirst(String tranNo_0, String typeItem_1, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findByTypeItemFirst " + dbName + " : " + "tranNo_0 : " + tranNo_0 + " typeItem_1 : " +  typeItem_1);
    Optional<TxAttachType> txAttachTypeT = null;
    if (dbName.equals(ContentName.onDay))
      txAttachTypeT = txAttachTypeReposDay.findTopByTranNoIsAndTypeItemIs(tranNo_0, typeItem_1);
    else if (dbName.equals(ContentName.onMon))
      txAttachTypeT = txAttachTypeReposMon.findTopByTranNoIsAndTypeItemIs(tranNo_0, typeItem_1);
    else if (dbName.equals(ContentName.onHist))
      txAttachTypeT = txAttachTypeReposHist.findTopByTranNoIsAndTypeItemIs(tranNo_0, typeItem_1);
    else 
      txAttachTypeT = txAttachTypeRepos.findTopByTranNoIsAndTypeItemIs(tranNo_0, typeItem_1);

    return txAttachTypeT.isPresent() ? txAttachTypeT.get() : null;
  }

  @Override
  public TxAttachType holdById(Long typeNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + typeNo);
    Optional<TxAttachType> txAttachType = null;
    if (dbName.equals(ContentName.onDay))
      txAttachType = txAttachTypeReposDay.findByTypeNo(typeNo);
    else if (dbName.equals(ContentName.onMon))
      txAttachType = txAttachTypeReposMon.findByTypeNo(typeNo);
    else if (dbName.equals(ContentName.onHist))
      txAttachType = txAttachTypeReposHist.findByTypeNo(typeNo);
    else 
      txAttachType = txAttachTypeRepos.findByTypeNo(typeNo);
    return txAttachType.isPresent() ? txAttachType.get() : null;
  }

  @Override
  public TxAttachType holdById(TxAttachType txAttachType, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txAttachType.getTypeNo());
    Optional<TxAttachType> txAttachTypeT = null;
    if (dbName.equals(ContentName.onDay))
      txAttachTypeT = txAttachTypeReposDay.findByTypeNo(txAttachType.getTypeNo());
    else if (dbName.equals(ContentName.onMon))
      txAttachTypeT = txAttachTypeReposMon.findByTypeNo(txAttachType.getTypeNo());
    else if (dbName.equals(ContentName.onHist))
      txAttachTypeT = txAttachTypeReposHist.findByTypeNo(txAttachType.getTypeNo());
    else 
      txAttachTypeT = txAttachTypeRepos.findByTypeNo(txAttachType.getTypeNo());
    return txAttachTypeT.isPresent() ? txAttachTypeT.get() : null;
  }

  @Override
  public TxAttachType insert(TxAttachType txAttachType, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + txAttachType.getTypeNo());
    if (this.findById(txAttachType.getTypeNo()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txAttachType.setCreateEmpNo(empNot);

    if(txAttachType.getLastUpdateEmpNo() == null || txAttachType.getLastUpdateEmpNo().isEmpty())
      txAttachType.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txAttachTypeReposDay.saveAndFlush(txAttachType);	
    else if (dbName.equals(ContentName.onMon))
      return txAttachTypeReposMon.saveAndFlush(txAttachType);
    else if (dbName.equals(ContentName.onHist))
      return txAttachTypeReposHist.saveAndFlush(txAttachType);
    else 
    return txAttachTypeRepos.saveAndFlush(txAttachType);
  }

  @Override
  public TxAttachType update(TxAttachType txAttachType, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txAttachType.getTypeNo());
    if (!empNot.isEmpty())
      txAttachType.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txAttachTypeReposDay.saveAndFlush(txAttachType);	
    else if (dbName.equals(ContentName.onMon))
      return txAttachTypeReposMon.saveAndFlush(txAttachType);
    else if (dbName.equals(ContentName.onHist))
      return txAttachTypeReposHist.saveAndFlush(txAttachType);
    else 
    return txAttachTypeRepos.saveAndFlush(txAttachType);
  }

  @Override
  public TxAttachType update2(TxAttachType txAttachType, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txAttachType.getTypeNo());
    if (!empNot.isEmpty())
      txAttachType.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txAttachTypeReposDay.saveAndFlush(txAttachType);	
    else if (dbName.equals(ContentName.onMon))
      txAttachTypeReposMon.saveAndFlush(txAttachType);
    else if (dbName.equals(ContentName.onHist))
        txAttachTypeReposHist.saveAndFlush(txAttachType);
    else 
      txAttachTypeRepos.saveAndFlush(txAttachType);	
    return this.findById(txAttachType.getTypeNo());
  }

  @Override
  public void delete(TxAttachType txAttachType, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + txAttachType.getTypeNo());
    if (dbName.equals(ContentName.onDay)) {
      txAttachTypeReposDay.delete(txAttachType);	
      txAttachTypeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAttachTypeReposMon.delete(txAttachType);	
      txAttachTypeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAttachTypeReposHist.delete(txAttachType);
      txAttachTypeReposHist.flush();
    }
    else {
      txAttachTypeRepos.delete(txAttachType);
      txAttachTypeRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxAttachType> txAttachType, TitaVo... titaVo) throws DBException {
    if (txAttachType == null || txAttachType.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (TxAttachType t : txAttachType){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      txAttachType = txAttachTypeReposDay.saveAll(txAttachType);	
      txAttachTypeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAttachType = txAttachTypeReposMon.saveAll(txAttachType);	
      txAttachTypeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAttachType = txAttachTypeReposHist.saveAll(txAttachType);
      txAttachTypeReposHist.flush();
    }
    else {
      txAttachType = txAttachTypeRepos.saveAll(txAttachType);
      txAttachTypeRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxAttachType> txAttachType, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (txAttachType == null || txAttachType.size() == 0)
      throw new DBException(6);

    for (TxAttachType t : txAttachType) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txAttachType = txAttachTypeReposDay.saveAll(txAttachType);	
      txAttachTypeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAttachType = txAttachTypeReposMon.saveAll(txAttachType);	
      txAttachTypeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAttachType = txAttachTypeReposHist.saveAll(txAttachType);
      txAttachTypeReposHist.flush();
    }
    else {
      txAttachType = txAttachTypeRepos.saveAll(txAttachType);
      txAttachTypeRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxAttachType> txAttachType, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txAttachType == null || txAttachType.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txAttachTypeReposDay.deleteAll(txAttachType);	
      txAttachTypeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAttachTypeReposMon.deleteAll(txAttachType);	
      txAttachTypeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAttachTypeReposHist.deleteAll(txAttachType);
      txAttachTypeReposHist.flush();
    }
    else {
      txAttachTypeRepos.deleteAll(txAttachType);
      txAttachTypeRepos.flush();
    }
  }

}
