package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClLandOwner;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClLandOwnerId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClLandOwnerService {

  /**
   * findByPrimaryKey
   *
   * @param clLandOwnerId PK
   * @param titaVo Variable-Length Argument
   * @return ClLandOwner ClLandOwner
   */
  public ClLandOwner findById(ClLandOwnerId clLandOwnerId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClLandOwner ClLandOwner of List
   */
  public Slice<ClLandOwner> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = ,AND LandSeq =
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param landSeq_3 landSeq_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClLandOwner ClLandOwner of List
   */
  public Slice<ClLandOwner> LandSeqEq(int clCode1_0, int clCode2_1, int clNo_2, int landSeq_3, int index, int limit, TitaVo... titaVo);

  /**
   * OwnerCustUKey = 
   *
   * @param ownerCustUKey_0 ownerCustUKey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClLandOwner ClLandOwner of List
   */
  public Slice<ClLandOwner> OwnerCustUKeyEq(String ownerCustUKey_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By ClLandOwner
   * 
   * @param clLandOwnerId key
   * @param titaVo Variable-Length Argument
   * @return ClLandOwner ClLandOwner
   */
  public ClLandOwner holdById(ClLandOwnerId clLandOwnerId, TitaVo... titaVo);

  /**
   * hold By ClLandOwner
   * 
   * @param clLandOwner key
   * @param titaVo Variable-Length Argument
   * @return ClLandOwner ClLandOwner
   */
  public ClLandOwner holdById(ClLandOwner clLandOwner, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param clLandOwner Entity
   * @param titaVo Variable-Length Argument
   * @return ClLandOwner Entity
   * @throws DBException exception
   */
  public ClLandOwner insert(ClLandOwner clLandOwner, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param clLandOwner Entity
   * @param titaVo Variable-Length Argument
   * @return ClLandOwner Entity
   * @throws DBException exception
   */
  public ClLandOwner update(ClLandOwner clLandOwner, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param clLandOwner Entity
   * @param titaVo Variable-Length Argument
   * @return ClLandOwner Entity
   * @throws DBException exception
   */
  public ClLandOwner update2(ClLandOwner clLandOwner, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param clLandOwner Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ClLandOwner clLandOwner, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param clLandOwner Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ClLandOwner> clLandOwner, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param clLandOwner Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ClLandOwner> clLandOwner, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param clLandOwner Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ClLandOwner> clLandOwner, TitaVo... titaVo) throws DBException;

}
