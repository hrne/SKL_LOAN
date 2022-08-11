package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BatxHead;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.BatxHeadId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BatxHeadService {

  /**
   * findByPrimaryKey
   *
   * @param batxHeadId PK
   * @param titaVo Variable-Length Argument
   * @return BatxHead BatxHead
   */
  public BatxHead findById(BatxHeadId batxHeadId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxHead BatxHead of List
   */
  public Slice<BatxHead> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AcDate &gt;= ,AND AcDate &lt;= 
   *
   * @param acDate_0 acDate_0
   * @param acDate_1 acDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxHead BatxHead of List
   */
  public Slice<BatxHead> acDateRange(int acDate_0, int acDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = ,AND BatchNo %
   *
   * @param acDate_0 acDate_0
   * @param batchNo_1 batchNo_1
   * @param titaVo Variable-Length Argument
   * @return Slice BatxHead BatxHead of List
   */
  public BatxHead batchNoDescFirst(int acDate_0, String batchNo_1, TitaVo... titaVo);

  /**
   * AcDate = ,AND TitaTxCd = ,AND BatxExeCode &lt;&gt; 
   *
   * @param acDate_0 acDate_0
   * @param titaTxCd_1 titaTxCd_1
   * @param batxExeCode_2 batxExeCode_2
   * @param titaVo Variable-Length Argument
   * @return Slice BatxHead BatxHead of List
   */
  public BatxHead titaTxCdFirst(int acDate_0, String titaTxCd_1, String batxExeCode_2, TitaVo... titaVo);

  /**
   * hold By BatxHead
   * 
   * @param batxHeadId key
   * @param titaVo Variable-Length Argument
   * @return BatxHead BatxHead
   */
  public BatxHead holdById(BatxHeadId batxHeadId, TitaVo... titaVo);

  /**
   * hold By BatxHead
   * 
   * @param batxHead key
   * @param titaVo Variable-Length Argument
   * @return BatxHead BatxHead
   */
  public BatxHead holdById(BatxHead batxHead, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param batxHead Entity
   * @param titaVo Variable-Length Argument
   * @return BatxHead Entity
   * @throws DBException exception
   */
  public BatxHead insert(BatxHead batxHead, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param batxHead Entity
   * @param titaVo Variable-Length Argument
   * @return BatxHead Entity
   * @throws DBException exception
   */
  public BatxHead update(BatxHead batxHead, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param batxHead Entity
   * @param titaVo Variable-Length Argument
   * @return BatxHead Entity
   * @throws DBException exception
   */
  public BatxHead update2(BatxHead batxHead, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param batxHead Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(BatxHead batxHead, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param batxHead Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<BatxHead> batxHead, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param batxHead Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<BatxHead> batxHead, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param batxHead Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<BatxHead> batxHead, TitaVo... titaVo) throws DBException;

}
