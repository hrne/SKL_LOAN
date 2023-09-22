package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.L7206Emp;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface L7206EmpService {

  /**
   * findByPrimaryKey
   *
   * @param logNo PK
   * @param titaVo Variable-Length Argument
   * @return L7206Emp L7206Emp
   */
  public L7206Emp findById(Long logNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice L7206Emp L7206Emp of List
   */
  public Slice<L7206Emp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By L7206Emp
   * 
   * @param logNo key
   * @param titaVo Variable-Length Argument
   * @return L7206Emp L7206Emp
   */
  public L7206Emp holdById(Long logNo, TitaVo... titaVo);

  /**
   * hold By L7206Emp
   * 
   * @param l7206Emp key
   * @param titaVo Variable-Length Argument
   * @return L7206Emp L7206Emp
   */
  public L7206Emp holdById(L7206Emp l7206Emp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param l7206Emp Entity
   * @param titaVo Variable-Length Argument
   * @return L7206Emp Entity
   * @throws DBException exception
   */
  public L7206Emp insert(L7206Emp l7206Emp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param l7206Emp Entity
   * @param titaVo Variable-Length Argument
   * @return L7206Emp Entity
   * @throws DBException exception
   */
  public L7206Emp update(L7206Emp l7206Emp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param l7206Emp Entity
   * @param titaVo Variable-Length Argument
   * @return L7206Emp Entity
   * @throws DBException exception
   */
  public L7206Emp update2(L7206Emp l7206Emp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param l7206Emp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(L7206Emp l7206Emp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param l7206Emp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<L7206Emp> l7206Emp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param l7206Emp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<L7206Emp> l7206Emp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param l7206Emp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<L7206Emp> l7206Emp, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * 更新利害關係人員工資料
   * @param  EmpNo String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L7_L7206Emp_Ins(String EmpNo, TitaVo... titaVo);

}
