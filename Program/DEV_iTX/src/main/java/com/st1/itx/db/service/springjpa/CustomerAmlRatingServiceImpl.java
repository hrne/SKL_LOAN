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
import com.st1.itx.db.domain.CustomerAmlRating;
import com.st1.itx.db.repository.online.CustomerAmlRatingRepository;
import com.st1.itx.db.repository.day.CustomerAmlRatingRepositoryDay;
import com.st1.itx.db.repository.mon.CustomerAmlRatingRepositoryMon;
import com.st1.itx.db.repository.hist.CustomerAmlRatingRepositoryHist;
import com.st1.itx.db.service.CustomerAmlRatingService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("customerAmlRatingService")
@Repository
public class CustomerAmlRatingServiceImpl extends ASpringJpaParm implements CustomerAmlRatingService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CustomerAmlRatingRepository customerAmlRatingRepos;

  @Autowired
  private CustomerAmlRatingRepositoryDay customerAmlRatingReposDay;

  @Autowired
  private CustomerAmlRatingRepositoryMon customerAmlRatingReposMon;

  @Autowired
  private CustomerAmlRatingRepositoryHist customerAmlRatingReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(customerAmlRatingRepos);
    org.junit.Assert.assertNotNull(customerAmlRatingReposDay);
    org.junit.Assert.assertNotNull(customerAmlRatingReposMon);
    org.junit.Assert.assertNotNull(customerAmlRatingReposHist);
  }

  @Override
  public CustomerAmlRating findById(String custId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + custId);
    Optional<CustomerAmlRating> customerAmlRating = null;
    if (dbName.equals(ContentName.onDay))
      customerAmlRating = customerAmlRatingReposDay.findById(custId);
    else if (dbName.equals(ContentName.onMon))
      customerAmlRating = customerAmlRatingReposMon.findById(custId);
    else if (dbName.equals(ContentName.onHist))
      customerAmlRating = customerAmlRatingReposHist.findById(custId);
    else 
      customerAmlRating = customerAmlRatingRepos.findById(custId);
    CustomerAmlRating obj = customerAmlRating.isPresent() ? customerAmlRating.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CustomerAmlRating> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CustomerAmlRating> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustId"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustId"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = customerAmlRatingReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = customerAmlRatingReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = customerAmlRatingReposHist.findAll(pageable);
    else 
      slice = customerAmlRatingRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CustomerAmlRating holdById(String custId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + custId);
    Optional<CustomerAmlRating> customerAmlRating = null;
    if (dbName.equals(ContentName.onDay))
      customerAmlRating = customerAmlRatingReposDay.findByCustId(custId);
    else if (dbName.equals(ContentName.onMon))
      customerAmlRating = customerAmlRatingReposMon.findByCustId(custId);
    else if (dbName.equals(ContentName.onHist))
      customerAmlRating = customerAmlRatingReposHist.findByCustId(custId);
    else 
      customerAmlRating = customerAmlRatingRepos.findByCustId(custId);
    return customerAmlRating.isPresent() ? customerAmlRating.get() : null;
  }

  @Override
  public CustomerAmlRating holdById(CustomerAmlRating customerAmlRating, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + customerAmlRating.getCustId());
    Optional<CustomerAmlRating> customerAmlRatingT = null;
    if (dbName.equals(ContentName.onDay))
      customerAmlRatingT = customerAmlRatingReposDay.findByCustId(customerAmlRating.getCustId());
    else if (dbName.equals(ContentName.onMon))
      customerAmlRatingT = customerAmlRatingReposMon.findByCustId(customerAmlRating.getCustId());
    else if (dbName.equals(ContentName.onHist))
      customerAmlRatingT = customerAmlRatingReposHist.findByCustId(customerAmlRating.getCustId());
    else 
      customerAmlRatingT = customerAmlRatingRepos.findByCustId(customerAmlRating.getCustId());
    return customerAmlRatingT.isPresent() ? customerAmlRatingT.get() : null;
  }

  @Override
  public CustomerAmlRating insert(CustomerAmlRating customerAmlRating, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + customerAmlRating.getCustId());
    if (this.findById(customerAmlRating.getCustId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      customerAmlRating.setCreateEmpNo(empNot);

    if(customerAmlRating.getLastUpdateEmpNo() == null || customerAmlRating.getLastUpdateEmpNo().isEmpty())
      customerAmlRating.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return customerAmlRatingReposDay.saveAndFlush(customerAmlRating);	
    else if (dbName.equals(ContentName.onMon))
      return customerAmlRatingReposMon.saveAndFlush(customerAmlRating);
    else if (dbName.equals(ContentName.onHist))
      return customerAmlRatingReposHist.saveAndFlush(customerAmlRating);
    else 
    return customerAmlRatingRepos.saveAndFlush(customerAmlRating);
  }

  @Override
  public CustomerAmlRating update(CustomerAmlRating customerAmlRating, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + customerAmlRating.getCustId());
    if (!empNot.isEmpty())
      customerAmlRating.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return customerAmlRatingReposDay.saveAndFlush(customerAmlRating);	
    else if (dbName.equals(ContentName.onMon))
      return customerAmlRatingReposMon.saveAndFlush(customerAmlRating);
    else if (dbName.equals(ContentName.onHist))
      return customerAmlRatingReposHist.saveAndFlush(customerAmlRating);
    else 
    return customerAmlRatingRepos.saveAndFlush(customerAmlRating);
  }

  @Override
  public CustomerAmlRating update2(CustomerAmlRating customerAmlRating, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + customerAmlRating.getCustId());
    if (!empNot.isEmpty())
      customerAmlRating.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      customerAmlRatingReposDay.saveAndFlush(customerAmlRating);	
    else if (dbName.equals(ContentName.onMon))
      customerAmlRatingReposMon.saveAndFlush(customerAmlRating);
    else if (dbName.equals(ContentName.onHist))
        customerAmlRatingReposHist.saveAndFlush(customerAmlRating);
    else 
      customerAmlRatingRepos.saveAndFlush(customerAmlRating);	
    return this.findById(customerAmlRating.getCustId());
  }

  @Override
  public void delete(CustomerAmlRating customerAmlRating, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + customerAmlRating.getCustId());
    if (dbName.equals(ContentName.onDay)) {
      customerAmlRatingReposDay.delete(customerAmlRating);	
      customerAmlRatingReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      customerAmlRatingReposMon.delete(customerAmlRating);	
      customerAmlRatingReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      customerAmlRatingReposHist.delete(customerAmlRating);
      customerAmlRatingReposHist.flush();
    }
    else {
      customerAmlRatingRepos.delete(customerAmlRating);
      customerAmlRatingRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CustomerAmlRating> customerAmlRating, TitaVo... titaVo) throws DBException {
    if (customerAmlRating == null || customerAmlRating.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (CustomerAmlRating t : customerAmlRating){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      customerAmlRating = customerAmlRatingReposDay.saveAll(customerAmlRating);	
      customerAmlRatingReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      customerAmlRating = customerAmlRatingReposMon.saveAll(customerAmlRating);	
      customerAmlRatingReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      customerAmlRating = customerAmlRatingReposHist.saveAll(customerAmlRating);
      customerAmlRatingReposHist.flush();
    }
    else {
      customerAmlRating = customerAmlRatingRepos.saveAll(customerAmlRating);
      customerAmlRatingRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CustomerAmlRating> customerAmlRating, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (customerAmlRating == null || customerAmlRating.size() == 0)
      throw new DBException(6);

    for (CustomerAmlRating t : customerAmlRating) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      customerAmlRating = customerAmlRatingReposDay.saveAll(customerAmlRating);	
      customerAmlRatingReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      customerAmlRating = customerAmlRatingReposMon.saveAll(customerAmlRating);	
      customerAmlRatingReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      customerAmlRating = customerAmlRatingReposHist.saveAll(customerAmlRating);
      customerAmlRatingReposHist.flush();
    }
    else {
      customerAmlRating = customerAmlRatingRepos.saveAll(customerAmlRating);
      customerAmlRatingRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CustomerAmlRating> customerAmlRating, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (customerAmlRating == null || customerAmlRating.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      customerAmlRatingReposDay.deleteAll(customerAmlRating);	
      customerAmlRatingReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      customerAmlRatingReposMon.deleteAll(customerAmlRating);	
      customerAmlRatingReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      customerAmlRatingReposHist.deleteAll(customerAmlRating);
      customerAmlRatingReposHist.flush();
    }
    else {
      customerAmlRatingRepos.deleteAll(customerAmlRating);
      customerAmlRatingRepos.flush();
    }
  }

}
