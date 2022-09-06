package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ450;
import com.st1.itx.db.domain.JcicZ450Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ450RepositoryDay extends JpaRepository<JcicZ450, JcicZ450Id> {

	// CustId=
	public Slice<JcicZ450> findAllByCustIdIsOrderByCustIdAscApplyDateDesc(String custId_0, Pageable pageable);

	// ApplyDate=
	public Slice<JcicZ450> findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(int applyDate_0, Pageable pageable);

	// CustId= , AND ApplyDate=
	public Slice<JcicZ450> findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(String custId_0, int applyDate_1, Pageable pageable);

	// SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode= , AND PayDate=
	public Slice<JcicZ450> findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndPayDateIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3,
			int payDate_4, Pageable pageable);

	// Ukey=
	public Optional<JcicZ450> findTopByUkeyIs(String ukey_0);

	// SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode= , AND PayDate=
	public Optional<JcicZ450> findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndPayDateIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3,
			int payDate_4);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<JcicZ450> findByJcicZ450Id(JcicZ450Id jcicZ450Id);

}
