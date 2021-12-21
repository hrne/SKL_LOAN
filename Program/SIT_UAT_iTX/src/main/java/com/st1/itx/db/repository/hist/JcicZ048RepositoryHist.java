package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ048;
import com.st1.itx.db.domain.JcicZ048Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ048RepositoryHist extends JpaRepository<JcicZ048, JcicZ048Id> {

	// CustId=
	public Slice<JcicZ048> findAllByCustIdIsOrderByCustIdAscRcDateDesc(String custId_0, Pageable pageable);

	// RcDate=
	public Slice<JcicZ048> findAllByRcDateIsOrderByCustIdAscRcDateDesc(int rcDate_0, Pageable pageable);

	// CustId= , AND RcDate=
	public Slice<JcicZ048> findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(String custId_0, int rcDate_1, Pageable pageable);

	// RcDate>= , AND RcDate<=
	public Slice<JcicZ048> findAllByRcDateGreaterThanEqualAndRcDateLessThanEqualOrderByCustIdAscRcDateDesc(int rcDate_0, int rcDate_1, Pageable pageable);

	// CustId= , AND RcDate>= , AND RcDate<=
	public Slice<JcicZ048> findAllByCustIdIsAndRcDateGreaterThanEqualAndRcDateLessThanEqualOrderByCustIdAscRcDateDesc(String custId_0, int rcDate_1, int rcDate_2, Pageable pageable);

	// SubmitKey= , AND CustId= , AND RcDate=
	public Slice<JcicZ048> findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, Pageable pageable);

	// Ukey=
	public Optional<JcicZ048> findTopByUkeyIs(String ukey_0);

	// SubmitKey= , AND CustId= , AND RcDate=
	public Optional<JcicZ048> findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<JcicZ048> findByJcicZ048Id(JcicZ048Id jcicZ048Id);

}
