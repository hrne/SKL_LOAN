package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.Ias34Cp;
import com.st1.itx.db.domain.Ias34CpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias34CpRepositoryDay extends JpaRepository<Ias34Cp, Ias34CpId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<Ias34Cp> findByIas34CpId(Ias34CpId ias34CpId);

	// (月底日日終批次)維護 IAS34 欄位清單C檔
	@Procedure(value = "\"Usp_L7_Ias34Cp_Upd\"")
	public void uspL7Ias34cpUpd(int TBSDYF, String EmpNo);

}
