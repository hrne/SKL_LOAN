package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClEva;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClEvaId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClEvaService {

  /**
   * findByPrimaryKey
   *
   * @param clEvaId PK
   * @param titaVo Variable-Length Argument
   * @return ClEva ClEva
   */
  public ClEva findById(ClEvaId clEvaId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClEva ClEva of List
   */
  public Slice<ClEva> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = ,AND  EvaNo &lt;&gt;
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param evaNo_3 evaNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClEva ClEva of List
   */
  public Slice<ClEva> findClNo(int clCode1_0, int clCode2_1, int clNo_2, int evaNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param titaVo Variable-Length Argument
   * @return Slice ClEva ClEva of List
   */
  public ClEva ClNoFirst(int clCode1_0, int clCode2_1, int clNo_2, TitaVo... titaVo);

  /**
   * hold By ClEva
   * 
   * @param clEvaId key
   * @param titaVo Variable-Length Argument
   * @return ClEva ClEva
   */
  public ClEva holdById(ClEvaId clEvaId, TitaVo... titaVo);

  /**
   * hold By ClEva
   * 
   * @param clEva key
   * @param titaVo Variable-Length Argument
   * @return ClEva ClEva
   */
  public ClEva holdById(ClEva clEva, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param clEva Entity
   * @param titaVo Variable-Length Argument
   * @return ClEva Entity
   * @throws DBException exception
   */
  public ClEva insert(ClEva clEva, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param clEva Entity
   * @param titaVo Variable-Length Argument
   * @return ClEva Entity
   * @throws DBException exception
   */
  public ClEva update(ClEva clEva, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param clEva Entity
   * @param titaVo Variable-Length Argument
   * @return ClEva Entity
   * @throws DBException exception
   */
  public ClEva update2(ClEva clEva, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param clEva Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ClEva clEva, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param clEva Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ClEva> clEva, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param clEva Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ClEva> clEva, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param clEva Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ClEva> clEva, TitaVo... titaVo) throws DBException;

}
