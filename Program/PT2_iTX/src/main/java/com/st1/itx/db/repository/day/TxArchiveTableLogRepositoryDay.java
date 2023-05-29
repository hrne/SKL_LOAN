package com.st1.itx.db.repository.day;


import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxArchiveTableLog;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxArchiveTableLogRepositoryDay extends JpaRepository<TxArchiveTableLog, Long> {

  // Type = ,AND TableName = ,AND ExecuteDate = ,AND DataFrom= ,AND DataTo = ,AND BatchNo = ,AND CustNo = ,AND FacmNo = ,AND BormNo =
  public Slice<TxArchiveTableLog> findAllByTypeIsAndTableNameIsAndExecuteDateIsAndDataFromIsAndDataToIsAndBatchNoIsAndCustNoIsAndFacmNoIsAndBormNoIs(String type_0, String tableName_1, int executeDate_2, String dataFrom_3, String dataTo_4, int batchNo_5, int custNo_6, int facmNo_7, int bormNo_8, Pageable pageable);

  // Type = ,AND TableName = ,AND ExecuteDate = ,AND BatchNo = ,AND CustNo = ,AND FacmNo = ,AND BormNo = ,AND IsDeleted = 
  public Slice<TxArchiveTableLog> findAllByTypeIsAndTableNameIsAndExecuteDateIsAndBatchNoIsAndCustNoIsAndFacmNoIsAndBormNoIsAndIsDeletedIs(String type_0, String tableName_1, int executeDate_2, int batchNo_3, int custNo_4, int facmNo_5, int bormNo_6, int isDeleted_7, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<TxArchiveTableLog> findByLogNo(Long logNo);

}

