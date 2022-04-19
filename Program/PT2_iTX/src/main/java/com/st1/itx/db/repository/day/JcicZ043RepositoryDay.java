package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ043;
import com.st1.itx.db.domain.JcicZ043Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ043RepositoryDay extends JpaRepository<JcicZ043, JcicZ043Id> {

	// CustId=
	public Slice<JcicZ043> findAllByCustIdIsOrderByCustIdAscRcDateDesc(String custId_0, Pageable pageable);

	// RcDate=
	public Slice<JcicZ043> findAllByRcDateIsOrderByCustIdAscRcDateDesc(int rcDate_0, Pageable pageable);

	// CustId= , AND RcDate=
	public Slice<JcicZ043> findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(String custId_0, int rcDate_1, Pageable pageable);

	// CustId= , AND RcDate= , AND SubmitKey= , AND MaxMainCode=
	public Slice<JcicZ043> findAllByCustIdIsAndRcDateIsAndSubmitKeyIsAndMaxMainCodeIsOrderByCustIdAscRcDateDesc(String custId_0, int rcDate_1, String submitKey_2, String maxMainCode_3,
			Pageable pageable);

	// SubmitKey= , AND CustId= , AND RcDate= , AND MaxMainCode= , AND Account=
	public Slice<JcicZ043> findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsAndAccountIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3,
			String account_4, Pageable pageable);

	// Ukey=
	public Optional<JcicZ043> findTopByUkeyIs(String ukey_0);

	// SubmitKey= , AND CustId= , AND RcDate= , AND MaxMainCode= , AND Account=
	public Optional<JcicZ043> findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsAndAccountIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3,
			String account_4);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<JcicZ043> findByJcicZ043Id(JcicZ043Id jcicZ043Id);

}
