package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BankRelationSelf;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.BankRelationSelfId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BankRelationSelfService {

  /**
   * findByPrimaryKey
   *
   * @param bankRelationSelfId PK
   * @param titaVo Variable-Length Argument
   * @return BankRelationSelf BankRelationSelf
   */
  public BankRelationSelf findById(BankRelationSelfId bankRelationSelfId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BankRelationSelf BankRelationSelf of List
   */
  public Slice<BankRelationSelf> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId =
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BankRelationSelf BankRelationSelf of List
   */
  public Slice<BankRelationSelf> findCustIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By BankRelationSelf
   * 
   * @param bankRelationSelfId key
   * @param titaVo Variable-Length Argument
   * @return BankRelationSelf BankRelationSelf
   */
  public BankRelationSelf holdById(BankRelationSelfId bankRelationSelfId, TitaVo... titaVo);

  /**
   * hold By BankRelationSelf
   * 
   * @param bankRelationSelf key
   * @param titaVo Variable-Length Argument
   * @return BankRelationSelf BankRelationSelf
   */
  public BankRelationSelf holdById(BankRelationSelf bankRelationSelf, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param bankRelationSelf Entity
   * @param titaVo Variable-Length Argument
   * @return BankRelationSelf Entity
   * @throws DBException exception
   */
  public BankRelationSelf insert(BankRelationSelf bankRelationSelf, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param bankRelationSelf Entity
   * @param titaVo Variable-Length Argument
   * @return BankRelationSelf Entity
   * @throws DBException exception
   */
  public BankRelationSelf update(BankRelationSelf bankRelationSelf, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param bankRelationSelf Entity
   * @param titaVo Variable-Length Argument
   * @return BankRelationSelf Entity
   * @throws DBException exception
   */
  public BankRelationSelf update2(BankRelationSelf bankRelationSelf, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param bankRelationSelf Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(BankRelationSelf bankRelationSelf, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param bankRelationSelf Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<BankRelationSelf> bankRelationSelf, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param bankRelationSelf Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<BankRelationSelf> bankRelationSelf, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param bankRelationSelf Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<BankRelationSelf> bankRelationSelf, TitaVo... titaVo) throws DBException;

}
