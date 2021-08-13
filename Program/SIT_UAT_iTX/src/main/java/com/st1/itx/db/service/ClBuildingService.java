package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClBuilding;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClBuildingId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClBuildingService {

  /**
   * findByPrimaryKey
   *
   * @param clBuildingId PK
   * @param titaVo Variable-Length Argument
   * @return ClBuilding ClBuilding
   */
  public ClBuilding findById(ClBuildingId clBuildingId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClBuilding ClBuilding of List
   */
  public Slice<ClBuilding> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = 
   *
   * @param clCode1_0 clCode1_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClBuilding ClBuilding of List
   */
  public Slice<ClBuilding> findClCode1(int clCode1_0, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClBuilding ClBuilding of List
   */
  public Slice<ClBuilding> findClCode2(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = ,AND ClNo = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param clNo_2 clNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClBuilding ClBuilding of List
   */
  public Slice<ClBuilding> findClNo(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * BdLocation = 
   *
   * @param bdLocation_0 bdLocation_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClBuilding ClBuilding of List
   */
  public Slice<ClBuilding> findBdLocationEq(String bdLocation_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By ClBuilding
   * 
   * @param clBuildingId key
   * @param titaVo Variable-Length Argument
   * @return ClBuilding ClBuilding
   */
  public ClBuilding holdById(ClBuildingId clBuildingId, TitaVo... titaVo);

  /**
   * hold By ClBuilding
   * 
   * @param clBuilding key
   * @param titaVo Variable-Length Argument
   * @return ClBuilding ClBuilding
   */
  public ClBuilding holdById(ClBuilding clBuilding, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param clBuilding Entity
   * @param titaVo Variable-Length Argument
   * @return ClBuilding Entity
   * @throws DBException exception
   */
  public ClBuilding insert(ClBuilding clBuilding, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param clBuilding Entity
   * @param titaVo Variable-Length Argument
   * @return ClBuilding Entity
   * @throws DBException exception
   */
  public ClBuilding update(ClBuilding clBuilding, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param clBuilding Entity
   * @param titaVo Variable-Length Argument
   * @return ClBuilding Entity
   * @throws DBException exception
   */
  public ClBuilding update2(ClBuilding clBuilding, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param clBuilding Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ClBuilding clBuilding, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param clBuilding Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ClBuilding> clBuilding, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param clBuilding Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ClBuilding> clBuilding, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param clBuilding Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ClBuilding> clBuilding, TitaVo... titaVo) throws DBException;

}
