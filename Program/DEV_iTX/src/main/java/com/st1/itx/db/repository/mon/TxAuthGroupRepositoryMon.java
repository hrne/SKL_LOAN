package com.st1.itx.db.repository.mon;

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
public interface TxAuthGroupRepositoryMon extends JpaRepository<TxAuthGroup, String> {

	// AuthNo % ,AND Status>= ,AND Status<=
	public Slice<TxAuthGroup> findAllByAuthNoLikeAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByAuthNoAsc(String authNo_0, int status_1, int status_2, Pageable pageable);

	// BranchNo = ,AND LevelFg =
	public Slice<TxAuthGroup> findAllByBranchNoIsAndLevelFgIsOrderByAuthNoAsc(String branchNo_0, int levelFg_1, Pageable pageable);

	// BranchNo = ,AND AuthNo % ,AND Status>= ,AND Status<=
	public Slice<TxAuthGroup> findAllByBranchNoIsAndAuthNoLikeAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByAuthNoAsc(String branchNo_0, String authNo_1, int status_2, int status_3,
			Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<TxAuthGroup> findByAuthNo(String authNo);

}
