package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdBankId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdBankRepositoryHist extends JpaRepository<CdBank, CdBankId> {

	// BankCode %
	public Slice<CdBank> findAllByBankCodeLikeOrderByBankCodeAscBranchCodeAsc(String bankCode_0, Pageable pageable);

	// BankCode % ,AND BranchCode %
	public Slice<CdBank> findAllByBankCodeLikeAndBranchCodeLikeOrderByBankCodeAscBranchCodeAsc(String bankCode_0, String branchCode_1, Pageable pageable);

	// BankCode % ,AND BranchCode % ,AND BankItem %
	public Slice<CdBank> findAllByBankCodeLikeAndBranchCodeLikeAndBankItemLikeOrderByBankCodeAscBranchCodeAsc(String bankCode_0, String branchCode_1, String bankItem_2, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<CdBank> findByCdBankId(CdBankId cdBankId);

}
