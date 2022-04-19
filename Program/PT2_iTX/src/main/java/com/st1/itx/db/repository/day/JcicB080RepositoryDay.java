package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicB080;
import com.st1.itx.db.domain.JcicB080Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB080RepositoryDay extends JpaRepository<JcicB080, JcicB080Id> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<JcicB080> findByJcicB080Id(JcicB080Id jcicB080Id);

	// (月底日日終批次)維護 JcicB080 每月聯徵授信額度資料檔
	@Procedure(value = "\"Usp_L8_JcicB080_Upd\"")
	public void uspL8Jcicb080Upd(int TBSDYF, String EmpNo);

}
