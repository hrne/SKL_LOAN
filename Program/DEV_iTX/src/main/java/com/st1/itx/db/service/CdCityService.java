package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCity;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdCityService {

  /**
   * findByPrimaryKey
   *
   * @param cityCode PK
   * @param titaVo Variable-Length Argument
   * @return CdCity CdCity
   */
  public CdCity findById(String cityCode, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCity CdCity of List
   */
  public Slice<CdCity> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * UnitCode &gt;= ,AND UnitCode &lt;=
   *
   * @param unitCode_0 unitCode_0
   * @param unitCode_1 unitCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCity CdCity of List
   */
  public Slice<CdCity> findUnitCode(String unitCode_0, String unitCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * CityCode &gt;= ,AND CityCode &lt;=
   *
   * @param cityCode_0 cityCode_0
   * @param cityCode_1 cityCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCity CdCity of List
   */
  public Slice<CdCity> findCityCode(String cityCode_0, String cityCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdCity
   * 
   * @param cityCode key
   * @param titaVo Variable-Length Argument
   * @return CdCity CdCity
   */
  public CdCity holdById(String cityCode, TitaVo... titaVo);

  /**
   * hold By CdCity
   * 
   * @param cdCity key
   * @param titaVo Variable-Length Argument
   * @return CdCity CdCity
   */
  public CdCity holdById(CdCity cdCity, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdCity Entity
   * @param titaVo Variable-Length Argument
   * @return CdCity Entity
   * @throws DBException exception
   */
  public CdCity insert(CdCity cdCity, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdCity Entity
   * @param titaVo Variable-Length Argument
   * @return CdCity Entity
   * @throws DBException exception
   */
  public CdCity update(CdCity cdCity, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdCity Entity
   * @param titaVo Variable-Length Argument
   * @return CdCity Entity
   * @throws DBException exception
   */
  public CdCity update2(CdCity cdCity, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdCity Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdCity cdCity, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdCity Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdCity> cdCity, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdCity Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdCity> cdCity, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdCity Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdCity> cdCity, TitaVo... titaVo) throws DBException;

}
