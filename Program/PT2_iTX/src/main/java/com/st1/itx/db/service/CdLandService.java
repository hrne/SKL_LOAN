package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdLand;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdLandId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdLandService {

  /**
   * findByPrimaryKey
   *
   * @param cdLandId PK
   * @param titaVo Variable-Length Argument
   * @return CdLand CdLand
   */
  public CdLand findById(CdLandId cdLandId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdLand CdLand of List
   */
  public Slice<CdLand> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CityCode =
   *
   * @param cityCode_0 cityCode_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdLand CdLand of List
   */
  public Slice<CdLand> findCityCodeEq(String cityCode_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdLand
   * 
   * @param cdLandId key
   * @param titaVo Variable-Length Argument
   * @return CdLand CdLand
   */
  public CdLand holdById(CdLandId cdLandId, TitaVo... titaVo);

  /**
   * hold By CdLand
   * 
   * @param cdLand key
   * @param titaVo Variable-Length Argument
   * @return CdLand CdLand
   */
  public CdLand holdById(CdLand cdLand, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdLand Entity
   * @param titaVo Variable-Length Argument
   * @return CdLand Entity
   * @throws DBException exception
   */
  public CdLand insert(CdLand cdLand, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdLand Entity
   * @param titaVo Variable-Length Argument
   * @return CdLand Entity
   * @throws DBException exception
   */
  public CdLand update(CdLand cdLand, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdLand Entity
   * @param titaVo Variable-Length Argument
   * @return CdLand Entity
   * @throws DBException exception
   */
  public CdLand update2(CdLand cdLand, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdLand Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdLand cdLand, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdLand Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdLand> cdLand, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdLand Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdLand> cdLand, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdLand Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdLand> cdLand, TitaVo... titaVo) throws DBException;

}
