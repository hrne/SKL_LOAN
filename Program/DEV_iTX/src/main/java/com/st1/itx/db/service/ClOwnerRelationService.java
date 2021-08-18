package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClOwnerRelation;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClOwnerRelationId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClOwnerRelationService {

  /**
   * findByPrimaryKey
   *
   * @param clOwnerRelationId PK
   * @param titaVo Variable-Length Argument
   * @return ClOwnerRelation ClOwnerRelation
   */
  public ClOwnerRelation findById(ClOwnerRelationId clOwnerRelationId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClOwnerRelation ClOwnerRelation of List
   */
  public Slice<ClOwnerRelation> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = ,AND ApplNo =
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param applNo_3 applNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClOwnerRelation ClOwnerRelation of List
   */
  public Slice<ClOwnerRelation> ApplNoAll(int clCode1_0, int clCode2_1, int clNo_2, int applNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * hold By ClOwnerRelation
   * 
   * @param clOwnerRelationId key
   * @param titaVo Variable-Length Argument
   * @return ClOwnerRelation ClOwnerRelation
   */
  public ClOwnerRelation holdById(ClOwnerRelationId clOwnerRelationId, TitaVo... titaVo);

  /**
   * hold By ClOwnerRelation
   * 
   * @param clOwnerRelation key
   * @param titaVo Variable-Length Argument
   * @return ClOwnerRelation ClOwnerRelation
   */
  public ClOwnerRelation holdById(ClOwnerRelation clOwnerRelation, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param clOwnerRelation Entity
   * @param titaVo Variable-Length Argument
   * @return ClOwnerRelation Entity
   * @throws DBException exception
   */
  public ClOwnerRelation insert(ClOwnerRelation clOwnerRelation, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param clOwnerRelation Entity
   * @param titaVo Variable-Length Argument
   * @return ClOwnerRelation Entity
   * @throws DBException exception
   */
  public ClOwnerRelation update(ClOwnerRelation clOwnerRelation, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param clOwnerRelation Entity
   * @param titaVo Variable-Length Argument
   * @return ClOwnerRelation Entity
   * @throws DBException exception
   */
  public ClOwnerRelation update2(ClOwnerRelation clOwnerRelation, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param clOwnerRelation Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ClOwnerRelation clOwnerRelation, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param clOwnerRelation Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ClOwnerRelation> clOwnerRelation, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param clOwnerRelation Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ClOwnerRelation> clOwnerRelation, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param clOwnerRelation Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ClOwnerRelation> clOwnerRelation, TitaVo... titaVo) throws DBException;

}
