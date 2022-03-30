package com.st1.ifx.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.st1.ifx.domain.Ticker;

public interface TickerReporitory extends JpaRepository<Ticker, Long> {

	// 先測試效能,如果有問題可能需要增加DB2欄位Version 之類的實現樂觀鎖
	// 20160325 只要丟一台就好,因為跑馬燈移除快取
	// @Lock(LockModeType.PESSIMISTIC_WRITE )
	Ticker findByBrnoAndDatedAndTickno(String brno, java.sql.Date date, String tickno);

	List<Ticker> findByBrnoAndStopTimeGreaterThanEqualAndStopTimeLessThanEqualOrderByIdAsc(String brno, Long starttime,
			Long stoptime);
}
