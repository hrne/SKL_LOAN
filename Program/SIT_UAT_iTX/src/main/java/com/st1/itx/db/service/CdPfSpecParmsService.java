package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdPfSpecParms;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdPfSpecParmsId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdPfSpecParmsService {

  /**
   * findByPrimaryKey
   *
   * @param cdPfSpecParmsId PK
   * @param titaVo Variable-Length Argument
   * @return CdPfSpecParms CdPfSpecParms
   */
  public CdPfSpecParms findById(CdPfSpecParmsId cdPfSpecParmsId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdPfSpecParms CdPfSpecParms of List
   */
  public Slice<CdPfSpecParms> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ConditionCode = 
   *
   * @param conditionCode_0 conditionCode_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdPfSpecParms CdPfSpecParms of List
   */
  public Slice<CdPfSpecParms> conditionCodeEq(String conditionCode_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdPfSpecParms
   * 
   * @param cdPfSpecParmsId key
   * @param titaVo Variable-Length Argument
   * @return CdPfSpecParms CdPfSpecParms
   */
  public CdPfSpecParms holdById(CdPfSpecParmsId cdPfSpecParmsId, TitaVo... titaVo);

  /**
   * hold By CdPfSpecParms
   * 
   * @param cdPfSpecParms key
   * @param titaVo Variable-Length Argument
   * @return CdPfSpecParms CdPfSpecParms
   */
  public CdPfSpecParms holdById(CdPfSpecParms cdPfSpecParms, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdPfSpecParms Entity
   * @param titaVo Variable-Length Argument
   * @return CdPfSpecParms Entity
   * @throws DBException exception
   */
  public CdPfSpecParms insert(CdPfSpecParms cdPfSpecParms, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdPfSpecParms Entity
   * @param titaVo Variable-Length Argument
   * @return CdPfSpecParms Entity
   * @throws DBException exception
   */
  public CdPfSpecParms update(CdPfSpecParms cdPfSpecParms, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdPfSpecParms Entity
   * @param titaVo Variable-Length Argument
   * @return CdPfSpecParms Entity
   * @throws DBException exception
   */
  public CdPfSpecParms update2(CdPfSpecParms cdPfSpecParms, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdPfSpecParms Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdPfSpecParms cdPfSpecParms, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdPfSpecParms Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdPfSpecParms> cdPfSpecParms, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdPfSpecParms Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdPfSpecParms> cdPfSpecParms, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdPfSpecParms Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdPfSpecParms> cdPfSpecParms, TitaVo... titaVo) throws DBException;

}
