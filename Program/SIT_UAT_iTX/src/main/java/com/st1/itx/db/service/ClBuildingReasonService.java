package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClBuildingReason;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClBuildingReasonId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClBuildingReasonService {

  /**
   * findByPrimaryKey
   *
   * @param clBuildingReasonId PK
   * @param titaVo Variable-Length Argument
   * @return ClBuildingReason ClBuildingReason
   */
  public ClBuildingReason findById(ClBuildingReasonId clBuildingReasonId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClBuildingReason ClBuildingReason of List
   */
  public Slice<ClBuildingReason> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param titaVo Variable-Length Argument
   * @return Slice ClBuildingReason ClBuildingReason of List
   */
  public ClBuildingReason clNoFirst(int clCode1_0, int clCode2_1, int clNo_2, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClBuildingReason ClBuildingReason of List
   */
  public Slice<ClBuildingReason> clNoEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By ClBuildingReason
   * 
   * @param clBuildingReasonId key
   * @param titaVo Variable-Length Argument
   * @return ClBuildingReason ClBuildingReason
   */
  public ClBuildingReason holdById(ClBuildingReasonId clBuildingReasonId, TitaVo... titaVo);

  /**
   * hold By ClBuildingReason
   * 
   * @param clBuildingReason key
   * @param titaVo Variable-Length Argument
   * @return ClBuildingReason ClBuildingReason
   */
  public ClBuildingReason holdById(ClBuildingReason clBuildingReason, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param clBuildingReason Entity
   * @param titaVo Variable-Length Argument
   * @return ClBuildingReason Entity
   * @throws DBException exception
   */
  public ClBuildingReason insert(ClBuildingReason clBuildingReason, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param clBuildingReason Entity
   * @param titaVo Variable-Length Argument
   * @return ClBuildingReason Entity
   * @throws DBException exception
   */
  public ClBuildingReason update(ClBuildingReason clBuildingReason, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param clBuildingReason Entity
   * @param titaVo Variable-Length Argument
   * @return ClBuildingReason Entity
   * @throws DBException exception
   */
  public ClBuildingReason update2(ClBuildingReason clBuildingReason, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param clBuildingReason Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ClBuildingReason clBuildingReason, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param clBuildingReason Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ClBuildingReason> clBuildingReason, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param clBuildingReason Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ClBuildingReason> clBuildingReason, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param clBuildingReason Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ClBuildingReason> clBuildingReason, TitaVo... titaVo) throws DBException;

}
