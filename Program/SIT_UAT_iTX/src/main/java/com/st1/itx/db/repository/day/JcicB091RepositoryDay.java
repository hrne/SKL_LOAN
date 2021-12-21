package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicB091;
import com.st1.itx.db.domain.JcicB091Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB091RepositoryDay extends JpaRepository<JcicB091, JcicB091Id> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<JcicB091> findByJcicB091Id(JcicB091Id jcicB091Id);

	// (每月日終批次)維護 JcicB091 每月有價證券(股票除外)擔保品明細檔
	@Procedure(value = "\"Usp_L8_JcicB091_Upd\"")
	public void uspL8Jcicb091Upd(int TBSDYF, String EmpNo);

}
