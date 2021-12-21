package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxAuthGroup;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAuthGroupRepositoryDay extends JpaRepository<TxAuthGroup, String> {

	// AuthNo %
	public Slice<TxAuthGroup> findAllByAuthNoLikeOrderByAuthNoAsc(String authNo_0, Pageable pageable);

	// BranchNo = ,AND LevelFg =
	public Slice<TxAuthGroup> findAllByBranchNoIsAndLevelFgIsOrderByAuthNoAsc(String branchNo_0, int levelFg_1, Pageable pageable);

	// BranchNo = ,AND AuthNo %
	public Slice<TxAuthGroup> findAllByBranchNoIsAndAuthNoLikeOrderByBranchNoAscAuthNoAsc(String branchNo_0, String authNo_1, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<TxAuthGroup> findByAuthNo(String authNo);

}
