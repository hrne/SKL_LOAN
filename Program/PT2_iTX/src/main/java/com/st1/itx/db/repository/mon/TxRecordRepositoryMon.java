package com.st1.itx.db.repository.mon;

import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxRecord;
import com.st1.itx.db.domain.TxRecordId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxRecordRepositoryMon extends JpaRepository<TxRecord, TxRecordId> {

	// Entdy = ,AND BrNo =
	public Slice<TxRecord> findAllByEntdyIsAndBrNoIsOrderByCreateDateAsc(int entdy_0, String brNo_1, Pageable pageable);

	// Entdy = ,AND BrNo = ,AND TxResult = ,AND CanCancel = ,AND ActionFg = ,AND
	// Hcode<> ,AND TlrNo % ,AND TranNo %
	public Slice<TxRecord> findAllByEntdyIsAndBrNoIsAndTxResultIsAndCanCancelIsAndActionFgIsAndHcodeNotAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(int entdy_0, String brNo_1, String txResult_2,
			int canCancel_3, int actionFg_4, int hcode_5, String tlrNo_6, String tranNo_7, Pageable pageable);

	// Entdy = ,AND BrNo = ,AND TxResult = ,AND CanModify = ,AND ActionFg = ,AND
	// Hcode<> ,AND TlrNo % ,AND TranNo %
	public Slice<TxRecord> findAllByEntdyIsAndBrNoIsAndTxResultIsAndCanModifyIsAndActionFgIsAndHcodeNotAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(int entdy_0, String brNo_1, String txResult_2,
			int canModify_3, int actionFg_4, int hcode_5, String tlrNo_6, String tranNo_7, Pageable pageable);

	// Entdy >= ,AND Entdy <= ,AND BrNo = ,AND TxResult = ,AND ActionFg = ,AND TlrNo
	// % ,AND TranNo %
	public Slice<TxRecord> findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndBrNoIsAndTxResultIsAndActionFgIsAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(int entdy_0, int entdy_1, String brNo_2,
			String txResult_3, int actionFg_4, String tlrNo_5, String tranNo_6, Pageable pageable);

	// Entdy >= ,AND Entdy <= ,AND BrNo = ,AND TxResult = ,AND TlrNo % ,AND TranNo %
	public Slice<TxRecord> findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndBrNoIsAndTxResultIsAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(int entdy_0, int entdy_1, String brNo_2,
			String txResult_3, String tlrNo_4, String tranNo_5, Pageable pageable);

	// Entdy >= ,AND Entdy <= ,AND BrNo = ,AND TxResult = ,AND Hcode = ,AND TlrNo %
	// ,AND TranNo %
	public Slice<TxRecord> findAllByEntdyGreaterThanEqualAndEntdyLessThanEqualAndBrNoIsAndTxResultIsAndHcodeIsAndTlrNoLikeAndTranNoLikeOrderByCreateDateAsc(int entdy_0, int entdy_1, String brNo_2,
			String txResult_3, int hcode_4, String tlrNo_5, String tranNo_6, Pageable pageable);

	// MrKey % ,AND TranNo ^i ,AND Entdy >= ,AND Entdy <=
	public Slice<TxRecord> findAllByMrKeyLikeAndTranNoInAndEntdyGreaterThanEqualAndEntdyLessThanEqualOrderByCreateDateAsc(String mrKey_0, List<String> tranNo_1, int entdy_2, int entdy_3,
			Pageable pageable);

	// CalDate = ,AND BrNo =,AND ImportFg =
	public Slice<TxRecord> findAllByCalDateIsAndBrNoIsAndImportFgIsOrderByCreateDateAsc(int calDate_0, String brNo_1, String importFg_2, Pageable pageable);

	// CalDate >= ,AND CalDate <= ,AND BrNo = ,AND ImportFg =
	public Slice<TxRecord> findAllByCalDateGreaterThanEqualAndCalDateLessThanEqualAndBrNoIsAndImportFgIsOrderByCreateDateAsc(int calDate_0, int calDate_1, String brNo_2, String importFg_3,
			Pageable pageable);

	// CalDate >= ,AND CalDate <= ,AND BrNo = ,AND LockCustNo = ,AND ImportFg =
	public Slice<TxRecord> findAllByCalDateGreaterThanEqualAndCalDateLessThanEqualAndBrNoIsAndLockCustNoIsAndImportFgIsOrderByCreateDateAsc(int calDate_0, int calDate_1, String brNo_2,
			int lockCustNo_3, String importFg_4, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<TxRecord> findByTxRecordId(TxRecordId txRecordId);

}
