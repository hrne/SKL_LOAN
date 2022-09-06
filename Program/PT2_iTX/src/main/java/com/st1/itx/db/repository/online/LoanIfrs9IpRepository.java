package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIfrs9Ip;
import com.st1.itx.db.domain.LoanIfrs9IpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrs9IpRepository extends JpaRepository<LoanIfrs9Ip, LoanIfrs9IpId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<LoanIfrs9Ip> findByLoanIfrs9IpId(LoanIfrs9IpId loanIfrs9IpId);

	// (月底日日終批次)維護 LoanIfrsIp IFRS9欄位清單9
	@Procedure(value = "\"Usp_L7_LoanIfrs9Ip_Upd\"")
	public void uspL7Loanifrs9ipUpd(int TBSDYF, String EmpNo, int NewAcFg);

}
