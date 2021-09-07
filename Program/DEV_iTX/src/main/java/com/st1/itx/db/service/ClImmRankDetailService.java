package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClImmRankDetail;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClImmRankDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClImmRankDetailService {

  /**
   * findByPrimaryKey
   *
   * @param clImmRankDetailId PK
   * @param titaVo Variable-Length Argument
   * @return ClImmRankDetail ClImmRankDetail
   */
  public ClImmRankDetail findById(ClImmRankDetailId clImmRankDetailId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClImmRankDetail ClImmRankDetail of List
   */
  public Slice<ClImmRankDetail> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClImmRankDetail ClImmRankDetail of List
   */
  public Slice<ClImmRankDetail> clNoEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * hold By ClImmRankDetail
   * 
   * @param clImmRankDetailId key
   * @param titaVo Variable-Length Argument
   * @return ClImmRankDetail ClImmRankDetail
   */
  public ClImmRankDetail holdById(ClImmRankDetailId clImmRankDetailId, TitaVo... titaVo);

  /**
   * hold By ClImmRankDetail
   * 
   * @param clImmRankDetail key
   * @param titaVo Variable-Length Argument
   * @return ClImmRankDetail ClImmRankDetail
   */
  public ClImmRankDetail holdById(ClImmRankDetail clImmRankDetail, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param clImmRankDetail Entity
   * @param titaVo Variable-Length Argument
   * @return ClImmRankDetail Entity
   * @throws DBException exception
   */
  public ClImmRankDetail insert(ClImmRankDetail clImmRankDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param clImmRankDetail Entity
   * @param titaVo Variable-Length Argument
   * @return ClImmRankDetail Entity
   * @throws DBException exception
   */
  public ClImmRankDetail update(ClImmRankDetail clImmRankDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param clImmRankDetail Entity
   * @param titaVo Variable-Length Argument
   * @return ClImmRankDetail Entity
   * @throws DBException exception
   */
  public ClImmRankDetail update2(ClImmRankDetail clImmRankDetail, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param clImmRankDetail Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ClImmRankDetail clImmRankDetail, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param clImmRankDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ClImmRankDetail> clImmRankDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param clImmRankDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ClImmRankDetail> clImmRankDetail, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param clImmRankDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ClImmRankDetail> clImmRankDetail, TitaVo... titaVo) throws DBException;

}
