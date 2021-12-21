package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TbJcicMu01;
import com.st1.itx.db.domain.TbJcicMu01Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TbJcicMu01RepositoryDay extends JpaRepository<TbJcicMu01, TbJcicMu01Id> {

	// EmpId=
	public Slice<TbJcicMu01> findAllByEmpIdIs(String empId_0, Pageable pageable);

	// DataDate=
	public Slice<TbJcicMu01> findAllByDataDateIs(int dataDate_0, Pageable pageable);

	// EmpId= , AND DataDate=
	public Slice<TbJcicMu01> findAllByEmpIdIsAndDataDateIs(String empId_0, int dataDate_1, Pageable pageable);

	// HeadOfficeCode= , AND BranchCode= , AND DataDate=
	public Slice<TbJcicMu01> findAllByHeadOfficeCodeIsAndBranchCodeIsAndDataDateIs(String headOfficeCode_0, String branchCode_1, int dataDate_2, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<TbJcicMu01> findByTbJcicMu01Id(TbJcicMu01Id tbJcicMu01Id);

}
