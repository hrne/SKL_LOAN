package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustRelMain;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustRelMainService {

  /**
   * findByPrimaryKey
   *
   * @param ukey PK
   * @param titaVo Variable-Length Argument
   * @return CustRelMain CustRelMain
   */
  public CustRelMain findById(String ukey, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustRelMain CustRelMain of List
   */
  public Slice<CustRelMain> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustRelId=
   *
   * @param custRelId_0 custRelId_0
   * @param titaVo Variable-Length Argument
   * @return Slice CustRelMain CustRelMain of List
   */
  public CustRelMain custRelIdFirst(String custRelId_0, TitaVo... titaVo);

  /**
   * CustRelName=
   *
   * @param custRelName_0 custRelName_0
   * @param titaVo Variable-Length Argument
   * @return Slice CustRelMain CustRelMain of List
   */
  public CustRelMain custRelNameFirst(String custRelName_0, TitaVo... titaVo);

  /**
   * hold By CustRelMain
   * 
   * @param ukey key
   * @param titaVo Variable-Length Argument
   * @return CustRelMain CustRelMain
   */
  public CustRelMain holdById(String ukey, TitaVo... titaVo);

  /**
   * hold By CustRelMain
   * 
   * @param custRelMain key
   * @param titaVo Variable-Length Argument
   * @return CustRelMain CustRelMain
   */
  public CustRelMain holdById(CustRelMain custRelMain, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param custRelMain Entity
   * @param titaVo Variable-Length Argument
   * @return CustRelMain Entity
   * @throws DBException exception
   */
  public CustRelMain insert(CustRelMain custRelMain, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param custRelMain Entity
   * @param titaVo Variable-Length Argument
   * @return CustRelMain Entity
   * @throws DBException exception
   */
  public CustRelMain update(CustRelMain custRelMain, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param custRelMain Entity
   * @param titaVo Variable-Length Argument
   * @return CustRelMain Entity
   * @throws DBException exception
   */
  public CustRelMain update2(CustRelMain custRelMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param custRelMain Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CustRelMain custRelMain, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param custRelMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CustRelMain> custRelMain, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param custRelMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CustRelMain> custRelMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param custRelMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CustRelMain> custRelMain, TitaVo... titaVo) throws DBException;

}
