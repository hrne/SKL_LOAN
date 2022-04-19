package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.FacShareAppl;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacShareApplRepository extends JpaRepository<FacShareAppl, Integer> {

	// MainApplNo =
	public Slice<FacShareAppl> findAllByMainApplNoIsOrderByKeyinSeqAsc(int mainApplNo_0, Pageable pageable);

	// CustNo =
	public Slice<FacShareAppl> findAllByCustNoIsOrderByMainApplNoAscKeyinSeqAsc(int custNo_0, Pageable pageable);

	// MainApplNo =
	public Optional<FacShareAppl> findTopByMainApplNoIsOrderByKeyinSeqDesc(int mainApplNo_0);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<FacShareAppl> findByApplNo(int applNo);

}
