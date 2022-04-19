package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicB680;
import com.st1.itx.db.domain.JcicB680Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB680Repository extends JpaRepository<JcicB680, JcicB680Id> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<JcicB680> findByJcicB680Id(JcicB680Id jcicB680Id);

	// (每月日終批次)維護 JcicB680 每月聯徵貸款餘額扣除擔保品鑑估值之金額資料檔
	@Procedure(value = "\"Usp_L8_JcicB680_Upd\"")
	public void uspL8Jcicb680Upd(int TBSDYF, String EmpNo);

}
