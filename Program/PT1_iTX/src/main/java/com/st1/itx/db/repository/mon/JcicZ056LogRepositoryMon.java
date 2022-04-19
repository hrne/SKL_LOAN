package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ056Log;
import com.st1.itx.db.domain.JcicZ056LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ056LogRepositoryMon extends JpaRepository<JcicZ056Log, JcicZ056LogId> {

	// Ukey=
	public Optional<JcicZ056Log> findTopByUkeyIsOrderByCreateDateDesc(String ukey_0);

	// Ukey=
	public Slice<JcicZ056Log> findAllByUkeyIsOrderByCreateDateDesc(String ukey_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<JcicZ056Log> findByJcicZ056LogId(JcicZ056LogId jcicZ056LogId);

}
