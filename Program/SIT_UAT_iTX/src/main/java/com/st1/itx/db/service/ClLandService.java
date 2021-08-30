package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClLand;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClLandId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClLandService {

  /**
   * findByPrimaryKey
   *
   * @param clLandId PK
   * @param titaVo Variable-Length Argument
   * @return ClLand ClLand
   */
  public ClLand findById(ClLandId clLandId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClLand ClLand of List
   */
  public Slice<ClLand> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = 
   *
   * @param clCode1_0 clCode1_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClLand ClLand of List
   */
  public Slice<ClLand> findClCode1(int clCode1_0, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClLand ClLand of List
   */
  public Slice<ClLand> findClCode2(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClLand ClLand of List
   */
  public Slice<ClLand> findClNo(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * CityCode = ,AND AreaCode = ,AND IrCode = ,AND LandNo1 &gt;= ,AND LandNo1 &lt;= ,AND LandNo2 &gt;= ,AND LandNo2 &lt;=
   *
   * @param cityCode_0 cityCode_0
   * @param areaCode_1 areaCode_1
   * @param irCode_2 irCode_2
   * @param landNo1_3 landNo1_3
   * @param landNo1_4 landNo1_4
   * @param landNo2_5 landNo2_5
   * @param landNo2_6 landNo2_6
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClLand ClLand of List
   */
  public Slice<ClLand> findIrCode(String cityCode_0, String areaCode_1, String irCode_2, String landNo1_3, String landNo1_4, String landNo2_5, String landNo2_6, int index, int limit, TitaVo... titaVo);

  /**
   * CityCode = ,AND AreaCode = ,AND IrCode = ,AND LandNo1 = ,AND LandNo2 = 
   *
   * @param cityCode_0 cityCode_0
   * @param areaCode_1 areaCode_1
   * @param irCode_2 irCode_2
   * @param landNo1_3 landNo1_3
   * @param landNo2_4 landNo2_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClLand ClLand of List
   */
  public Slice<ClLand> findLandLocationEq(String cityCode_0, String areaCode_1, String irCode_2, String landNo1_3, String landNo2_4, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param titaVo Variable-Length Argument
   * @return Slice ClLand ClLand of List
   */
  public ClLand findClNoFirst(int clCode1_0, int clCode2_1, int clNo_2, TitaVo... titaVo);

  /**
   * hold By ClLand
   * 
   * @param clLandId key
   * @param titaVo Variable-Length Argument
   * @return ClLand ClLand
   */
  public ClLand holdById(ClLandId clLandId, TitaVo... titaVo);

  /**
   * hold By ClLand
   * 
   * @param clLand key
   * @param titaVo Variable-Length Argument
   * @return ClLand ClLand
   */
  public ClLand holdById(ClLand clLand, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param clLand Entity
   * @param titaVo Variable-Length Argument
   * @return ClLand Entity
   * @throws DBException exception
   */
  public ClLand insert(ClLand clLand, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param clLand Entity
   * @param titaVo Variable-Length Argument
   * @return ClLand Entity
   * @throws DBException exception
   */
  public ClLand update(ClLand clLand, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param clLand Entity
   * @param titaVo Variable-Length Argument
   * @return ClLand Entity
   * @throws DBException exception
   */
  public ClLand update2(ClLand clLand, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param clLand Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ClLand clLand, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param clLand Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ClLand> clLand, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param clLand Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ClLand> clLand, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param clLand Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ClLand> clLand, TitaVo... titaVo) throws DBException;

}
