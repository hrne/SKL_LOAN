package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ440;
import com.st1.itx.db.domain.JcicZ440Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ440RepositoryMon extends JpaRepository<JcicZ440, JcicZ440Id> {

	// CustId=
	public Slice<JcicZ440> findAllByCustIdIsOrderByCustIdAscApplyDateDesc(String custId_0, Pageable pageable);

	// ApplyDate=
	public Slice<JcicZ440> findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(int applyDate_0, Pageable pageable);

	// CustId= , AND ApplyDate=
	public Slice<JcicZ440> findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(String custId_0, int applyDate_1, Pageable pageable);

	// SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode=
	public Slice<JcicZ440> findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3,
			Pageable pageable);

	// Ukey=
	public Optional<JcicZ440> findTopByUkeyIs(String ukey_0);

	// SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode=
	public Optional<JcicZ440> findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<JcicZ440> findByJcicZ440Id(JcicZ440Id jcicZ440Id);

}
