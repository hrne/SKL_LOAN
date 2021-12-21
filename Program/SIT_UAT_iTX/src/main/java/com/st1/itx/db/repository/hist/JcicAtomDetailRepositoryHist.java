package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicAtomDetail;
import com.st1.itx.db.domain.JcicAtomDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicAtomDetailRepositoryHist extends JpaRepository<JcicAtomDetail, JcicAtomDetailId> {

	// FunctionCode =
	public Slice<JcicAtomDetail> findAllByFunctionCodeIsOrderByFunctionCodeAscDataOrderAsc(String functionCode_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<JcicAtomDetail> findByJcicAtomDetailId(JcicAtomDetailId jcicAtomDetailId);

}
