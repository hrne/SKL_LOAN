package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CollMeet;
import com.st1.itx.db.domain.CollMeetId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CollMeetRepositoryDay extends JpaRepository<CollMeet, CollMeetId> {

	// MeetDate>= , AND MeetDate<= ,AND CaseCode= ,AND CustNo= ,AND FacmNo= ,
	public Slice<CollMeet> findAllByMeetDateGreaterThanEqualAndMeetDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMeetDateDesc(int meetDate_0, int meetDate_1, String caseCode_2,
			int custNo_3, int facmNo_4, Pageable pageable);

	// CaseCode= ,AND CustNo= ,AND FacmNo= ,
	public Slice<CollMeet> findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMeetDateDesc(String caseCode_0, int custNo_1, int facmNo_2, Pageable pageable);

	// MeetDate>= , AND MeetDate<= ,AND CaseCode= ,AND CustNo=
	public Slice<CollMeet> findAllByMeetDateGreaterThanEqualAndMeetDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByMeetDateDesc(int meetDate_0, int meetDate_1, String caseCode_2, int custNo_3,
			Pageable pageable);

	// CaseCode= ,AND CustNo=
	public Slice<CollMeet> findAllByCaseCodeIsAndCustNoIsOrderByMeetDateDesc(String caseCode_0, int custNo_1, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<CollMeet> findByCollMeetId(CollMeetId collMeetId);

}
