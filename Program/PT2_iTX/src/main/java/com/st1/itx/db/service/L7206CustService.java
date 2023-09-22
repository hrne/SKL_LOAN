package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.L7206Cust;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface L7206CustService {

  /**
   * findByPrimaryKey
   *
   * @param logNo PK
   * @param titaVo Variable-Length Argument
   * @return L7206Cust L7206Cust
   */
  public L7206Cust findById(Long logNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice L7206Cust L7206Cust of List
   */
  public Slice<L7206Cust> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By L7206Cust
   * 
   * @param logNo key
   * @param titaVo Variable-Length Argument
   * @return L7206Cust L7206Cust
   */
  public L7206Cust holdById(Long logNo, TitaVo... titaVo);

  /**
   * hold By L7206Cust
   * 
   * @param l7206Cust key
   * @param titaVo Variable-Length Argument
   * @return L7206Cust L7206Cust
   */
  public L7206Cust holdById(L7206Cust l7206Cust, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param l7206Cust Entity
   * @param titaVo Variable-Length Argument
   * @return L7206Cust Entity
   * @throws DBException exception
   */
  public L7206Cust insert(L7206Cust l7206Cust, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param l7206Cust Entity
   * @param titaVo Variable-Length Argument
   * @return L7206Cust Entity
   * @throws DBException exception
   */
  public L7206Cust update(L7206Cust l7206Cust, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param l7206Cust Entity
   * @param titaVo Variable-Length Argument
   * @return L7206Cust Entity
   * @throws DBException exception
   */
  public L7206Cust update2(L7206Cust l7206Cust, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param l7206Cust Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(L7206Cust l7206Cust, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param l7206Cust Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<L7206Cust> l7206Cust, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param l7206Cust Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<L7206Cust> l7206Cust, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param l7206Cust Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<L7206Cust> l7206Cust, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * 更新利害關係人借款人資料
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_L7206Cust_Ins(String EmpNo, TitaVo... titaVo);

}
