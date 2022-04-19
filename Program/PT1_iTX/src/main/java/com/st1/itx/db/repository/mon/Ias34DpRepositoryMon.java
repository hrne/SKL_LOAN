package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.Ias34Dp;
import com.st1.itx.db.domain.Ias34DpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias34DpRepositoryMon extends JpaRepository<Ias34Dp, Ias34DpId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<Ias34Dp> findByIas34DpId(Ias34DpId ias34DpId);

	// (月底日日終批次)維護 IAS34 欄位清單D檔
	@Procedure(value = "\"Usp_L7_Ias34Dp_Upd\"")
	public void uspL7Ias34dpUpd(int TBSDYF, String EmpNo, int NewAcFg);

}
