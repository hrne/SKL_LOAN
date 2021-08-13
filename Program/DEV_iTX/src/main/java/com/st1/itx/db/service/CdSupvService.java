package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdSupv;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdSupvService {

  /**
   * findByPrimaryKey
   *
   * @param supvReasonCode PK
   * @param titaVo Variable-Length Argument
   * @return CdSupv CdSupv
   */
  public CdSupv findById(String supvReasonCode, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdSupv CdSupv of List
   */
  public Slice<CdSupv> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * SupvReasonLevel = 
   *
   * @param supvReasonLevel_0 supvReasonLevel_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdSupv CdSupv of List
   */
  public Slice<CdSupv> findSupvReasonLevel(String supvReasonLevel_0, int index, int limit, TitaVo... titaVo);

  /**
   * SupvReasonCode &gt;= ,AND SupvReasonCode &lt;= 
   *
   * @param supvReasonCode_0 supvReasonCode_0
   * @param supvReasonCode_1 supvReasonCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdSupv CdSupv of List
   */
  public Slice<CdSupv> findSupvReasonCode(String supvReasonCode_0, String supvReasonCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdSupv
   * 
   * @param supvReasonCode key
   * @param titaVo Variable-Length Argument
   * @return CdSupv CdSupv
   */
  public CdSupv holdById(String supvReasonCode, TitaVo... titaVo);

  /**
   * hold By CdSupv
   * 
   * @param cdSupv key
   * @param titaVo Variable-Length Argument
   * @return CdSupv CdSupv
   */
  public CdSupv holdById(CdSupv cdSupv, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdSupv Entity
   * @param titaVo Variable-Length Argument
   * @return CdSupv Entity
   * @throws DBException exception
   */
  public CdSupv insert(CdSupv cdSupv, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdSupv Entity
   * @param titaVo Variable-Length Argument
   * @return CdSupv Entity
   * @throws DBException exception
   */
  public CdSupv update(CdSupv cdSupv, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdSupv Entity
   * @param titaVo Variable-Length Argument
   * @return CdSupv Entity
   * @throws DBException exception
   */
  public CdSupv update2(CdSupv cdSupv, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdSupv Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdSupv cdSupv, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdSupv Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdSupv> cdSupv, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdSupv Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdSupv> cdSupv, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdSupv Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdSupv> cdSupv, TitaVo... titaVo) throws DBException;

}
