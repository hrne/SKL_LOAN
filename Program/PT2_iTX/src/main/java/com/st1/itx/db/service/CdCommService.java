package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdComm;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdCommId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdCommService {

  /**
   * findByPrimaryKey
   *
   * @param cdCommId PK
   * @param titaVo Variable-Length Argument
   * @return CdComm CdComm
   */
  public CdComm findById(CdCommId cdCommId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdComm CdComm of List
   */
  public Slice<CdComm> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CdType = ,AND CdItem = ,AND EffectDate &gt;= ,AND EffectDate &lt;=
   *
   * @param cdType_0 cdType_0
   * @param cdItem_1 cdItem_1
   * @param effectDate_2 effectDate_2
   * @param effectDate_3 effectDate_3
   * @param titaVo Variable-Length Argument
   * @return Slice CdComm CdComm of List
   */
  public CdComm CdTypeDescFirst(String cdType_0, String cdItem_1, int effectDate_2, int effectDate_3, TitaVo... titaVo);

  /**
   * CdType = ,AND CdItem = ,AND EffectDate &gt;= ,AND EffectDate &lt;=
   *
   * @param cdType_0 cdType_0
   * @param cdItem_1 cdItem_1
   * @param effectDate_2 effectDate_2
   * @param effectDate_3 effectDate_3
   * @param titaVo Variable-Length Argument
   * @return Slice CdComm CdComm of List
   */
  public CdComm CdTypeAscFirst(String cdType_0, String cdItem_1, int effectDate_2, int effectDate_3, TitaVo... titaVo);

  /**
   * hold By CdComm
   * 
   * @param cdCommId key
   * @param titaVo Variable-Length Argument
   * @return CdComm CdComm
   */
  public CdComm holdById(CdCommId cdCommId, TitaVo... titaVo);

  /**
   * hold By CdComm
   * 
   * @param cdComm key
   * @param titaVo Variable-Length Argument
   * @return CdComm CdComm
   */
  public CdComm holdById(CdComm cdComm, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdComm Entity
   * @param titaVo Variable-Length Argument
   * @return CdComm Entity
   * @throws DBException exception
   */
  public CdComm insert(CdComm cdComm, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdComm Entity
   * @param titaVo Variable-Length Argument
   * @return CdComm Entity
   * @throws DBException exception
   */
  public CdComm update(CdComm cdComm, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdComm Entity
   * @param titaVo Variable-Length Argument
   * @return CdComm Entity
   * @throws DBException exception
   */
  public CdComm update2(CdComm cdComm, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdComm Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdComm cdComm, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdComm Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdComm> cdComm, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdComm Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdComm> cdComm, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdComm Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdComm> cdComm, TitaVo... titaVo) throws DBException;

}
