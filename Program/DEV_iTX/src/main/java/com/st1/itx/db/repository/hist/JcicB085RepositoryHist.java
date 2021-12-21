package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicB085;
import com.st1.itx.db.domain.JcicB085Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB085RepositoryHist extends JpaRepository<JcicB085, JcicB085Id> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<JcicB085> findByJcicB085Id(JcicB085Id jcicB085Id);

	// (每月日終批次)維護 JcicB085 聯徵帳號轉換資料檔
	@Procedure(value = "\"Usp_L8_JcicB085_Upd\"")
	public void uspL8Jcicb085Upd(int TBSDYF, String EmpNo);

}
