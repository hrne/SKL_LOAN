package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicRel;
import com.st1.itx.db.domain.JcicRelId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicRelRepositoryMon extends JpaRepository<JcicRel, JcicRelId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<JcicRel> findByJcicRelId(JcicRelId jcicRelId);

	// (每月日終批次)維護 JcicRel 聯徵授信「同一關係企業及集團企業」資料報送檔
	@Procedure(value = "\"Usp_L8_JcicRel_Upd\"")
	public void uspL8JcicrelUpd(int TBSDYF, String EmpNo);

}
