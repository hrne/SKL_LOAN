package com.st1.itx.db.repository.hist;


import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.MlaundryChkDtl;
import com.st1.itx.db.domain.MlaundryChkDtlId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MlaundryChkDtlRepositoryHist extends JpaRepository<MlaundryChkDtl, MlaundryChkDtlId> {

  // EntryDate >= ,AND EntryDate <= 
  public Slice<MlaundryChkDtl> findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByEntryDateAscFactorAscCustNoAscDtlSeqAsc(int entryDate_0, int entryDate_1, Pageable pageable);

  // EntryDate >= ,AND EntryDate <= ,AND Factor =
  public Slice<MlaundryChkDtl> findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndFactorIsOrderByEntryDateAscCustNoAscDtlSeqAsc(int entryDate_0, int entryDate_1, int factor_2, Pageable pageable);

  // EntryDate >= ,AND EntryDate <= ,AND Factor = , AND CustNo = 
  public Slice<MlaundryChkDtl> findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndFactorIsAndCustNoIsOrderByEntryDateAscDtlSeqAsc(int entryDate_0, int entryDate_1, int factor_2, int custNo_3, Pageable pageable);

  // DtlEntryDate >= ,AND DtlEntryDate <= 
  public Slice<MlaundryChkDtl> findAllByDtlEntryDateGreaterThanEqualAndDtlEntryDateLessThanEqualOrderByDtlEntryDateAscFactorAscCustNoAscDtlSeqAsc(int dtlEntryDate_0, int dtlEntryDate_1, Pageable pageable);

  // DtlEntryDate >= ,AND DtlEntryDate <= ,AND Factor =
  public Slice<MlaundryChkDtl> findAllByDtlEntryDateGreaterThanEqualAndDtlEntryDateLessThanEqualAndFactorIsOrderByDtlEntryDateAscCustNoAscDtlSeqAsc(int dtlEntryDate_0, int dtlEntryDate_1, int factor_2, Pageable pageable);

  // DtlEntryDate >= ,AND DtlEntryDate <= ,AND Factor = , AND CustNo = 
  public Slice<MlaundryChkDtl> findAllByDtlEntryDateGreaterThanEqualAndDtlEntryDateLessThanEqualAndFactorIsAndCustNoIsOrderByDtlEntryDateAscDtlSeqAsc(int dtlEntryDate_0, int dtlEntryDate_1, int factor_2, int custNo_3, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<MlaundryChkDtl> findByMlaundryChkDtlId(MlaundryChkDtlId mlaundryChkDtlId);

}

