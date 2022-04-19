package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ048Log;
import com.st1.itx.db.domain.JcicZ048LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ048LogRepositoryMon extends JpaRepository<JcicZ048Log, JcicZ048LogId> {

	// Ukey=
	public Optional<JcicZ048Log> findTopByUkeyIsOrderByCreateDateDesc(String ukey_0);

	// Ukey=
	public Slice<JcicZ048Log> findAllByUkeyIsOrderByCreateDateDesc(String ukey_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<JcicZ048Log> findByJcicZ048LogId(JcicZ048LogId jcicZ048LogId);

}
