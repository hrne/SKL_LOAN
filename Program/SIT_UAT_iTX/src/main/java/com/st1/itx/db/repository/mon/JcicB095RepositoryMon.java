package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicB095;
import com.st1.itx.db.domain.JcicB095Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB095RepositoryMon extends JpaRepository<JcicB095, JcicB095Id> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<JcicB095> findByJcicB095Id(JcicB095Id jcicB095Id);

	// (每月日終批次)維護 JcicB095 每月聯徵不動產擔保品明細-建號附加檔
	@Procedure(value = "\"Usp_L8_JcicB095_Upd\"")
	public void uspL8Jcicb095Upd(int TBSDYF, String EmpNo);

}
