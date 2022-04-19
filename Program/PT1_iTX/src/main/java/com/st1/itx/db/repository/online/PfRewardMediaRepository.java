package com.st1.itx.db.repository.online;

import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.PfRewardMedia;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfRewardMediaRepository extends JpaRepository<PfRewardMedia, Long> {

	// WorkMonth = ,AND BonusType ^i ,AND MediaFg =
	public Slice<PfRewardMedia> findAllByWorkMonthIsAndBonusTypeInAndMediaFgIs(int workMonth_0, List<Integer> bonusType_1, int mediaFg_2, Pageable pageable);

	// CustNo = ,AND FacmNo =
	public Slice<PfRewardMedia> findAllByCustNoIsAndFacmNoIsOrderByPerfDateAsc(int custNo_0, int facmNo_1, Pageable pageable);

	// CustNo = ,AND FacmNo = ,AND BormNo = ,AND BonusType =
	public Optional<PfRewardMedia> findTopByCustNoIsAndFacmNoIsAndBormNoIsAndBonusTypeIs(int custNo_0, int facmNo_1, int bormNo_2, int bonusType_3);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<PfRewardMedia> findByBonusNo(Long bonusNo);

}
