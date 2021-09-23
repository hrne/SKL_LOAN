package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClParkingType;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClParkingTypeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClParkingTypeService {

  /**
   * findByPrimaryKey
   *
   * @param clParkingTypeId PK
   * @param titaVo Variable-Length Argument
   * @return ClParkingType ClParkingType
   */
  public ClParkingType findById(ClParkingTypeId clParkingTypeId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClParkingType ClParkingType of List
   */
  public Slice<ClParkingType> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClParkingType ClParkingType of List
   */
  public Slice<ClParkingType> clNoEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By ClParkingType
   * 
   * @param clParkingTypeId key
   * @param titaVo Variable-Length Argument
   * @return ClParkingType ClParkingType
   */
  public ClParkingType holdById(ClParkingTypeId clParkingTypeId, TitaVo... titaVo);

  /**
   * hold By ClParkingType
   * 
   * @param clParkingType key
   * @param titaVo Variable-Length Argument
   * @return ClParkingType ClParkingType
   */
  public ClParkingType holdById(ClParkingType clParkingType, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param clParkingType Entity
   * @param titaVo Variable-Length Argument
   * @return ClParkingType Entity
   * @throws DBException exception
   */
  public ClParkingType insert(ClParkingType clParkingType, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param clParkingType Entity
   * @param titaVo Variable-Length Argument
   * @return ClParkingType Entity
   * @throws DBException exception
   */
  public ClParkingType update(ClParkingType clParkingType, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param clParkingType Entity
   * @param titaVo Variable-Length Argument
   * @return ClParkingType Entity
   * @throws DBException exception
   */
  public ClParkingType update2(ClParkingType clParkingType, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param clParkingType Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ClParkingType clParkingType, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param clParkingType Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ClParkingType> clParkingType, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param clParkingType Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ClParkingType> clParkingType, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param clParkingType Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ClParkingType> clParkingType, TitaVo... titaVo) throws DBException;

}
