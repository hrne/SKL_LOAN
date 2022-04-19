package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxHoliday;
import com.st1.itx.db.domain.TxHolidayId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxHolidayRepositoryHist extends JpaRepository<TxHoliday, TxHolidayId> {

	// Country = ,AND Holiday >= ,AND Holiday <=
	public Slice<TxHoliday> findAllByCountryIsAndHolidayGreaterThanEqualAndHolidayLessThanEqualOrderByHolidayAsc(String country_0, int holiday_1, int holiday_2, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<TxHoliday> findByTxHolidayId(TxHolidayId txHolidayId);

}
