package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxToDoMain;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxToDoMainRepository extends JpaRepository<TxToDoMain, String> {

	// AutoFg = ,AND ExcuteTxcd =
	public Optional<TxToDoMain> findTopByAutoFgIsAndExcuteTxcdIs(String autoFg_0, String excuteTxcd_1);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<TxToDoMain> findByItemCode(String itemCode);

}
