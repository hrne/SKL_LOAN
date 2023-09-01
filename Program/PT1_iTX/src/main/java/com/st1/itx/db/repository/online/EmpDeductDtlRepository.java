package com.st1.itx.db.repository.online;


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

import com.st1.itx.db.domain.EmpDeductDtl;
import com.st1.itx.db.domain.EmpDeductDtlId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface EmpDeductDtlRepository extends JpaRepository<EmpDeductDtl, EmpDeductDtlId> {

  // MediaDate = , AND MediaKind = , AND MediaSeq = 
  public Optional<EmpDeductDtl> findTopByMediaDateIsAndMediaKindIsAndMediaSeqIs(int mediaDate_0, String mediaKind_1, int mediaSeq_2);

  // EntryDate >= , AND EntryDate <= , AND ProcCode ^i
  public Slice<EmpDeductDtl> findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndProcCodeInOrderByEntryDateAscCustNoAscAchRepayCodeDesc(int entryDate_0, int entryDate_1, List<String> procCode_2, Pageable pageable);

  // MediaDate = , AND MediaKind = , AND MediaSeq = 
  public Slice<EmpDeductDtl> findAllByMediaDateIsAndMediaKindIsAndMediaSeqIs(int mediaDate_0, String mediaKind_1, int mediaSeq_2, Pageable pageable);

  // MediaDate = , AND MediaKind = 
  public Slice<EmpDeductDtl> findAllByMediaDateIsAndMediaKindIsOrderByEntryDateAscCustNoAscAchRepayCodeDesc(int mediaDate_0, String mediaKind_1, Pageable pageable);

  // Acdate = ,AND TitaTlrNo = ,AND TitaTxtNo =
  public Slice<EmpDeductDtl> findAllByAcdateIsAndTitaTlrNoIsAndTitaTxtNoIs(int acdate_0, String titaTlrNo_1, String titaTxtNo_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<EmpDeductDtl> findByEmpDeductDtlId(EmpDeductDtlId empDeductDtlId);

}

