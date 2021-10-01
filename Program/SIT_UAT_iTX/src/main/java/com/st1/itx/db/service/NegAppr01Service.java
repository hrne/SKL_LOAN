package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.NegAppr01;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.NegAppr01Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface NegAppr01Service {

  /**
   * findByPrimaryKey
   *
   * @param negAppr01Id PK
   * @param titaVo Variable-Length Argument
   * @return NegAppr01 NegAppr01
   */
  public NegAppr01 findById(NegAppr01Id negAppr01Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegAppr01 NegAppr01 of List
   */
  public Slice<NegAppr01> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo= 
   *
   * @param custNo_0 custNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegAppr01 NegAppr01 of List
   */
  public Slice<NegAppr01> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo= , AND ExportDate =
   *
   * @param custNo_0 custNo_0
   * @param exportDate_1 exportDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegAppr01 NegAppr01 of List
   */
  public Slice<NegAppr01> custNoExportDateEq(int custNo_0, int exportDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo= , AND ExportDate&gt;= , AND ExportDate &lt;=
   *
   * @param custNo_0 custNo_0
   * @param exportDate_1 exportDate_1
   * @param exportDate_2 exportDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegAppr01 NegAppr01 of List
   */
  public Slice<NegAppr01> custExporBetween(int custNo_0, int exportDate_1, int exportDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * ExportDate=
   *
   * @param exportDate_0 exportDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegAppr01 NegAppr01 of List
   */
  public Slice<NegAppr01> exportEq(int exportDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * ExportDate&gt;= , AND ExportDate &lt;=
   *
   * @param exportDate_0 exportDate_0
   * @param exportDate_1 exportDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegAppr01 NegAppr01 of List
   */
  public Slice<NegAppr01> exportDateBetween(int exportDate_0, int exportDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * BringUpDate=
   *
   * @param bringUpDate_0 bringUpDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegAppr01 NegAppr01 of List
   */
  public Slice<NegAppr01> bringUpDateEq(int bringUpDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * BatchTxtNo = , AND FinCode = , AND ApprDate=
   *
   * @param batchTxtNo_0 batchTxtNo_0
   * @param finCode_1 finCode_1
   * @param apprDate_2 apprDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegAppr01 NegAppr01 of List
   */
  public Slice<NegAppr01> findBatch(String batchTxtNo_0, String finCode_1, int apprDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = , AND CaseSeq = 
   *
   * @param custNo_0 custNo_0
   * @param caseSeq_1 caseSeq_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegAppr01 NegAppr01 of List
   */
  public Slice<NegAppr01> sumCustNo(int custNo_0, int caseSeq_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = , AND CaseSeq = , AND FinCode=
   *
   * @param custNo_0 custNo_0
   * @param caseSeq_1 caseSeq_1
   * @param finCode_2 finCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegAppr01 NegAppr01 of List
   */
  public Slice<NegAppr01> sumCustNoFinCode(int custNo_0, int caseSeq_1, String finCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = , AND TitaTlrNo = ,AND TitaTxtNo=
   *
   * @param acDate_0 acDate_0
   * @param titaTlrNo_1 titaTlrNo_1
   * @param titaTxtNo_2 titaTxtNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegAppr01 NegAppr01 of List
   */
  public Slice<NegAppr01> findTrans(int acDate_0, String titaTlrNo_1, int titaTxtNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = , AND CaseSeq = , AND ExportDate=
   *
   * @param custNo_0 custNo_0
   * @param caseSeq_1 caseSeq_1
   * @param exportDate_2 exportDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice NegAppr01 NegAppr01 of List
   */
  public Slice<NegAppr01> findByCustNoCaseSeq(int custNo_0, int caseSeq_1, int exportDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By NegAppr01
   * 
   * @param negAppr01Id key
   * @param titaVo Variable-Length Argument
   * @return NegAppr01 NegAppr01
   */
  public NegAppr01 holdById(NegAppr01Id negAppr01Id, TitaVo... titaVo);

  /**
   * hold By NegAppr01
   * 
   * @param negAppr01 key
   * @param titaVo Variable-Length Argument
   * @return NegAppr01 NegAppr01
   */
  public NegAppr01 holdById(NegAppr01 negAppr01, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param negAppr01 Entity
   * @param titaVo Variable-Length Argument
   * @return NegAppr01 Entity
   * @throws DBException exception
   */
  public NegAppr01 insert(NegAppr01 negAppr01, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param negAppr01 Entity
   * @param titaVo Variable-Length Argument
   * @return NegAppr01 Entity
   * @throws DBException exception
   */
  public NegAppr01 update(NegAppr01 negAppr01, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param negAppr01 Entity
   * @param titaVo Variable-Length Argument
   * @return NegAppr01 Entity
   * @throws DBException exception
   */
  public NegAppr01 update2(NegAppr01 negAppr01, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param negAppr01 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(NegAppr01 negAppr01, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param negAppr01 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<NegAppr01> negAppr01, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param negAppr01 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<NegAppr01> negAppr01, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param negAppr01 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<NegAppr01> negAppr01, TitaVo... titaVo) throws DBException;

}
