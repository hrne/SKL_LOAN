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
import com.st1.itx.db.domain.TxAuthorize;
import com.st1.itx.db.repository.online.TxAuthorizeRepository;
import com.st1.itx.db.repository.day.TxAuthorizeRepositoryDay;
import com.st1.itx.db.repository.mon.TxAuthorizeRepositoryMon;
import com.st1.itx.db.repository.hist.TxAuthorizeRepositoryHist;
import com.st1.itx.db.service.TxAuthorizeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txAuthorizeService")
@Repository
public class TxAuthorizeServiceImpl extends ASpringJpaParm implements TxAuthorizeService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxAuthorizeRepository txAuthorizeRepos;

  @Autowired
  private TxAuthorizeRepositoryDay txAuthorizeReposDay;

  @Autowired
  private TxAuthorizeRepositoryMon txAuthorizeReposMon;

  @Autowired
  private TxAuthorizeRepositoryHist txAuthorizeReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txAuthorizeRepos);
    org.junit.Assert.assertNotNull(txAuthorizeReposDay);
    org.junit.Assert.assertNotNull(txAuthorizeReposMon);
    org.junit.Assert.assertNotNull(txAuthorizeReposHist);
  }

  @Override
  public TxAuthorize findById(Long autoSeq, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + autoSeq);
    Optional<TxAuthorize> txAuthorize = null;
    if (dbName.equals(ContentName.onDay))
      txAuthorize = txAuthorizeReposDay.findById(autoSeq);
    else if (dbName.equals(ContentName.onMon))
      txAuthorize = txAuthorizeReposMon.findById(autoSeq);
    else if (dbName.equals(ContentName.onHist))
      txAuthorize = txAuthorizeReposHist.findById(autoSeq);
    else 
      txAuthorize = txAuthorizeRepos.findById(autoSeq);
    TxAuthorize obj = txAuthorize.isPresent() ? txAuthorize.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxAuthorize> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAuthorize> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AutoSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AutoSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txAuthorizeReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAuthorizeReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAuthorizeReposHist.findAll(pageable);
    else 
      slice = txAuthorizeRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxAuthorize> findSupNo(int entdy_0, String supNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAuthorize> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findSupNo " + dbName + " : " + "entdy_0 : " + entdy_0 + " supNo_1 : " +  supNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = txAuthorizeReposDay.findAllByEntdyIsAndSupNoIsOrderBySupNoAsc(entdy_0, supNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAuthorizeReposMon.findAllByEntdyIsAndSupNoIsOrderBySupNoAsc(entdy_0, supNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAuthorizeReposHist.findAllByEntdyIsAndSupNoIsOrderBySupNoAsc(entdy_0, supNo_1, pageable);
    else 
      slice = txAuthorizeRepos.findAllByEntdyIsAndSupNoIsOrderBySupNoAsc(entdy_0, supNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxAuthorize> findEntdy(int entdy_0, int entdy_1, String supNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAuthorize> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findEntdy " + dbName + " : " + "entdy_0 : " + entdy_0 + " entdy_1 : " +  entdy_1 + " supNo_2 : " +  supNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = txAuthorizeReposDay.findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndSupNoLikeOrderByEntdyAsc(entdy_0, entdy_1, supNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAuthorizeReposMon.findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndSupNoLikeOrderByEntdyAsc(entdy_0, entdy_1, supNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAuthorizeReposHist.findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndSupNoLikeOrderByEntdyAsc(entdy_0, entdy_1, supNo_2, pageable);
    else 
      slice = txAuthorizeRepos.findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndSupNoLikeOrderByEntdyAsc(entdy_0, entdy_1, supNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxAuthorize> findCreatDate(java.sql.Timestamp createDate_0, java.sql.Timestamp createDate_1, String supNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAuthorize> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCreatDate " + dbName + " : " + "createDate_0 : " + createDate_0 + " createDate_1 : " +  createDate_1 + " supNo_2 : " +  supNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = txAuthorizeReposDay.findAllByCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndSupNoLikeOrderByCreateDateAsc(createDate_0, createDate_1, supNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAuthorizeReposMon.findAllByCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndSupNoLikeOrderByCreateDateAsc(createDate_0, createDate_1, supNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAuthorizeReposHist.findAllByCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndSupNoLikeOrderByCreateDateAsc(createDate_0, createDate_1, supNo_2, pageable);
    else 
      slice = txAuthorizeRepos.findAllByCreateDateGreaterThanEqualAndCreateDateLessThanEqualAndSupNoLikeOrderByCreateDateAsc(createDate_0, createDate_1, supNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxAuthorize holdById(Long autoSeq, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + autoSeq);
    Optional<TxAuthorize> txAuthorize = null;
    if (dbName.equals(ContentName.onDay))
      txAuthorize = txAuthorizeReposDay.findByAutoSeq(autoSeq);
    else if (dbName.equals(ContentName.onMon))
      txAuthorize = txAuthorizeReposMon.findByAutoSeq(autoSeq);
    else if (dbName.equals(ContentName.onHist))
      txAuthorize = txAuthorizeReposHist.findByAutoSeq(autoSeq);
    else 
      txAuthorize = txAuthorizeRepos.findByAutoSeq(autoSeq);
    return txAuthorize.isPresent() ? txAuthorize.get() : null;
  }

  @Override
  public TxAuthorize holdById(TxAuthorize txAuthorize, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txAuthorize.getAutoSeq());
    Optional<TxAuthorize> txAuthorizeT = null;
    if (dbName.equals(ContentName.onDay))
      txAuthorizeT = txAuthorizeReposDay.findByAutoSeq(txAuthorize.getAutoSeq());
    else if (dbName.equals(ContentName.onMon))
      txAuthorizeT = txAuthorizeReposMon.findByAutoSeq(txAuthorize.getAutoSeq());
    else if (dbName.equals(ContentName.onHist))
      txAuthorizeT = txAuthorizeReposHist.findByAutoSeq(txAuthorize.getAutoSeq());
    else 
      txAuthorizeT = txAuthorizeRepos.findByAutoSeq(txAuthorize.getAutoSeq());
    return txAuthorizeT.isPresent() ? txAuthorizeT.get() : null;
  }

  @Override
  public TxAuthorize insert(TxAuthorize txAuthorize, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + txAuthorize.getAutoSeq());
    if (this.findById(txAuthorize.getAutoSeq()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txAuthorize.setCreateEmpNo(empNot);

    if(txAuthorize.getLastUpdateEmpNo() == null || txAuthorize.getLastUpdateEmpNo().isEmpty())
      txAuthorize.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txAuthorizeReposDay.saveAndFlush(txAuthorize);	
    else if (dbName.equals(ContentName.onMon))
      return txAuthorizeReposMon.saveAndFlush(txAuthorize);
    else if (dbName.equals(ContentName.onHist))
      return txAuthorizeReposHist.saveAndFlush(txAuthorize);
    else 
    return txAuthorizeRepos.saveAndFlush(txAuthorize);
  }

  @Override
  public TxAuthorize update(TxAuthorize txAuthorize, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txAuthorize.getAutoSeq());
    if (!empNot.isEmpty())
      txAuthorize.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txAuthorizeReposDay.saveAndFlush(txAuthorize);	
    else if (dbName.equals(ContentName.onMon))
      return txAuthorizeReposMon.saveAndFlush(txAuthorize);
    else if (dbName.equals(ContentName.onHist))
      return txAuthorizeReposHist.saveAndFlush(txAuthorize);
    else 
    return txAuthorizeRepos.saveAndFlush(txAuthorize);
  }

  @Override
  public TxAuthorize update2(TxAuthorize txAuthorize, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txAuthorize.getAutoSeq());
    if (!empNot.isEmpty())
      txAuthorize.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txAuthorizeReposDay.saveAndFlush(txAuthorize);	
    else if (dbName.equals(ContentName.onMon))
      txAuthorizeReposMon.saveAndFlush(txAuthorize);
    else if (dbName.equals(ContentName.onHist))
        txAuthorizeReposHist.saveAndFlush(txAuthorize);
    else 
      txAuthorizeRepos.saveAndFlush(txAuthorize);	
    return this.findById(txAuthorize.getAutoSeq());
  }

  @Override
  public void delete(TxAuthorize txAuthorize, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + txAuthorize.getAutoSeq());
    if (dbName.equals(ContentName.onDay)) {
      txAuthorizeReposDay.delete(txAuthorize);	
      txAuthorizeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAuthorizeReposMon.delete(txAuthorize);	
      txAuthorizeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAuthorizeReposHist.delete(txAuthorize);
      txAuthorizeReposHist.flush();
    }
    else {
      txAuthorizeRepos.delete(txAuthorize);
      txAuthorizeRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxAuthorize> txAuthorize, TitaVo... titaVo) throws DBException {
    if (txAuthorize == null || txAuthorize.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (TxAuthorize t : txAuthorize){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      txAuthorize = txAuthorizeReposDay.saveAll(txAuthorize);	
      txAuthorizeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAuthorize = txAuthorizeReposMon.saveAll(txAuthorize);	
      txAuthorizeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAuthorize = txAuthorizeReposHist.saveAll(txAuthorize);
      txAuthorizeReposHist.flush();
    }
    else {
      txAuthorize = txAuthorizeRepos.saveAll(txAuthorize);
      txAuthorizeRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxAuthorize> txAuthorize, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (txAuthorize == null || txAuthorize.size() == 0)
      throw new DBException(6);

    for (TxAuthorize t : txAuthorize) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txAuthorize = txAuthorizeReposDay.saveAll(txAuthorize);	
      txAuthorizeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAuthorize = txAuthorizeReposMon.saveAll(txAuthorize);	
      txAuthorizeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAuthorize = txAuthorizeReposHist.saveAll(txAuthorize);
      txAuthorizeReposHist.flush();
    }
    else {
      txAuthorize = txAuthorizeRepos.saveAll(txAuthorize);
      txAuthorizeRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxAuthorize> txAuthorize, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txAuthorize == null || txAuthorize.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txAuthorizeReposDay.deleteAll(txAuthorize);	
      txAuthorizeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAuthorizeReposMon.deleteAll(txAuthorize);	
      txAuthorizeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAuthorizeReposHist.deleteAll(txAuthorize);
      txAuthorizeReposHist.flush();
    }
    else {
      txAuthorizeRepos.deleteAll(txAuthorize);
      txAuthorizeRepos.flush();
    }
  }

}
