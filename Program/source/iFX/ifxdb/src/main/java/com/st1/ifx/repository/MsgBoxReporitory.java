package com.st1.ifx.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.st1.ifx.domain.MsgBox;

public interface MsgBoxReporitory extends JpaRepository<MsgBox, Long> {
	// 先測試效能,如果有問題可能需要增加DB2欄位Version 之類的實現樂觀鎖
	// 20160325 只要丟一台就好,ap會互相找,故關閉lock
	// @Lock(LockModeType.PESSIMISTIC_WRITE )
	MsgBox findByBrnoAndTlrnoAndMsgno(String brno, String tlrno, String msgno);
}
