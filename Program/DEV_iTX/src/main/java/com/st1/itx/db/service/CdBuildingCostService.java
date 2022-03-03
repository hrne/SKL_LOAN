package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdBuildingCost;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdBuildingCostId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdBuildingCostService {

  /**
   * findByPrimaryKey
   *
   * @param cdBuildingCostId PK
   * @param titaVo Variable-Length Argument
   * @return CdBuildingCost CdBuildingCost
   */
  public CdBuildingCost findById(CdBuildingCostId cdBuildingCostId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBuildingCost CdBuildingCost of List
   */
  public Slice<CdBuildingCost> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CityCode = ,AND FloorLowerLimit &lt;= ,AND VersionDate =
   *
   * @param cityCode_0 cityCode_0
   * @param floorLowerLimit_1 floorLowerLimit_1
   * @param versionDate_2 versionDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBuildingCost CdBuildingCost of List
   */
  public Slice<CdBuildingCost> getCost(String cityCode_0, int floorLowerLimit_1, int versionDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * CityCode = ,AND Material = ,AND VersionDate =
   *
   * @param cityCode_0 cityCode_0
   * @param material_1 material_1
   * @param versionDate_2 versionDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdBuildingCost CdBuildingCost of List
   */
  public Slice<CdBuildingCost> findCityCode(String cityCode_0, int material_1, int versionDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdBuildingCost
   * 
   * @param cdBuildingCostId key
   * @param titaVo Variable-Length Argument
   * @return CdBuildingCost CdBuildingCost
   */
  public CdBuildingCost holdById(CdBuildingCostId cdBuildingCostId, TitaVo... titaVo);

  /**
   * hold By CdBuildingCost
   * 
   * @param cdBuildingCost key
   * @param titaVo Variable-Length Argument
   * @return CdBuildingCost CdBuildingCost
   */
  public CdBuildingCost holdById(CdBuildingCost cdBuildingCost, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdBuildingCost Entity
   * @param titaVo Variable-Length Argument
   * @return CdBuildingCost Entity
   * @throws DBException exception
   */
  public CdBuildingCost insert(CdBuildingCost cdBuildingCost, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdBuildingCost Entity
   * @param titaVo Variable-Length Argument
   * @return CdBuildingCost Entity
   * @throws DBException exception
   */
  public CdBuildingCost update(CdBuildingCost cdBuildingCost, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdBuildingCost Entity
   * @param titaVo Variable-Length Argument
   * @return CdBuildingCost Entity
   * @throws DBException exception
   */
  public CdBuildingCost update2(CdBuildingCost cdBuildingCost, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdBuildingCost Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdBuildingCost cdBuildingCost, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdBuildingCost Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdBuildingCost> cdBuildingCost, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdBuildingCost Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdBuildingCost> cdBuildingCost, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdBuildingCost Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdBuildingCost> cdBuildingCost, TitaVo... titaVo) throws DBException;

}
