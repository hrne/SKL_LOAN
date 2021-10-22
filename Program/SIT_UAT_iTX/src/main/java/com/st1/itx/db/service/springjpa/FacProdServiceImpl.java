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
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.repository.online.FacProdRepository;
import com.st1.itx.db.repository.day.FacProdRepositoryDay;
import com.st1.itx.db.repository.mon.FacProdRepositoryMon;
import com.st1.itx.db.repository.hist.FacProdRepositoryHist;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("facProdService")
@Repository
public class FacProdServiceImpl extends ASpringJpaParm implements FacProdService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private FacProdRepository facProdRepos;

  @Autowired
  private FacProdRepositoryDay facProdReposDay;

  @Autowired
  private FacProdRepositoryMon facProdReposMon;

  @Autowired
  private FacProdRepositoryHist facProdReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(facProdRepos);
    org.junit.Assert.assertNotNull(facProdReposDay);
    org.junit.Assert.assertNotNull(facProdReposMon);
    org.junit.Assert.assertNotNull(facProdReposHist);
  }

  @Override
  public FacProd findById(String prodNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + prodNo);
    Optional<FacProd> facProd = null;
    if (dbName.equals(ContentName.onDay))
      facProd = facProdReposDay.findById(prodNo);
    else if (dbName.equals(ContentName.onMon))
      facProd = facProdReposMon.findById(prodNo);
    else if (dbName.equals(ContentName.onHist))
      facProd = facProdReposHist.findById(prodNo);
    else 
      facProd = facProdRepos.findById(prodNo);
    FacProd obj = facProd.isPresent() ? facProd.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<FacProd> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacProd> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ProdNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ProdNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = facProdReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facProdReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facProdReposHist.findAll(pageable);
    else 
      slice = facProdRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<FacProd> prodNoLike(String prodNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacProd> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("prodNoLike " + dbName + " : " + "prodNo_0 : " + prodNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = facProdReposDay.findAllByProdNoLikeOrderByProdNoAsc(prodNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facProdReposMon.findAllByProdNoLikeOrderByProdNoAsc(prodNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facProdReposHist.findAllByProdNoLikeOrderByProdNoAsc(prodNo_0, pageable);
    else 
      slice = facProdRepos.findAllByProdNoLikeOrderByProdNoAsc(prodNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<FacProd> fildStatus(String prodNo_0, List<String> statusCode_1, List<String> govOfferFlag_2, List<String> financialFlag_3, List<String> empFlag_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacProd> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("fildStatus " + dbName + " : " + "prodNo_0 : " + prodNo_0 + " statusCode_1 : " +  statusCode_1 + " govOfferFlag_2 : " +  govOfferFlag_2 + " financialFlag_3 : " +  financialFlag_3 + " empFlag_4 : " +  empFlag_4);
    if (dbName.equals(ContentName.onDay))
      slice = facProdReposDay.findAllByProdNoLikeAndStatusCodeInAndGovOfferFlagInAndFinancialFlagInAndEmpFlagInOrderByProdNoAsc(prodNo_0, statusCode_1, govOfferFlag_2, financialFlag_3, empFlag_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facProdReposMon.findAllByProdNoLikeAndStatusCodeInAndGovOfferFlagInAndFinancialFlagInAndEmpFlagInOrderByProdNoAsc(prodNo_0, statusCode_1, govOfferFlag_2, financialFlag_3, empFlag_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facProdReposHist.findAllByProdNoLikeAndStatusCodeInAndGovOfferFlagInAndFinancialFlagInAndEmpFlagInOrderByProdNoAsc(prodNo_0, statusCode_1, govOfferFlag_2, financialFlag_3, empFlag_4, pageable);
    else 
      slice = facProdRepos.findAllByProdNoLikeAndStatusCodeInAndGovOfferFlagInAndFinancialFlagInAndEmpFlagInOrderByProdNoAsc(prodNo_0, statusCode_1, govOfferFlag_2, financialFlag_3, empFlag_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<FacProd> fildentCode(List<String> enterpriseFg_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacProd> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("fildentCode " + dbName + " : " + "enterpriseFg_0 : " + enterpriseFg_0);
    if (dbName.equals(ContentName.onDay))
      slice = facProdReposDay.findAllByEnterpriseFgInOrderByProdNoAsc(enterpriseFg_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facProdReposMon.findAllByEnterpriseFgInOrderByProdNoAsc(enterpriseFg_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facProdReposHist.findAllByEnterpriseFgInOrderByProdNoAsc(enterpriseFg_0, pageable);
    else 
      slice = facProdRepos.findAllByEnterpriseFgInOrderByProdNoAsc(enterpriseFg_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<FacProd> fildProdNo(String prodNo_0, List<String> statusCode_1, List<String> enterpriseFg_2, List<String> govOfferFlag_3, List<String> financialFlag_4, List<String> empFlag_5, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacProd> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("fildProdNo " + dbName + " : " + "prodNo_0 : " + prodNo_0 + " statusCode_1 : " +  statusCode_1 + " enterpriseFg_2 : " +  enterpriseFg_2 + " govOfferFlag_3 : " +  govOfferFlag_3 + " financialFlag_4 : " +  financialFlag_4 + " empFlag_5 : " +  empFlag_5);
    if (dbName.equals(ContentName.onDay))
      slice = facProdReposDay.findAllByProdNoLikeAndStatusCodeInAndEnterpriseFgInAndGovOfferFlagInAndFinancialFlagInAndEmpFlagInOrderByProdNoAsc(prodNo_0, statusCode_1, enterpriseFg_2, govOfferFlag_3, financialFlag_4, empFlag_5, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facProdReposMon.findAllByProdNoLikeAndStatusCodeInAndEnterpriseFgInAndGovOfferFlagInAndFinancialFlagInAndEmpFlagInOrderByProdNoAsc(prodNo_0, statusCode_1, enterpriseFg_2, govOfferFlag_3, financialFlag_4, empFlag_5, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facProdReposHist.findAllByProdNoLikeAndStatusCodeInAndEnterpriseFgInAndGovOfferFlagInAndFinancialFlagInAndEmpFlagInOrderByProdNoAsc(prodNo_0, statusCode_1, enterpriseFg_2, govOfferFlag_3, financialFlag_4, empFlag_5, pageable);
    else 
      slice = facProdRepos.findAllByProdNoLikeAndStatusCodeInAndEnterpriseFgInAndGovOfferFlagInAndFinancialFlagInAndEmpFlagInOrderByProdNoAsc(prodNo_0, statusCode_1, enterpriseFg_2, govOfferFlag_3, financialFlag_4, empFlag_5, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public FacProd holdById(String prodNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + prodNo);
    Optional<FacProd> facProd = null;
    if (dbName.equals(ContentName.onDay))
      facProd = facProdReposDay.findByProdNo(prodNo);
    else if (dbName.equals(ContentName.onMon))
      facProd = facProdReposMon.findByProdNo(prodNo);
    else if (dbName.equals(ContentName.onHist))
      facProd = facProdReposHist.findByProdNo(prodNo);
    else 
      facProd = facProdRepos.findByProdNo(prodNo);
    return facProd.isPresent() ? facProd.get() : null;
  }

  @Override
  public FacProd holdById(FacProd facProd, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + facProd.getProdNo());
    Optional<FacProd> facProdT = null;
    if (dbName.equals(ContentName.onDay))
      facProdT = facProdReposDay.findByProdNo(facProd.getProdNo());
    else if (dbName.equals(ContentName.onMon))
      facProdT = facProdReposMon.findByProdNo(facProd.getProdNo());
    else if (dbName.equals(ContentName.onHist))
      facProdT = facProdReposHist.findByProdNo(facProd.getProdNo());
    else 
      facProdT = facProdRepos.findByProdNo(facProd.getProdNo());
    return facProdT.isPresent() ? facProdT.get() : null;
  }

  @Override
  public FacProd insert(FacProd facProd, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + facProd.getProdNo());
    if (this.findById(facProd.getProdNo()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      facProd.setCreateEmpNo(empNot);

    if(facProd.getLastUpdateEmpNo() == null || facProd.getLastUpdateEmpNo().isEmpty())
      facProd.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return facProdReposDay.saveAndFlush(facProd);	
    else if (dbName.equals(ContentName.onMon))
      return facProdReposMon.saveAndFlush(facProd);
    else if (dbName.equals(ContentName.onHist))
      return facProdReposHist.saveAndFlush(facProd);
    else 
    return facProdRepos.saveAndFlush(facProd);
  }

  @Override
  public FacProd update(FacProd facProd, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + facProd.getProdNo());
    if (!empNot.isEmpty())
      facProd.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return facProdReposDay.saveAndFlush(facProd);	
    else if (dbName.equals(ContentName.onMon))
      return facProdReposMon.saveAndFlush(facProd);
    else if (dbName.equals(ContentName.onHist))
      return facProdReposHist.saveAndFlush(facProd);
    else 
    return facProdRepos.saveAndFlush(facProd);
  }

  @Override
  public FacProd update2(FacProd facProd, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + facProd.getProdNo());
    if (!empNot.isEmpty())
      facProd.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      facProdReposDay.saveAndFlush(facProd);	
    else if (dbName.equals(ContentName.onMon))
      facProdReposMon.saveAndFlush(facProd);
    else if (dbName.equals(ContentName.onHist))
        facProdReposHist.saveAndFlush(facProd);
    else 
      facProdRepos.saveAndFlush(facProd);	
    return this.findById(facProd.getProdNo());
  }

  @Override
  public void delete(FacProd facProd, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + facProd.getProdNo());
    if (dbName.equals(ContentName.onDay)) {
      facProdReposDay.delete(facProd);	
      facProdReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facProdReposMon.delete(facProd);	
      facProdReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facProdReposHist.delete(facProd);
      facProdReposHist.flush();
    }
    else {
      facProdRepos.delete(facProd);
      facProdRepos.flush();
    }
   }

  @Override
  public void insertAll(List<FacProd> facProd, TitaVo... titaVo) throws DBException {
    if (facProd == null || facProd.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (FacProd t : facProd){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      facProd = facProdReposDay.saveAll(facProd);	
      facProdReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facProd = facProdReposMon.saveAll(facProd);	
      facProdReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facProd = facProdReposHist.saveAll(facProd);
      facProdReposHist.flush();
    }
    else {
      facProd = facProdRepos.saveAll(facProd);
      facProdRepos.flush();
    }
    }

  @Override
  public void updateAll(List<FacProd> facProd, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (facProd == null || facProd.size() == 0)
      throw new DBException(6);

    for (FacProd t : facProd) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      facProd = facProdReposDay.saveAll(facProd);	
      facProdReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facProd = facProdReposMon.saveAll(facProd);	
      facProdReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facProd = facProdReposHist.saveAll(facProd);
      facProdReposHist.flush();
    }
    else {
      facProd = facProdRepos.saveAll(facProd);
      facProdRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<FacProd> facProd, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (facProd == null || facProd.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      facProdReposDay.deleteAll(facProd);	
      facProdReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facProdReposMon.deleteAll(facProd);	
      facProdReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facProdReposHist.deleteAll(facProd);
      facProdReposHist.flush();
    }
    else {
      facProdRepos.deleteAll(facProd);
      facProdRepos.flush();
    }
  }

}
