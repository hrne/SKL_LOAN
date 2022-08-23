package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.InnDocRecord;
import com.st1.itx.db.domain.InnDocRecordId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface InnDocRecordRepositoryHist extends JpaRepository<InnDocRecord, InnDocRecordId> {

  // CustNo = ,AND ApplDate >= ,AND ApplDate <= ,AND UsageCode = ,AND ApplCode = 
  public Slice<InnDocRecord> findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualAndUsageCodeIsAndApplCodeIsOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(int custNo_0, int applDate_1, int applDate_2, String usageCode_3, String applCode_4, Pageable pageable);

  // CustNo = ,AND ApplDate >= ,AND ApplDate <= ,AND ApplCode = 
  public Slice<InnDocRecord> findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualAndApplCodeIsOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(int custNo_0, int applDate_1, int applDate_2, String applCode_3, Pageable pageable);

  // CustNo = ,AND ApplDate >= ,AND ApplDate <= ,AND UsageCode = 
  public Slice<InnDocRecord> findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualAndUsageCodeIsOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(int custNo_0, int applDate_1, int applDate_2, String usageCode_3, Pageable pageable);

  // CustNo = ,AND ApplDate >= ,AND ApplDate <= 
  public Slice<InnDocRecord> findAllByCustNoIsAndApplDateGreaterThanEqualAndApplDateLessThanEqualOrderByCustNoAscApplDateAscUsageCodeAscApplCodeAsc(int custNo_0, int applDate_1, int applDate_2, Pageable pageable);

  // ApplDate >= ,AND ApplDate <= ,AND ReturnDate = 
  public Slice<InnDocRecord> findAllByApplDateGreaterThanEqualAndApplDateLessThanEqualAndReturnDateIs(int applDate_0, int applDate_1, int returnDate_2, Pageable pageable);

  // ApplDate >= ,AND ApplDate <= ,AND UsageCode = 
  public Slice<InnDocRecord> findAllByApplDateGreaterThanEqualAndApplDateLessThanEqualAndUsageCodeIs(int applDate_0, int applDate_1, String usageCode_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<InnDocRecord> findByInnDocRecordId(InnDocRecordId innDocRecordId);

}

