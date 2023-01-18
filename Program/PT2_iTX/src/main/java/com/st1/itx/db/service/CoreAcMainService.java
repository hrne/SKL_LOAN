package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CoreAcMain;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CoreAcMainId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CoreAcMainService {

  /**
   * findByPrimaryKey
   *
   * @param coreAcMainId PK
   * @param titaVo Variable-Length Argument
   * @return CoreAcMain CoreAcMain
   */
  public CoreAcMain findById(CoreAcMainId coreAcMainId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CoreAcMain CoreAcMain of List
   */
  public Slice<CoreAcMain> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = 
   *
   * @param acDate_0 acDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CoreAcMain CoreAcMain of List
   */
  public Slice<CoreAcMain> findByAcDate(int acDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CoreAcMain
   * 
   * @param coreAcMainId key
   * @param titaVo Variable-Length Argument
   * @return CoreAcMain CoreAcMain
   */
  public CoreAcMain holdById(CoreAcMainId coreAcMainId, TitaVo... titaVo);

  /**
   * hold By CoreAcMain
   * 
   * @param coreAcMain key
   * @param titaVo Variable-Length Argument
   * @return CoreAcMain CoreAcMain
   */
  public CoreAcMain holdById(CoreAcMain coreAcMain, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param coreAcMain Entity
   * @param titaVo Variable-Length Argument
   * @return CoreAcMain Entity
   * @throws DBException exception
   */
  public CoreAcMain insert(CoreAcMain coreAcMain, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param coreAcMain Entity
   * @param titaVo Variable-Length Argument
   * @return CoreAcMain Entity
   * @throws DBException exception
   */
  public CoreAcMain update(CoreAcMain coreAcMain, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param coreAcMain Entity
   * @param titaVo Variable-Length Argument
   * @return CoreAcMain Entity
   * @throws DBException exception
   */
  public CoreAcMain update2(CoreAcMain coreAcMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param coreAcMain Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CoreAcMain coreAcMain, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param coreAcMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CoreAcMain> coreAcMain, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param coreAcMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CoreAcMain> coreAcMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param coreAcMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CoreAcMain> coreAcMain, TitaVo... titaVo) throws DBException;

}
