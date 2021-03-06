package com.st1.itx.db.repository.hist;


import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxDataLog;
import com.st1.itx.db.domain.TxDataLogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxDataLogRepositoryHist extends JpaRepository<TxDataLog, TxDataLogId> {

  // TxDate >= ,AND TxDate <= ,AND TranNo %
  public Slice<TxDataLog> findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeOrderByCreateDateAsc(int txDate_0, int txDate_1, String tranNo_2, Pageable pageable);

  // TxDate >= ,AND TxDate <= ,AND TranNo % ,AND CustNo =
  public Slice<TxDataLog> findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeAndCustNoIsOrderByCreateDateAsc(int txDate_0, int txDate_1, String tranNo_2, int custNo_3, Pageable pageable);

  // TxDate >= ,AND TxDate <= ,AND TranNo % ,AND CustNo = ,AND FacmNo =
  public Slice<TxDataLog> findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeAndCustNoIsAndFacmNoIsOrderByCreateDateAsc(int txDate_0, int txDate_1, String tranNo_2, int custNo_3, int facmNo_4, Pageable pageable);

  // TxDate >= ,AND TxDate <= ,AND TranNo % ,AND CustNo = ,AND FacmNo = ,AND BormNo =
  public Slice<TxDataLog> findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoLikeAndCustNoIsAndFacmNoIsAndBormNoIsOrderByCreateDateAsc(int txDate_0, int txDate_1, String tranNo_2, int custNo_3, int facmNo_4, int bormNo_5, Pageable pageable);

  // TxDate >= ,AND TxDate <= ,AND TxSeq %
  public Slice<TxDataLog> findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTxSeqLikeOrderByCustNoAsc(int txDate_0, int txDate_1, String txSeq_2, Pageable pageable);

  // TxDate >= ,AND TxDate <= ,AND TranNo ^i
  public Slice<TxDataLog> findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInOrderByCreateDateDesc(int txDate_0, int txDate_1, List<String> tranNo_2, Pageable pageable);

  // TxDate >= ,AND TxDate <= ,AND TranNo ^i ,AND CustNo =
  public Slice<TxDataLog> findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInAndCustNoIsOrderByCreateDateDesc(int txDate_0, int txDate_1, List<String> tranNo_2, int custNo_3, Pageable pageable);

  // TxDate >= ,AND TxDate <= ,AND TranNo ^i ,AND CustNo = ,AND FacmNo =
  public Slice<TxDataLog> findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInAndCustNoIsAndFacmNoIsOrderByCreateDateDesc(int txDate_0, int txDate_1, List<String> tranNo_2, int custNo_3, int facmNo_4, Pageable pageable);

  // TxDate >= ,AND TxDate <= ,AND TranNo ^i ,AND CustNo = ,AND FacmNo = ,AND BormNo =
  public Slice<TxDataLog> findAllByTxDateGreaterThanEqualAndTxDateLessThanEqualAndTranNoInAndCustNoIsAndFacmNoIsAndBormNoIsOrderByCreateDateDesc(int txDate_0, int txDate_1, List<String> tranNo_2, int custNo_3, int facmNo_4, int bormNo_5, Pageable pageable);

  // TranNo = ,AND MrKey = 
  public Slice<TxDataLog> findAllByTranNoIsAndMrKeyIsOrderByCreateDateDesc(String tranNo_0, String mrKey_1, Pageable pageable);

  // MrKey = ,AND TranNo ^i
  public Optional<TxDataLog> findTopByMrKeyIsAndTranNoInOrderByCreateDateDesc(String mrKey_0, List<String> tranNo_1);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<TxDataLog> findByTxDataLogId(TxDataLogId txDataLogId);

}

