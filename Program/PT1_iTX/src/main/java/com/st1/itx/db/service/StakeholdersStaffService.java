package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.StakeholdersStaff;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface StakeholdersStaffService {

  /**
   * findByPrimaryKey
   *
   * @param staffId PK
   * @param titaVo Variable-Length Argument
   * @return StakeholdersStaff StakeholdersStaff
   */
  public StakeholdersStaff findById(String staffId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice StakeholdersStaff StakeholdersStaff of List
   */
  public Slice<StakeholdersStaff> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By StakeholdersStaff
   * 
   * @param staffId key
   * @param titaVo Variable-Length Argument
   * @return StakeholdersStaff StakeholdersStaff
   */
  public StakeholdersStaff holdById(String staffId, TitaVo... titaVo);

  /**
   * hold By StakeholdersStaff
   * 
   * @param stakeholdersStaff key
   * @param titaVo Variable-Length Argument
   * @return StakeholdersStaff StakeholdersStaff
   */
  public StakeholdersStaff holdById(StakeholdersStaff stakeholdersStaff, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param stakeholdersStaff Entity
   * @param titaVo Variable-Length Argument
   * @return StakeholdersStaff Entity
   * @throws DBException exception
   */
  public StakeholdersStaff insert(StakeholdersStaff stakeholdersStaff, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param stakeholdersStaff Entity
   * @param titaVo Variable-Length Argument
   * @return StakeholdersStaff Entity
   * @throws DBException exception
   */
  public StakeholdersStaff update(StakeholdersStaff stakeholdersStaff, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param stakeholdersStaff Entity
   * @param titaVo Variable-Length Argument
   * @return StakeholdersStaff Entity
   * @throws DBException exception
   */
  public StakeholdersStaff update2(StakeholdersStaff stakeholdersStaff, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param stakeholdersStaff Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(StakeholdersStaff stakeholdersStaff, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param stakeholdersStaff Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<StakeholdersStaff> stakeholdersStaff, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param stakeholdersStaff Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<StakeholdersStaff> stakeholdersStaff, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param stakeholdersStaff Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<StakeholdersStaff> stakeholdersStaff, TitaVo... titaVo) throws DBException;

}
