package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.InnDocRecord;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.InnDocRecordId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface InnDocRecordService {

  /**
   * findByPrimaryKey
   *
   * @param innDocRecordId PK
   * @param titaVo Variable-Length Argument
   * @return InnDocRecord InnDocRecord
   */
  public InnDocRecord findById(InnDocRecordId innDocRecordId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InnDocRecord InnDocRecord of List
   */
  public Slice<InnDocRecord> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND ApplDate &gt;= ,AND ApplDate &lt;= ,AND UsageCode = ,AND ApplCode = 
   *
   * @param custNo_0 custNo_0
   * @param applDate_1 applDate_1
   * @param applDate_2 applDate_2
   * @param usageCode_3 usageCode_3
   * @param applCode_4 applCode_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InnDocRecord InnDocRecord of List
   */
  public Slice<InnDocRecord> findL5903ARg(int custNo_0, int applDate_1, int applDate_2, String usageCode_3, String applCode_4, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND ApplDate &gt;= ,AND ApplDate &lt;= ,AND ApplCode = 
   *
   * @param custNo_0 custNo_0
   * @param applDate_1 applDate_1
   * @param applDate_2 applDate_2
   * @param applCode_3 applCode_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InnDocRecord InnDocRecord of List
   */
  public Slice<InnDocRecord> findL5903BRg(int custNo_0, int applDate_1, int applDate_2, String applCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND ApplDate &gt;= ,AND ApplDate &lt;= ,AND UsageCode = 
   *
   * @param custNo_0 custNo_0
   * @param applDate_1 applDate_1
   * @param applDate_2 applDate_2
   * @param usageCode_3 usageCode_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InnDocRecord InnDocRecord of List
   */
  public Slice<InnDocRecord> findL5903CRg(int custNo_0, int applDate_1, int applDate_2, String usageCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND ApplDate &gt;= ,AND ApplDate &lt;= 
   *
   * @param custNo_0 custNo_0
   * @param applDate_1 applDate_1
   * @param applDate_2 applDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InnDocRecord InnDocRecord of List
   */
  public Slice<InnDocRecord> findL5903DRg(int custNo_0, int applDate_1, int applDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * ApplDate &gt;= ,AND ApplDate &lt;= ,AND ReturnDate = 
   *
   * @param applDate_0 applDate_0
   * @param applDate_1 applDate_1
   * @param returnDate_2 returnDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InnDocRecord InnDocRecord of List
   */
  public Slice<InnDocRecord> findL5104ARg(int applDate_0, int applDate_1, int returnDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * ApplDate &gt;= ,AND ApplDate &lt;= ,AND UsageCode = 
   *
   * @param applDate_0 applDate_0
   * @param applDate_1 applDate_1
   * @param usageCode_2 usageCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice InnDocRecord InnDocRecord of List
   */
  public Slice<InnDocRecord> findL5104BRg(int applDate_0, int applDate_1, String usageCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By InnDocRecord
   * 
   * @param innDocRecordId key
   * @param titaVo Variable-Length Argument
   * @return InnDocRecord InnDocRecord
   */
  public InnDocRecord holdById(InnDocRecordId innDocRecordId, TitaVo... titaVo);

  /**
   * hold By InnDocRecord
   * 
   * @param innDocRecord key
   * @param titaVo Variable-Length Argument
   * @return InnDocRecord InnDocRecord
   */
  public InnDocRecord holdById(InnDocRecord innDocRecord, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param innDocRecord Entity
   * @param titaVo Variable-Length Argument
   * @return InnDocRecord Entity
   * @throws DBException exception
   */
  public InnDocRecord insert(InnDocRecord innDocRecord, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param innDocRecord Entity
   * @param titaVo Variable-Length Argument
   * @return InnDocRecord Entity
   * @throws DBException exception
   */
  public InnDocRecord update(InnDocRecord innDocRecord, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param innDocRecord Entity
   * @param titaVo Variable-Length Argument
   * @return InnDocRecord Entity
   * @throws DBException exception
   */
  public InnDocRecord update2(InnDocRecord innDocRecord, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param innDocRecord Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(InnDocRecord innDocRecord, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param innDocRecord Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<InnDocRecord> innDocRecord, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param innDocRecord Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<InnDocRecord> innDocRecord, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param innDocRecord Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<InnDocRecord> innDocRecord, TitaVo... titaVo) throws DBException;

}
