package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicB096;
import com.st1.itx.db.domain.JcicB096Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB096RepositoryDay extends JpaRepository<JcicB096, JcicB096Id> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<JcicB096> findByJcicB096Id(JcicB096Id jcicB096Id);

	// (每月日終批次)維護 JcicB096 每月聯徵不動產擔保品明細-地號附加檔
	@Procedure(value = "\"Usp_L8_JcicB096_Upd\"")
	public void uspL8Jcicb096Upd(int TBSDYF, String EmpNo);

}
