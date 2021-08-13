package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FacShareMain;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.FacShareMainId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacShareMainService {

  /**
   * findByPrimaryKey
   *
   * @param facShareMainId PK
   * @param titaVo Variable-Length Argument
   * @return FacShareMain FacShareMain
   */
  public FacShareMain findById(FacShareMainId facShareMainId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacShareMain FacShareMain of List
   */
  public Slice<FacShareMain> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CreditSysNo = 
   *
   * @param creditSysNo_0 creditSysNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FacShareMain FacShareMain of List
   */
  public Slice<FacShareMain> findCreditSysNoEq(int creditSysNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By FacShareMain
   * 
   * @param facShareMainId key
   * @param titaVo Variable-Length Argument
   * @return FacShareMain FacShareMain
   */
  public FacShareMain holdById(FacShareMainId facShareMainId, TitaVo... titaVo);

  /**
   * hold By FacShareMain
   * 
   * @param facShareMain key
   * @param titaVo Variable-Length Argument
   * @return FacShareMain FacShareMain
   */
  public FacShareMain holdById(FacShareMain facShareMain, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param facShareMain Entity
   * @param titaVo Variable-Length Argument
   * @return FacShareMain Entity
   * @throws DBException exception
   */
  public FacShareMain insert(FacShareMain facShareMain, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param facShareMain Entity
   * @param titaVo Variable-Length Argument
   * @return FacShareMain Entity
   * @throws DBException exception
   */
  public FacShareMain update(FacShareMain facShareMain, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param facShareMain Entity
   * @param titaVo Variable-Length Argument
   * @return FacShareMain Entity
   * @throws DBException exception
   */
  public FacShareMain update2(FacShareMain facShareMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param facShareMain Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(FacShareMain facShareMain, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param facShareMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<FacShareMain> facShareMain, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param facShareMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<FacShareMain> facShareMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param facShareMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<FacShareMain> facShareMain, TitaVo... titaVo) throws DBException;

}
