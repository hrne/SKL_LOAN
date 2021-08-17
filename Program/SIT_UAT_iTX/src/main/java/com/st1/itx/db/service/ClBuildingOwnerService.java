package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClBuildingOwner;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClBuildingOwnerId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClBuildingOwnerService {

  /**
   * findByPrimaryKey
   *
   * @param clBuildingOwnerId PK
   * @param titaVo Variable-Length Argument
   * @return ClBuildingOwner ClBuildingOwner
   */
  public ClBuildingOwner findById(ClBuildingOwnerId clBuildingOwnerId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClBuildingOwner ClBuildingOwner of List
   */
  public Slice<ClBuildingOwner> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClBuildingOwner ClBuildingOwner of List
   */
  public Slice<ClBuildingOwner> clNoEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * OwnerCustUKey = 
   *
   * @param ownerCustUKey_0 ownerCustUKey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClBuildingOwner ClBuildingOwner of List
   */
  public Slice<ClBuildingOwner> OwnerCustUKeyEq(String ownerCustUKey_0, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param titaVo Variable-Length Argument
   * @return Slice ClBuildingOwner ClBuildingOwner of List
   */
  public ClBuildingOwner clNoFirst(int clCode1_0, int clCode2_1, int clNo_2, TitaVo... titaVo);

  /**
   * hold By ClBuildingOwner
   * 
   * @param clBuildingOwnerId key
   * @param titaVo Variable-Length Argument
   * @return ClBuildingOwner ClBuildingOwner
   */
  public ClBuildingOwner holdById(ClBuildingOwnerId clBuildingOwnerId, TitaVo... titaVo);

  /**
   * hold By ClBuildingOwner
   * 
   * @param clBuildingOwner key
   * @param titaVo Variable-Length Argument
   * @return ClBuildingOwner ClBuildingOwner
   */
  public ClBuildingOwner holdById(ClBuildingOwner clBuildingOwner, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param clBuildingOwner Entity
   * @param titaVo Variable-Length Argument
   * @return ClBuildingOwner Entity
   * @throws DBException exception
   */
  public ClBuildingOwner insert(ClBuildingOwner clBuildingOwner, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param clBuildingOwner Entity
   * @param titaVo Variable-Length Argument
   * @return ClBuildingOwner Entity
   * @throws DBException exception
   */
  public ClBuildingOwner update(ClBuildingOwner clBuildingOwner, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param clBuildingOwner Entity
   * @param titaVo Variable-Length Argument
   * @return ClBuildingOwner Entity
   * @throws DBException exception
   */
  public ClBuildingOwner update2(ClBuildingOwner clBuildingOwner, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param clBuildingOwner Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ClBuildingOwner clBuildingOwner, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param clBuildingOwner Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ClBuildingOwner> clBuildingOwner, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param clBuildingOwner Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ClBuildingOwner> clBuildingOwner, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param clBuildingOwner Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ClBuildingOwner> clBuildingOwner, TitaVo... titaVo) throws DBException;

}
