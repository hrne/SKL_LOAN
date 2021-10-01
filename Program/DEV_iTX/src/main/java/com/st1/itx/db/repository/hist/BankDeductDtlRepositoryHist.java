package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.BankDeductDtl;
import com.st1.itx.db.domain.BankDeductDtlId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BankDeductDtlRepositoryHist extends JpaRepository<BankDeductDtl, BankDeductDtlId> {

  // CustNo = ,AND EntryDate >= ,AND EntryDate <= 
  public Slice<BankDeductDtl> findAllByCustNoIsAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByEntryDateAsc(int custNo_0, int entryDate_1, int entryDate_2, Pageable pageable);

  // CustNo = 
  public Slice<BankDeductDtl> findAllByCustNoIsOrderByEntryDateAsc(int custNo_0, Pageable pageable);

  // EntryDate >= ,AND EntryDate <= 
  public Slice<BankDeductDtl> findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscFacmNoAscPayIntDateAscRepayTypeDesc(int entryDate_0, int entryDate_1, Pageable pageable);

  // MediaDate = , AND MediaKind = , AND MediaSeq = 
  public Slice<BankDeductDtl> findAllByMediaDateIsAndMediaKindIsAndMediaSeqIsOrderByMediaKindAscMediaSeqAsc(int mediaDate_0, String mediaKind_1, int mediaSeq_2, Pageable pageable);

  // RepayBank = , AND EntryDate >= ,AND EntryDate <= 
  public Slice<BankDeductDtl> findAllByRepayBankIsAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscFacmNoAscPayIntDateAscRepayTypeDesc(String repayBank_0, int entryDate_1, int entryDate_2, Pageable pageable);

  // EntryDate >= ,AND EntryDate <= ,AND RepayType =
  public Slice<BankDeductDtl> findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndRepayTypeIs(int entryDate_0, int entryDate_1, int repayType_2, Pageable pageable);

  // EntryDate = ,AND CustNo = ,AND FacmNo = ,AND RepayType = ,AND PayIntDate =
  public Slice<BankDeductDtl> findAllByEntryDateIsAndCustNoIsAndFacmNoIsAndRepayTypeIsAndPayIntDateIs(int entryDate_0, int custNo_1, int facmNo_2, int repayType_3, int payIntDate_4, Pageable pageable);

  // EntryDate >= ,AND EntryDate <= ,AND RepayBank = ,AND RepayType =
  public Slice<BankDeductDtl> findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndRepayBankIsAndRepayTypeIs(int entryDate_0, int entryDate_1, String repayBank_2, int repayType_3, Pageable pageable);

  // MediaDate >= , AND MediaDate <= 
  public Slice<BankDeductDtl> findAllByMediaDateGreaterThanEqualAndMediaDateLessThanEqual(int mediaDate_0, int mediaDate_1, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND BormNo = ,AND RepayType = ,AND PayIntDate = 
  public Slice<BankDeductDtl> findAllByCustNoIsAndFacmNoIsAndBormNoIsAndRepayTypeIsAndPayIntDateIsOrderByEntryDateDesc(int custNo_0, int facmNo_1, int bormNo_2, int repayType_3, int payIntDate_4, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND BormNo = ,AND PrevIntDate =
  public Slice<BankDeductDtl> findAllByCustNoIsAndFacmNoIsAndBormNoIsAndPrevIntDateIsOrderByEntryDateDesc(int custNo_0, int facmNo_1, int bormNo_2, int prevIntDate_3, Pageable pageable);

  // EntryDate >= ,AND EntryDate <= , AND MediaKind = 
  public Optional<BankDeductDtl> findTopByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndMediaKindIsOrderByMediaSeqDesc(int entryDate_0, int entryDate_1, String mediaKind_2);

  // RepayBank <> , AND EntryDate >= ,AND EntryDate <= 
  public Slice<BankDeductDtl> findAllByRepayBankNotAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscFacmNoAscPayIntDateAscRepayTypeDesc(String repayBank_0, int entryDate_1, int entryDate_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<BankDeductDtl> findByBankDeductDtlId(BankDeductDtlId bankDeductDtlId);

}

