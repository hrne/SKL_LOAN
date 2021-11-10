package com.st1.itx.db.repository.mon;


import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.MlaundryDetail;
import com.st1.itx.db.domain.MlaundryDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MlaundryDetailRepositoryMon extends JpaRepository<MlaundryDetail, MlaundryDetailId> {

  // EntryDate >= ,AND EntryDate <= ,AND Rational ^i
  public Slice<MlaundryDetail> findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndRationalInOrderByEntryDateAscCustNoAsc(int entryDate_0, int entryDate_1, List<String> rational_2, Pageable pageable);

  // EntryDate >= ,AND EntryDate <= ,
  public Slice<MlaundryDetail> findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByEntryDateAscCustNoAsc(int entryDate_0, int entryDate_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<MlaundryDetail> findByMlaundryDetailId(MlaundryDetailId mlaundryDetailId);

}

