package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacMainRepositoryMon extends JpaRepository<FacMain, FacMainId> {

  // CustNo >= ,AND CustNo <=  ,AND FacmNo >= ,AND FacmNo <= 
  public Slice<FacMain> findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(int custNo_0, int custNo_1, int facmNo_2, int facmNo_3, Pageable pageable);

  // ApplNo >= ,AND ApplNo <= ,AND FacmNo >= ,AND FacmNo <= ,AND ColSetFlag %
  public Slice<FacMain> findAllByApplNoGreaterThanEqualAndApplNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndColSetFlagLikeOrderByApplNoAscFacmNoAsc(int applNo_0, int applNo_1, int facmNo_2, int facmNo_3, String colSetFlag_4, Pageable pageable);

  // ApplNo =
  public Optional<FacMain> findTopByApplNoIs(int applNo_0);

  // CreditSysNo >= ,AND CreditSysNo <= ,AND FacmNo >= ,AND FacmNo <=
  public Slice<FacMain> findAllByCreditSysNoGreaterThanEqualAndCreditSysNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCreditSysNoAscFacmNoAsc(int creditSysNo_0, int creditSysNo_1, int facmNo_2, int facmNo_3, Pageable pageable);

  // CreditSysNo >= ,AND CreditSysNo <= ,AND FacmNo >= ,AND FacmNo <= 
  public Optional<FacMain> findTopByCreditSysNoGreaterThanEqualAndCreditSysNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCreditSysNoAscFacmNoAsc(int creditSysNo_0, int creditSysNo_1, int facmNo_2, int facmNo_3);

  // CreditOfficer >= ,AND CreditOfficer <= ,AND FacmNo >= ,AND FacmNo <=
  public Slice<FacMain> findAllByCreditOfficerGreaterThanEqualAndCreditOfficerLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(String creditOfficer_0, String creditOfficer_1, int facmNo_2, int facmNo_3, Pageable pageable);

  // CustNo >= ,AND CustNo <= ,AND ColSetFlag %
  public Slice<FacMain> findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndColSetFlagLikeOrderByApplNoAscFacmNoAsc(int custNo_0, int custNo_1, String colSetFlag_2, Pageable pageable);

  // BusinessOfficer >= ,AND BusinessOfficer <= ,AND FacmNo >= ,AND FacmNo <=
  public Slice<FacMain> findAllByBusinessOfficerGreaterThanEqualAndBusinessOfficerLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(String businessOfficer_0, String businessOfficer_1, int facmNo_2, int facmNo_3, Pageable pageable);

  // CustNo = ,AND CreditSysNo =
  public Optional<FacMain> findTopByCustNoIsAndCreditSysNoIsOrderByCustNoAscFacmNoAsc(int custNo_0, int creditSysNo_1);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<FacMain> findByFacMainId(FacMainId facMainId);

}

