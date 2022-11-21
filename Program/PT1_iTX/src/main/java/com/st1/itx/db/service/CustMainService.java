package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustMain;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustMainService {

  /**
   * findByPrimaryKey
   *
   * @param custUKey PK
   * @param titaVo Variable-Length Argument
   * @return CustMain CustMain
   */
  public CustMain findById(String custUKey, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustMain CustMain of List
   */
  public Slice<CustMain> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId %
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustMain CustMain of List
   */
  public Slice<CustMain> custIdLike(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId =
   *
   * @param custId_0 custId_0
   * @param titaVo Variable-Length Argument
   * @return Slice CustMain CustMain of List
   */
  public CustMain custIdFirst(String custId_0, TitaVo... titaVo);

  /**
   * CustNo &gt;= ,AND CustNo &lt;=
   *
   * @param custNo_0 custNo_0
   * @param custNo_1 custNo_1
   * @param titaVo Variable-Length Argument
   * @return Slice CustMain CustMain of List
   */
  public CustMain custNoFirst(int custNo_0, int custNo_1, TitaVo... titaVo);

  /**
   * CustNo &gt;= ,AND CustNo &lt;=
   *
   * @param custNo_0 custNo_0
   * @param custNo_1 custNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustMain CustMain of List
   */
  public Slice<CustMain> custNoRange(int custNo_0, int custNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustName =
   *
   * @param custName_0 custName_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustMain CustMain of List
   */
  public Slice<CustMain> custNameEq(String custName_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustName %
   *
   * @param custName_0 custName_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustMain CustMain of List
   */
  public Slice<CustMain> custNameLike(String custName_0, int index, int limit, TitaVo... titaVo);

  /**
   * EmpNo =
   *
   * @param empNo_0 empNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustMain CustMain of List
   */
  public Slice<CustMain> empNoEq(String empNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * EmpNo &gt;= 
   *
   * @param empNo_0 empNo_0
   * @param titaVo Variable-Length Argument
   * @return Slice CustMain CustMain of List
   */
  public CustMain empNoFirst(String empNo_0, TitaVo... titaVo);

  /**
   * IndustryCode =
   *
   * @param industryCode_0 industryCode_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CustMain CustMain of List
   */
  public Slice<CustMain> industryCodeAll(String industryCode_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CustMain
   * 
   * @param custUKey key
   * @param titaVo Variable-Length Argument
   * @return CustMain CustMain
   */
  public CustMain holdById(String custUKey, TitaVo... titaVo);

  /**
   * hold By CustMain
   * 
   * @param custMain key
   * @param titaVo Variable-Length Argument
   * @return CustMain CustMain
   */
  public CustMain holdById(CustMain custMain, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param custMain Entity
   * @param titaVo Variable-Length Argument
   * @return CustMain Entity
   * @throws DBException exception
   */
  public CustMain insert(CustMain custMain, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param custMain Entity
   * @param titaVo Variable-Length Argument
   * @return CustMain Entity
   * @throws DBException exception
   */
  public CustMain update(CustMain custMain, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param custMain Entity
   * @param titaVo Variable-Length Argument
   * @return CustMain Entity
   * @throws DBException exception
   */
  public CustMain update2(CustMain custMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param custMain Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CustMain custMain, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param custMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CustMain> custMain, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param custMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CustMain> custMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param custMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CustMain> custMain, TitaVo... titaVo) throws DBException;

}
