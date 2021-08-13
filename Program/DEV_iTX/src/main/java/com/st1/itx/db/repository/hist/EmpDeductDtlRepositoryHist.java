package com.st1.itx.db.repository.hist;


import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.EmpDeductDtl;
import com.st1.itx.db.domain.EmpDeductDtlId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface EmpDeductDtlRepositoryHist extends JpaRepository<EmpDeductDtl, EmpDeductDtlId> {

  // MediaDate = , AND MediaKind = , AND MediaSeq = 
  public Optional<EmpDeductDtl> findTopByMediaDateIsAndMediaKindIsAndMediaSeqIs(int mediaDate_0, String mediaKind_1, int mediaSeq_2);

  // ErrMsg !, AND EntryDate >= , AND EntryDate <= , AND ProcCode ^i
  public Slice<EmpDeductDtl> findAllByErrMsgIsNullAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndProcCodeInOrderByEntryDateAscCustNoAscAchRepayCodeDesc(int entryDate_1, int entryDate_2, List<String> procCode_3, Pageable pageable);

  // MediaDate = , AND MediaKind = , AND MediaSeq = 
  public Slice<EmpDeductDtl> findAllByMediaDateIsAndMediaKindIsAndMediaSeqIs(int mediaDate_0, String mediaKind_1, int mediaSeq_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<EmpDeductDtl> findByEmpDeductDtlId(EmpDeductDtlId empDeductDtlId);

}

