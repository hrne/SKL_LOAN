package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ040Log;
import com.st1.itx.db.domain.JcicZ040LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ040LogRepositoryDay extends JpaRepository<JcicZ040Log, JcicZ040LogId> {

	// Ukey=
	public Optional<JcicZ040Log> findTopByUkeyIsOrderByCreateDateDesc(String ukey_0);

	// Ukey=
	public Slice<JcicZ040Log> findAllByUkeyIsOrderByCreateDateDesc(String ukey_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<JcicZ040Log> findByJcicZ040LogId(JcicZ040LogId jcicZ040LogId);

}
