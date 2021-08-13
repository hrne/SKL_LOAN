package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.Ias39LGD;
import com.st1.itx.db.domain.Ias39LGDId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias39LGDRepositoryHist extends JpaRepository<Ias39LGD, Ias39LGDId> {

  // Type = ,AND Date >= ,AND Date <= 
  public Slice<Ias39LGD> findAllByTypeIsAndDateGreaterThanEqualAndDateLessThanEqualOrderByDateAsc(String type_0, int date_1, int date_2, Pageable pageable);

  // Date >= ,AND Date <= ,AND Type >= ,AND Type <= 
  public Slice<Ias39LGD> findAllByDateGreaterThanEqualAndDateLessThanEqualAndTypeGreaterThanEqualAndTypeLessThanEqualOrderByDateAscTypeAsc(int date_0, int date_1, String type_2, String type_3, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<Ias39LGD> findByIas39LGDId(Ias39LGDId ias39LGDId);

}

