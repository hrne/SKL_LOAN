package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdAoDept;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdAoDeptService {

  /**
   * findByPrimaryKey
   *
   * @param employeeNo PK
   * @param titaVo Variable-Length Argument
   * @return CdAoDept CdAoDept
   */
  public CdAoDept findById(String employeeNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdAoDept CdAoDept of List
   */
  public Slice<CdAoDept> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * EmployeeNo &gt;= ,AND EmployeeNo &lt;= 
   *
   * @param employeeNo_0 employeeNo_0
   * @param employeeNo_1 employeeNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdAoDept CdAoDept of List
   */
  public Slice<CdAoDept> findEmployeeNo(String employeeNo_0, String employeeNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdAoDept
   * 
   * @param employeeNo key
   * @param titaVo Variable-Length Argument
   * @return CdAoDept CdAoDept
   */
  public CdAoDept holdById(String employeeNo, TitaVo... titaVo);

  /**
   * hold By CdAoDept
   * 
   * @param cdAoDept key
   * @param titaVo Variable-Length Argument
   * @return CdAoDept CdAoDept
   */
  public CdAoDept holdById(CdAoDept cdAoDept, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdAoDept Entity
   * @param titaVo Variable-Length Argument
   * @return CdAoDept Entity
   * @throws DBException exception
   */
  public CdAoDept insert(CdAoDept cdAoDept, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdAoDept Entity
   * @param titaVo Variable-Length Argument
   * @return CdAoDept Entity
   * @throws DBException exception
   */
  public CdAoDept update(CdAoDept cdAoDept, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdAoDept Entity
   * @param titaVo Variable-Length Argument
   * @return CdAoDept Entity
   * @throws DBException exception
   */
  public CdAoDept update2(CdAoDept cdAoDept, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdAoDept Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdAoDept cdAoDept, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdAoDept Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdAoDept> cdAoDept, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdAoDept Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdAoDept> cdAoDept, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdAoDept Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdAoDept> cdAoDept, TitaVo... titaVo) throws DBException;

}
