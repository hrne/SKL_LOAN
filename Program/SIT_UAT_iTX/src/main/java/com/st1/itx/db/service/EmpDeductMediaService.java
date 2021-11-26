package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.EmpDeductMedia;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.EmpDeductMediaId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface EmpDeductMediaService {

  /**
   * findByPrimaryKey
   *
   * @param empDeductMediaId PK
   * @param titaVo Variable-Length Argument
   * @return EmpDeductMedia EmpDeductMedia
   */
  public EmpDeductMedia findById(EmpDeductMediaId empDeductMediaId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice EmpDeductMedia EmpDeductMedia of List
   */
  public Slice<EmpDeductMedia> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = ,AND BatchNo = ,AND DetailSeq = 
   *
   * @param acDate_0 acDate_0
   * @param batchNo_1 batchNo_1
   * @param detailSeq_2 detailSeq_2
   * @param titaVo Variable-Length Argument
   * @return Slice EmpDeductMedia EmpDeductMedia of List
   */
  public EmpDeductMedia detailSeqFirst(int acDate_0, String batchNo_1, int detailSeq_2, TitaVo... titaVo);

  /**
   * MediaKind = ,AND CustNo = ,AND EntryDate = ,AND RepayCode = ,AND RepayAmt = 
   *
   * @param mediaKind_0 mediaKind_0
   * @param custNo_1 custNo_1
   * @param entryDate_2 entryDate_2
   * @param repayCode_3 repayCode_3
   * @param repayAmt_4 repayAmt_4
   * @param titaVo Variable-Length Argument
   * @return Slice EmpDeductMedia EmpDeductMedia of List
   */
  public EmpDeductMedia receiveCheckFirst(String mediaKind_0, int custNo_1, int entryDate_2, int repayCode_3, BigDecimal repayAmt_4, TitaVo... titaVo);

  /**
   * MediaDate &gt;= , AND MediaDate &lt;= , AND MediaKind = 
   *
   * @param mediaDate_0 mediaDate_0
   * @param mediaDate_1 mediaDate_1
   * @param mediaKind_2 mediaKind_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice EmpDeductMedia EmpDeductMedia of List
   */
  public Slice<EmpDeductMedia> mediaDateRng(int mediaDate_0, int mediaDate_1, String mediaKind_2, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = ,AND PerfMonth = ,AND FlowCode = 
   *
   * @param acDate_0 acDate_0
   * @param perfMonth_1 perfMonth_1
   * @param flowCode_2 flowCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice EmpDeductMedia EmpDeductMedia of List
   */
  public Slice<EmpDeductMedia> findL4520A(int acDate_0, int perfMonth_1, String flowCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * MediaDate = ,AND MediaKind = 
   *
   * @param mediaDate_0 mediaDate_0
   * @param mediaKind_1 mediaKind_1
   * @param titaVo Variable-Length Argument
   * @return Slice EmpDeductMedia EmpDeductMedia of List
   */
  public EmpDeductMedia lastMediaSeqFirst(int mediaDate_0, String mediaKind_1, TitaVo... titaVo);

  /**
   * EntryDate &gt;= , AND EntryDate &lt;= , AND MediaKind =
   *
   * @param entryDate_0 entryDate_0
   * @param entryDate_1 entryDate_1
   * @param mediaKind_2 mediaKind_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice EmpDeductMedia EmpDeductMedia of List
   */
  public Slice<EmpDeductMedia> entryDateRng(int entryDate_0, int entryDate_1, String mediaKind_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By EmpDeductMedia
   * 
   * @param empDeductMediaId key
   * @param titaVo Variable-Length Argument
   * @return EmpDeductMedia EmpDeductMedia
   */
  public EmpDeductMedia holdById(EmpDeductMediaId empDeductMediaId, TitaVo... titaVo);

  /**
   * hold By EmpDeductMedia
   * 
   * @param empDeductMedia key
   * @param titaVo Variable-Length Argument
   * @return EmpDeductMedia EmpDeductMedia
   */
  public EmpDeductMedia holdById(EmpDeductMedia empDeductMedia, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param empDeductMedia Entity
   * @param titaVo Variable-Length Argument
   * @return EmpDeductMedia Entity
   * @throws DBException exception
   */
  public EmpDeductMedia insert(EmpDeductMedia empDeductMedia, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param empDeductMedia Entity
   * @param titaVo Variable-Length Argument
   * @return EmpDeductMedia Entity
   * @throws DBException exception
   */
  public EmpDeductMedia update(EmpDeductMedia empDeductMedia, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param empDeductMedia Entity
   * @param titaVo Variable-Length Argument
   * @return EmpDeductMedia Entity
   * @throws DBException exception
   */
  public EmpDeductMedia update2(EmpDeductMedia empDeductMedia, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param empDeductMedia Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(EmpDeductMedia empDeductMedia, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param empDeductMedia Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<EmpDeductMedia> empDeductMedia, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param empDeductMedia Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<EmpDeductMedia> empDeductMedia, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param empDeductMedia Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<EmpDeductMedia> empDeductMedia, TitaVo... titaVo) throws DBException;

}
