package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.NegTrans;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.NegTransId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface NegTransService {

  /**
   * findByPrimaryKey
   *
   * @param negTransId PK
   * @param titaVo Variable-Length Argument
   * @return NegTrans NegTrans
   */
  public NegTrans findById(NegTransId negTransId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegTrans NegTrans of List
   */
  public Slice<NegTrans> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = 
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegTrans NegTrans of List
   */
  public Slice<NegTrans> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= , AND AcDate &lt;=
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegTrans NegTrans of List
   */
  public Slice<NegTrans> acDateBetween(int acDate_0, int acDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = , AND AcDate &gt;= , AND AcDate &lt;=
   *
   * @param custNo_0 custNo_0
   * @param acDate_1 acDate_1
   * @param acDate_2 acDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegTrans NegTrans of List
   */
  public Slice<NegTrans> custAndAcDate(int custNo_0, int acDate_1, int acDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * EntryDate &gt;= , AND EntryDate &lt;=
   *
   * @param entryDate_0 entryDate_0
   * @param entryDate_1 entryDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegTrans NegTrans of List
   */
  public Slice<NegTrans> entryDateBetween(int entryDate_0, int entryDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = , AND EntryDate &gt;= , AND EntryDate &lt;=
   *
   * @param custNo_0 custNo_0
   * @param entryDate_1 entryDate_1
   * @param entryDate_2 entryDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegTrans NegTrans of List
   */
  public Slice<NegTrans> custAndEntryDate(int custNo_0, int entryDate_1, int entryDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * TxStatus&gt;= , AND TxStatus&lt;=
   *
   * @param txStatus_0 txStatus_0
   * @param txStatus_1 txStatus_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegTrans NegTrans of List
   */
  public Slice<NegTrans> txStatusBetween(int txStatus_0, int txStatus_1, int index, int limit, TitaVo... titaVo);

  /**
   * TxStatus=  , AND RepayDate=
   *
   * @param txStatus_0 txStatus_0
   * @param repayDate_1 repayDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegTrans NegTrans of List
   */
  public Slice<NegTrans> txStatusDateEq(int txStatus_0, int repayDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * RepayDate&gt;= , AND RepayDate&lt;=
   *
   * @param repayDate_0 repayDate_0
   * @param repayDate_1 repayDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegTrans NegTrans of List
   */
  public Slice<NegTrans> repayDateBetween(int repayDate_0, int repayDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * RepayDate=
   *
   * @param repayDate_0 repayDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegTrans NegTrans of List
   */
  public Slice<NegTrans> repayDateEq(int repayDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * ExportDate&gt;= , AND ExportDate&lt;=
   *
   * @param exportDate_0 exportDate_0
   * @param exportDate_1 exportDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegTrans NegTrans of List
   */
  public Slice<NegTrans> exportDateBetween(int exportDate_0, int exportDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * ExportDate=
   *
   * @param exportDate_0 exportDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegTrans NegTrans of List
   */
  public Slice<NegTrans> exportDateEq(int exportDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * ExportAcDate=
   *
   * @param exportAcDate_0 exportAcDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegTrans NegTrans of List
   */
  public Slice<NegTrans> exportAcDateEq(int exportAcDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * ThisEntdy = , AND ThisKinbr = , AND ThisTlrNo = , AND ThisTxtNo = , AND ThisSeqNo = 
   *
   * @param thisEntdy_0 thisEntdy_0
   * @param thisKinbr_1 thisKinbr_1
   * @param thisTlrNo_2 thisTlrNo_2
   * @param thisTxtNo_3 thisTxtNo_3
   * @param thisSeqNo_4 thisSeqNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegTrans NegTrans of List
   */
  public Slice<NegTrans> backFunc(int thisEntdy_0, String thisKinbr_1, String thisTlrNo_2, String thisTxtNo_3, String thisSeqNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate =, AND TitaTxtNo =
   *
   * @param acDate_0 acDate_0
   * @param titaTxtNo_1 titaTxtNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegTrans NegTrans of List
   */
  public Slice<NegTrans> findbyTitaTxTno(int acDate_0, int titaTxtNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = , AND CaseSeq =
   *
   * @param custNo_0 custNo_0
   * @param caseSeq_1 caseSeq_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegTrans NegTrans of List
   */
  public Slice<NegTrans> custAndCaseSeq(int custNo_0, int caseSeq_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By NegTrans
   * 
   * @param negTransId key
   * @param titaVo Variable-Length Argument
   * @return NegTrans NegTrans
   */
  public NegTrans holdById(NegTransId negTransId, TitaVo... titaVo);

  /**
   * hold By NegTrans
   * 
   * @param negTrans key
   * @param titaVo Variable-Length Argument
   * @return NegTrans NegTrans
   */
  public NegTrans holdById(NegTrans negTrans, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param negTrans Entity
   * @param titaVo Variable-Length Argument
   * @return NegTrans Entity
   * @throws DBException exception
   */
  public NegTrans insert(NegTrans negTrans, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param negTrans Entity
   * @param titaVo Variable-Length Argument
   * @return NegTrans Entity
   * @throws DBException exception
   */
  public NegTrans update(NegTrans negTrans, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param negTrans Entity
   * @param titaVo Variable-Length Argument
   * @return NegTrans Entity
   * @throws DBException exception
   */
  public NegTrans update2(NegTrans negTrans, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param negTrans Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(NegTrans negTrans, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param negTrans Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<NegTrans> negTrans, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param negTrans Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<NegTrans> negTrans, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param negTrans Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<NegTrans> negTrans, TitaVo... titaVo) throws DBException;

}
