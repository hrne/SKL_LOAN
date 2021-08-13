package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BankRelationSuspected;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.BankRelationSuspectedId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BankRelationSuspectedService {

  /**
   * findByPrimaryKey
   *
   * @param bankRelationSuspectedId PK
   * @param titaVo Variable-Length Argument
   * @return BankRelationSuspected BankRelationSuspected
   */
  public BankRelationSuspected findById(BankRelationSuspectedId bankRelationSuspectedId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BankRelationSuspected BankRelationSuspected of List
   */
  public Slice<BankRelationSuspected> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * RepCusName =
   *
   * @param repCusName_0 repCusName_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BankRelationSuspected BankRelationSuspected of List
   */
  public Slice<BankRelationSuspected> RepCusNameEq(String repCusName_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By BankRelationSuspected
   * 
   * @param bankRelationSuspectedId key
   * @param titaVo Variable-Length Argument
   * @return BankRelationSuspected BankRelationSuspected
   */
  public BankRelationSuspected holdById(BankRelationSuspectedId bankRelationSuspectedId, TitaVo... titaVo);

  /**
   * hold By BankRelationSuspected
   * 
   * @param bankRelationSuspected key
   * @param titaVo Variable-Length Argument
   * @return BankRelationSuspected BankRelationSuspected
   */
  public BankRelationSuspected holdById(BankRelationSuspected bankRelationSuspected, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param bankRelationSuspected Entity
   * @param titaVo Variable-Length Argument
   * @return BankRelationSuspected Entity
   * @throws DBException exception
   */
  public BankRelationSuspected insert(BankRelationSuspected bankRelationSuspected, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param bankRelationSuspected Entity
   * @param titaVo Variable-Length Argument
   * @return BankRelationSuspected Entity
   * @throws DBException exception
   */
  public BankRelationSuspected update(BankRelationSuspected bankRelationSuspected, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param bankRelationSuspected Entity
   * @param titaVo Variable-Length Argument
   * @return BankRelationSuspected Entity
   * @throws DBException exception
   */
  public BankRelationSuspected update2(BankRelationSuspected bankRelationSuspected, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param bankRelationSuspected Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(BankRelationSuspected bankRelationSuspected, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param bankRelationSuspected Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<BankRelationSuspected> bankRelationSuspected, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param bankRelationSuspected Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<BankRelationSuspected> bankRelationSuspected, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param bankRelationSuspected Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<BankRelationSuspected> bankRelationSuspected, TitaVo... titaVo) throws DBException;

}
