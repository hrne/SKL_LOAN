package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.Ias39Loan34Data;
import com.st1.itx.db.domain.Ias39Loan34DataId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias39Loan34DataRepositoryHist extends JpaRepository<Ias39Loan34Data, Ias39Loan34DataId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<Ias39Loan34Data> findByIas39Loan34DataId(Ias39Loan34DataId ias39Loan34DataId);

	// (月底日日終批次)維護 Ias39Loan34Data 聯徵IAS39放款34號公報資料檔
	@Procedure(value = "\"Usp_L7_Ias39Loan34Data_Upd\"")
	public void uspL7Ias39loan34dataUpd(int TBSDYF, String EmpNo);

}
