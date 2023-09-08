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
import com.st1.itx.db.domain.ForeclosureFee;
import com.st1.itx.db.repository.online.ForeclosureFeeRepository;
import com.st1.itx.db.repository.day.ForeclosureFeeRepositoryDay;
import com.st1.itx.db.repository.mon.ForeclosureFeeRepositoryMon;
import com.st1.itx.db.repository.hist.ForeclosureFeeRepositoryHist;
import com.st1.itx.db.service.ForeclosureFeeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("foreclosureFeeService")
@Repository
public class ForeclosureFeeServiceImpl extends ASpringJpaParm implements ForeclosureFeeService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private ForeclosureFeeRepository foreclosureFeeRepos;

  @Autowired
  private ForeclosureFeeRepositoryDay foreclosureFeeReposDay;

  @Autowired
  private ForeclosureFeeRepositoryMon foreclosureFeeReposMon;

  @Autowired
  private ForeclosureFeeRepositoryHist foreclosureFeeReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(foreclosureFeeRepos);
    org.junit.Assert.assertNotNull(foreclosureFeeReposDay);
    org.junit.Assert.assertNotNull(foreclosureFeeReposMon);
    org.junit.Assert.assertNotNull(foreclosureFeeReposHist);
  }

  @Override
  public ForeclosureFee findById(int recordNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + recordNo);
    Optional<ForeclosureFee> foreclosureFee = null;
    if (dbName.equals(ContentName.onDay))
      foreclosureFee = foreclosureFeeReposDay.findById(recordNo);
    else if (dbName.equals(ContentName.onMon))
      foreclosureFee = foreclosureFeeReposMon.findById(recordNo);
    else if (dbName.equals(ContentName.onHist))
      foreclosureFee = foreclosureFeeReposHist.findById(recordNo);
    else 
      foreclosureFee = foreclosureFeeRepos.findById(recordNo);
    ForeclosureFee obj = foreclosureFee.isPresent() ? foreclosureFee.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<ForeclosureFee> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ForeclosureFee> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "RecordNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "RecordNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = foreclosureFeeReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = foreclosureFeeReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = foreclosureFeeReposHist.findAll(pageable);
    else 
      slice = foreclosureFeeRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ForeclosureFee> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ForeclosureFee> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custNoEq " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = foreclosureFeeReposDay.findAllByCustNoIsOrderByCustNoAscReceiveDateAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = foreclosureFeeReposMon.findAllByCustNoIsOrderByCustNoAscReceiveDateAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = foreclosureFeeReposHist.findAllByCustNoIsOrderByCustNoAscReceiveDateAsc(custNo_0, pageable);
    else 
      slice = foreclosureFeeRepos.findAllByCustNoIsOrderByCustNoAscReceiveDateAsc(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ForeclosureFee> receiveDateBetween(int receiveDate_0, int receiveDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ForeclosureFee> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("receiveDateBetween " + dbName + " : " + "receiveDate_0 : " + receiveDate_0 + " receiveDate_1 : " +  receiveDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = foreclosureFeeReposDay.findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualOrderByCustNoAscReceiveDateAsc(receiveDate_0, receiveDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = foreclosureFeeReposMon.findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualOrderByCustNoAscReceiveDateAsc(receiveDate_0, receiveDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = foreclosureFeeReposHist.findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualOrderByCustNoAscReceiveDateAsc(receiveDate_0, receiveDate_1, pageable);
    else 
      slice = foreclosureFeeRepos.findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualOrderByCustNoAscReceiveDateAsc(receiveDate_0, receiveDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ForeclosureFee findRecordNoFirst(int recordNo_0, int recordNo_1, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findRecordNoFirst " + dbName + " : " + "recordNo_0 : " + recordNo_0 + " recordNo_1 : " +  recordNo_1);
    Optional<ForeclosureFee> foreclosureFeeT = null;
    if (dbName.equals(ContentName.onDay))
      foreclosureFeeT = foreclosureFeeReposDay.findTopByRecordNoGreaterThanEqualAndRecordNoLessThanEqualOrderByRecordNoDesc(recordNo_0, recordNo_1);
    else if (dbName.equals(ContentName.onMon))
      foreclosureFeeT = foreclosureFeeReposMon.findTopByRecordNoGreaterThanEqualAndRecordNoLessThanEqualOrderByRecordNoDesc(recordNo_0, recordNo_1);
    else if (dbName.equals(ContentName.onHist))
      foreclosureFeeT = foreclosureFeeReposHist.findTopByRecordNoGreaterThanEqualAndRecordNoLessThanEqualOrderByRecordNoDesc(recordNo_0, recordNo_1);
    else 
      foreclosureFeeT = foreclosureFeeRepos.findTopByRecordNoGreaterThanEqualAndRecordNoLessThanEqualOrderByRecordNoDesc(recordNo_0, recordNo_1);

    return foreclosureFeeT.isPresent() ? foreclosureFeeT.get() : null;
  }

  @Override
  public Slice<ForeclosureFee> selectForL2078(int receiveDate_0, int receiveDate_1, int custNo_2, int custNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ForeclosureFee> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectForL2078 " + dbName + " : " + "receiveDate_0 : " + receiveDate_0 + " receiveDate_1 : " +  receiveDate_1 + " custNo_2 : " +  custNo_2 + " custNo_3 : " +  custNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = foreclosureFeeReposDay.findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscReceiveDateAsc(receiveDate_0, receiveDate_1, custNo_2, custNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = foreclosureFeeReposMon.findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscReceiveDateAsc(receiveDate_0, receiveDate_1, custNo_2, custNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = foreclosureFeeReposHist.findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscReceiveDateAsc(receiveDate_0, receiveDate_1, custNo_2, custNo_3, pageable);
    else 
      slice = foreclosureFeeRepos.findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscReceiveDateAsc(receiveDate_0, receiveDate_1, custNo_2, custNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ForeclosureFee> openAcDateBetween(int openAcDate_0, int openAcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ForeclosureFee> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("openAcDateBetween " + dbName + " : " + "openAcDate_0 : " + openAcDate_0 + " openAcDate_1 : " +  openAcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = foreclosureFeeReposDay.findAllByOpenAcDateGreaterThanEqualAndOpenAcDateLessThanEqualOrderByCustNoAscReceiveDateAsc(openAcDate_0, openAcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = foreclosureFeeReposMon.findAllByOpenAcDateGreaterThanEqualAndOpenAcDateLessThanEqualOrderByCustNoAscReceiveDateAsc(openAcDate_0, openAcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = foreclosureFeeReposHist.findAllByOpenAcDateGreaterThanEqualAndOpenAcDateLessThanEqualOrderByCustNoAscReceiveDateAsc(openAcDate_0, openAcDate_1, pageable);
    else 
      slice = foreclosureFeeRepos.findAllByOpenAcDateGreaterThanEqualAndOpenAcDateLessThanEqualOrderByCustNoAscReceiveDateAsc(openAcDate_0, openAcDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ForeclosureFee> closeDateBetween(int closeDate_0, int closeDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ForeclosureFee> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("closeDateBetween " + dbName + " : " + "closeDate_0 : " + closeDate_0 + " closeDate_1 : " +  closeDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = foreclosureFeeReposDay.findAllByCloseDateGreaterThanEqualAndCloseDateLessThanEqualOrderByCustNoAscReceiveDateAsc(closeDate_0, closeDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = foreclosureFeeReposMon.findAllByCloseDateGreaterThanEqualAndCloseDateLessThanEqualOrderByCustNoAscReceiveDateAsc(closeDate_0, closeDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = foreclosureFeeReposHist.findAllByCloseDateGreaterThanEqualAndCloseDateLessThanEqualOrderByCustNoAscReceiveDateAsc(closeDate_0, closeDate_1, pageable);
    else 
      slice = foreclosureFeeRepos.findAllByCloseDateGreaterThanEqualAndCloseDateLessThanEqualOrderByCustNoAscReceiveDateAsc(closeDate_0, closeDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ForeclosureFee> overdueDateBetween(int overdueDate_0, int overdueDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ForeclosureFee> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("overdueDateBetween " + dbName + " : " + "overdueDate_0 : " + overdueDate_0 + " overdueDate_1 : " +  overdueDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = foreclosureFeeReposDay.findAllByOverdueDateGreaterThanEqualAndOverdueDateLessThanEqualOrderByCustNoAscReceiveDateAsc(overdueDate_0, overdueDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = foreclosureFeeReposMon.findAllByOverdueDateGreaterThanEqualAndOverdueDateLessThanEqualOrderByCustNoAscReceiveDateAsc(overdueDate_0, overdueDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = foreclosureFeeReposHist.findAllByOverdueDateGreaterThanEqualAndOverdueDateLessThanEqualOrderByCustNoAscReceiveDateAsc(overdueDate_0, overdueDate_1, pageable);
    else 
      slice = foreclosureFeeRepos.findAllByOverdueDateGreaterThanEqualAndOverdueDateLessThanEqualOrderByCustNoAscReceiveDateAsc(overdueDate_0, overdueDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ForeclosureFee> selectForL2R32(int custNo_0, int facmNo_1, int closeDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ForeclosureFee> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectForL2R32 " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " closeDate_2 : " +  closeDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = foreclosureFeeReposDay.findAllByCustNoIsAndFacmNoIsAndCloseDateIsOrderByRecordNoAsc(custNo_0, facmNo_1, closeDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = foreclosureFeeReposMon.findAllByCustNoIsAndFacmNoIsAndCloseDateIsOrderByRecordNoAsc(custNo_0, facmNo_1, closeDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = foreclosureFeeReposHist.findAllByCustNoIsAndFacmNoIsAndCloseDateIsOrderByRecordNoAsc(custNo_0, facmNo_1, closeDate_2, pageable);
    else 
      slice = foreclosureFeeRepos.findAllByCustNoIsAndFacmNoIsAndCloseDateIsOrderByRecordNoAsc(custNo_0, facmNo_1, closeDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<ForeclosureFee> receiveDatecloseZero(int receiveDate_0, int receiveDate_1, int closeDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ForeclosureFee> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("receiveDatecloseZero " + dbName + " : " + "receiveDate_0 : " + receiveDate_0 + " receiveDate_1 : " +  receiveDate_1 + " closeDate_2 : " +  closeDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = foreclosureFeeReposDay.findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualAndCloseDateIsOrderByCustNoAscReceiveDateAsc(receiveDate_0, receiveDate_1, closeDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = foreclosureFeeReposMon.findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualAndCloseDateIsOrderByCustNoAscReceiveDateAsc(receiveDate_0, receiveDate_1, closeDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = foreclosureFeeReposHist.findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualAndCloseDateIsOrderByCustNoAscReceiveDateAsc(receiveDate_0, receiveDate_1, closeDate_2, pageable);
    else 
      slice = foreclosureFeeRepos.findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualAndCloseDateIsOrderByCustNoAscReceiveDateAsc(receiveDate_0, receiveDate_1, closeDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ForeclosureFee holdById(int recordNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + recordNo);
    Optional<ForeclosureFee> foreclosureFee = null;
    if (dbName.equals(ContentName.onDay))
      foreclosureFee = foreclosureFeeReposDay.findByRecordNo(recordNo);
    else if (dbName.equals(ContentName.onMon))
      foreclosureFee = foreclosureFeeReposMon.findByRecordNo(recordNo);
    else if (dbName.equals(ContentName.onHist))
      foreclosureFee = foreclosureFeeReposHist.findByRecordNo(recordNo);
    else 
      foreclosureFee = foreclosureFeeRepos.findByRecordNo(recordNo);
    return foreclosureFee.isPresent() ? foreclosureFee.get() : null;
  }

  @Override
  public ForeclosureFee holdById(ForeclosureFee foreclosureFee, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + foreclosureFee.getRecordNo());
    Optional<ForeclosureFee> foreclosureFeeT = null;
    if (dbName.equals(ContentName.onDay))
      foreclosureFeeT = foreclosureFeeReposDay.findByRecordNo(foreclosureFee.getRecordNo());
    else if (dbName.equals(ContentName.onMon))
      foreclosureFeeT = foreclosureFeeReposMon.findByRecordNo(foreclosureFee.getRecordNo());
    else if (dbName.equals(ContentName.onHist))
      foreclosureFeeT = foreclosureFeeReposHist.findByRecordNo(foreclosureFee.getRecordNo());
    else 
      foreclosureFeeT = foreclosureFeeRepos.findByRecordNo(foreclosureFee.getRecordNo());
    return foreclosureFeeT.isPresent() ? foreclosureFeeT.get() : null;
  }

  @Override
  public ForeclosureFee insert(ForeclosureFee foreclosureFee, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + foreclosureFee.getRecordNo());
    if (this.findById(foreclosureFee.getRecordNo(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      foreclosureFee.setCreateEmpNo(empNot);

    if(foreclosureFee.getLastUpdateEmpNo() == null || foreclosureFee.getLastUpdateEmpNo().isEmpty())
      foreclosureFee.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return foreclosureFeeReposDay.saveAndFlush(foreclosureFee);	
    else if (dbName.equals(ContentName.onMon))
      return foreclosureFeeReposMon.saveAndFlush(foreclosureFee);
    else if (dbName.equals(ContentName.onHist))
      return foreclosureFeeReposHist.saveAndFlush(foreclosureFee);
    else 
    return foreclosureFeeRepos.saveAndFlush(foreclosureFee);
  }

  @Override
  public ForeclosureFee update(ForeclosureFee foreclosureFee, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + foreclosureFee.getRecordNo());
    if (!empNot.isEmpty())
      foreclosureFee.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return foreclosureFeeReposDay.saveAndFlush(foreclosureFee);	
    else if (dbName.equals(ContentName.onMon))
      return foreclosureFeeReposMon.saveAndFlush(foreclosureFee);
    else if (dbName.equals(ContentName.onHist))
      return foreclosureFeeReposHist.saveAndFlush(foreclosureFee);
    else 
    return foreclosureFeeRepos.saveAndFlush(foreclosureFee);
  }

  @Override
  public ForeclosureFee update2(ForeclosureFee foreclosureFee, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + foreclosureFee.getRecordNo());
    if (!empNot.isEmpty())
      foreclosureFee.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      foreclosureFeeReposDay.saveAndFlush(foreclosureFee);	
    else if (dbName.equals(ContentName.onMon))
      foreclosureFeeReposMon.saveAndFlush(foreclosureFee);
    else if (dbName.equals(ContentName.onHist))
        foreclosureFeeReposHist.saveAndFlush(foreclosureFee);
    else 
      foreclosureFeeRepos.saveAndFlush(foreclosureFee);	
    return this.findById(foreclosureFee.getRecordNo());
  }

  @Override
  public void delete(ForeclosureFee foreclosureFee, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + foreclosureFee.getRecordNo());
    if (dbName.equals(ContentName.onDay)) {
      foreclosureFeeReposDay.delete(foreclosureFee);	
      foreclosureFeeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      foreclosureFeeReposMon.delete(foreclosureFee);	
      foreclosureFeeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      foreclosureFeeReposHist.delete(foreclosureFee);
      foreclosureFeeReposHist.flush();
    }
    else {
      foreclosureFeeRepos.delete(foreclosureFee);
      foreclosureFeeRepos.flush();
    }
   }

  @Override
  public void insertAll(List<ForeclosureFee> foreclosureFee, TitaVo... titaVo) throws DBException {
    if (foreclosureFee == null || foreclosureFee.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (ForeclosureFee t : foreclosureFee){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      foreclosureFee = foreclosureFeeReposDay.saveAll(foreclosureFee);	
      foreclosureFeeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      foreclosureFee = foreclosureFeeReposMon.saveAll(foreclosureFee);	
      foreclosureFeeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      foreclosureFee = foreclosureFeeReposHist.saveAll(foreclosureFee);
      foreclosureFeeReposHist.flush();
    }
    else {
      foreclosureFee = foreclosureFeeRepos.saveAll(foreclosureFee);
      foreclosureFeeRepos.flush();
    }
    }

  @Override
  public void updateAll(List<ForeclosureFee> foreclosureFee, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (foreclosureFee == null || foreclosureFee.size() == 0)
      throw new DBException(6);

    for (ForeclosureFee t : foreclosureFee) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      foreclosureFee = foreclosureFeeReposDay.saveAll(foreclosureFee);	
      foreclosureFeeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      foreclosureFee = foreclosureFeeReposMon.saveAll(foreclosureFee);	
      foreclosureFeeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      foreclosureFee = foreclosureFeeReposHist.saveAll(foreclosureFee);
      foreclosureFeeReposHist.flush();
    }
    else {
      foreclosureFee = foreclosureFeeRepos.saveAll(foreclosureFee);
      foreclosureFeeRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<ForeclosureFee> foreclosureFee, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (foreclosureFee == null || foreclosureFee.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      foreclosureFeeReposDay.deleteAll(foreclosureFee);	
      foreclosureFeeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      foreclosureFeeReposMon.deleteAll(foreclosureFee);	
      foreclosureFeeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      foreclosureFeeReposHist.deleteAll(foreclosureFee);
      foreclosureFeeReposHist.flush();
    }
    else {
      foreclosureFeeRepos.deleteAll(foreclosureFee);
      foreclosureFeeRepos.flush();
    }
  }

}
