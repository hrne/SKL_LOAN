package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdConvertCode;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdConvertCodeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdConvertCodeService {

  /**
   * findByPrimaryKey
   *
   * @param cdConvertCodeId PK
   * @param titaVo Variable-Length Argument
   * @return CdConvertCode CdConvertCode
   */
  public CdConvertCode findById(CdConvertCodeId cdConvertCodeId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdConvertCode CdConvertCode of List
   */
  public Slice<CdConvertCode> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdConvertCode
   * 
   * @param cdConvertCodeId key
   * @param titaVo Variable-Length Argument
   * @return CdConvertCode CdConvertCode
   */
  public CdConvertCode holdById(CdConvertCodeId cdConvertCodeId, TitaVo... titaVo);

  /**
   * hold By CdConvertCode
   * 
   * @param cdConvertCode key
   * @param titaVo Variable-Length Argument
   * @return CdConvertCode CdConvertCode
   */
  public CdConvertCode holdById(CdConvertCode cdConvertCode, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdConvertCode Entity
   * @param titaVo Variable-Length Argument
   * @return CdConvertCode Entity
   * @throws DBException exception
   */
  public CdConvertCode insert(CdConvertCode cdConvertCode, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdConvertCode Entity
   * @param titaVo Variable-Length Argument
   * @return CdConvertCode Entity
   * @throws DBException exception
   */
  public CdConvertCode update(CdConvertCode cdConvertCode, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdConvertCode Entity
   * @param titaVo Variable-Length Argument
   * @return CdConvertCode Entity
   * @throws DBException exception
   */
  public CdConvertCode update2(CdConvertCode cdConvertCode, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdConvertCode Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdConvertCode cdConvertCode, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdConvertCode Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdConvertCode> cdConvertCode, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdConvertCode Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdConvertCode> cdConvertCode, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdConvertCode Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdConvertCode> cdConvertCode, TitaVo... titaVo) throws DBException;

}
