package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MlaundryRecord;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MlaundryRecordId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MlaundryRecordService {

  /**
   * findByPrimaryKey
   *
   * @param mlaundryRecordId PK
   * @param titaVo Variable-Length Argument
   * @return MlaundryRecord MlaundryRecord
   */
  public MlaundryRecord findById(MlaundryRecordId mlaundryRecordId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MlaundryRecord MlaundryRecord of List
   */
  public Slice<MlaundryRecord> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * RecordDate &gt;= ,AND RecordDate &lt;= ,AND ActualRepayDate &gt;= ,AND ActualRepayDate &lt;= 
   *
   * @param recordDate_0 recordDate_0
   * @param recordDate_1 recordDate_1
   * @param actualRepayDate_2 actualRepayDate_2
   * @param actualRepayDate_3 actualRepayDate_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MlaundryRecord MlaundryRecord of List
   */
  public Slice<MlaundryRecord> findRecordDate(int recordDate_0, int recordDate_1, int actualRepayDate_2, int actualRepayDate_3, int index, int limit, TitaVo... titaVo);

  /**
   * RecordDate &gt;= ,AND RecordDate &lt;= 
   *
   * @param recordDate_0 recordDate_0
   * @param recordDate_1 recordDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MlaundryRecord MlaundryRecord of List
   */
  public Slice<MlaundryRecord> findRecordD(int recordDate_0, int recordDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * ActualRepayDate &gt;= ,AND ActualRepayDate &lt;=
   *
   * @param actualRepayDate_0 actualRepayDate_0
   * @param actualRepayDate_1 actualRepayDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MlaundryRecord MlaundryRecord of List
   */
  public Slice<MlaundryRecord> findRepayD(int actualRepayDate_0, int actualRepayDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;= ,AND BormNo &gt;= ,AND BormNo &lt;= ,AND RepayDate &gt;=
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param facmNo_2 facmNo_2
   * @param bormNo_3 bormNo_3
   * @param bormNo_4 bormNo_4
   * @param repayDate_5 repayDate_5
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MlaundryRecord MlaundryRecord of List
   */
  public Slice<MlaundryRecord> findCustNoEq(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int repayDate_5, int index, int limit, TitaVo... titaVo);

  /**
   * hold By MlaundryRecord
   * 
   * @param mlaundryRecordId key
   * @param titaVo Variable-Length Argument
   * @return MlaundryRecord MlaundryRecord
   */
  public MlaundryRecord holdById(MlaundryRecordId mlaundryRecordId, TitaVo... titaVo);

  /**
   * hold By MlaundryRecord
   * 
   * @param mlaundryRecord key
   * @param titaVo Variable-Length Argument
   * @return MlaundryRecord MlaundryRecord
   */
  public MlaundryRecord holdById(MlaundryRecord mlaundryRecord, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param mlaundryRecord Entity
   * @param titaVo Variable-Length Argument
   * @return MlaundryRecord Entity
   * @throws DBException exception
   */
  public MlaundryRecord insert(MlaundryRecord mlaundryRecord, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param mlaundryRecord Entity
   * @param titaVo Variable-Length Argument
   * @return MlaundryRecord Entity
   * @throws DBException exception
   */
  public MlaundryRecord update(MlaundryRecord mlaundryRecord, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param mlaundryRecord Entity
   * @param titaVo Variable-Length Argument
   * @return MlaundryRecord Entity
   * @throws DBException exception
   */
  public MlaundryRecord update2(MlaundryRecord mlaundryRecord, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param mlaundryRecord Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(MlaundryRecord mlaundryRecord, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param mlaundryRecord Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<MlaundryRecord> mlaundryRecord, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param mlaundryRecord Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<MlaundryRecord> mlaundryRecord, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param mlaundryRecord Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<MlaundryRecord> mlaundryRecord, TitaVo... titaVo) throws DBException;

}
