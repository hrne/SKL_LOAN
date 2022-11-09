package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCityRate;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdCityRateId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdCityRateService {

  /**
   * findByPrimaryKey
   *
   * @param cdCityRateId PK
   * @param titaVo Variable-Length Argument
   * @return CdCityRate CdCityRate
   */
  public CdCityRate findById(CdCityRateId cdCityRateId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCityRate CdCityRate of List
   */
  public Slice<CdCityRate> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CityCode = ,AND EffectYYMM &gt;= ,AND EffectYYMM &lt;=
   *
   * @param cityCode_0 cityCode_0
   * @param effectYYMM_1 effectYYMM_1
   * @param effectYYMM_2 effectYYMM_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCityRate CdCityRate of List
   */
  public Slice<CdCityRate> findCityCodeEq(String cityCode_0, int effectYYMM_1, int effectYYMM_2, int index, int limit, TitaVo... titaVo);

  /**
   * EffectYYMM =
   *
   * @param effectYYMM_0 effectYYMM_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCityRate CdCityRate of List
   */
  public Slice<CdCityRate> findEffectDateEq(int effectYYMM_0, int index, int limit, TitaVo... titaVo);

  /**
   * EffectYYMM &gt;= ,AND EffectYYMM &lt;=
   *
   * @param effectYYMM_0 effectYYMM_0
   * @param effectYYMM_1 effectYYMM_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCityRate CdCityRate of List
   */
  public Slice<CdCityRate> findEffectDateRange(int effectYYMM_0, int effectYYMM_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdCityRate
   * 
   * @param cdCityRateId key
   * @param titaVo Variable-Length Argument
   * @return CdCityRate CdCityRate
   */
  public CdCityRate holdById(CdCityRateId cdCityRateId, TitaVo... titaVo);

  /**
   * hold By CdCityRate
   * 
   * @param cdCityRate key
   * @param titaVo Variable-Length Argument
   * @return CdCityRate CdCityRate
   */
  public CdCityRate holdById(CdCityRate cdCityRate, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdCityRate Entity
   * @param titaVo Variable-Length Argument
   * @return CdCityRate Entity
   * @throws DBException exception
   */
  public CdCityRate insert(CdCityRate cdCityRate, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdCityRate Entity
   * @param titaVo Variable-Length Argument
   * @return CdCityRate Entity
   * @throws DBException exception
   */
  public CdCityRate update(CdCityRate cdCityRate, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdCityRate Entity
   * @param titaVo Variable-Length Argument
   * @return CdCityRate Entity
   * @throws DBException exception
   */
  public CdCityRate update2(CdCityRate cdCityRate, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdCityRate Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdCityRate cdCityRate, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdCityRate Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdCityRate> cdCityRate, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdCityRate Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdCityRate> cdCityRate, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdCityRate Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdCityRate> cdCityRate, TitaVo... titaVo) throws DBException;

}
