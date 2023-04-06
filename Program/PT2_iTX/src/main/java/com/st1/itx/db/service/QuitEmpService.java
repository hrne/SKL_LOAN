package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.QuitEmp;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface QuitEmpService {

  /**
   * findByPrimaryKey
   *
   * @param empNo PK
   * @param titaVo Variable-Length Argument
   * @return QuitEmp QuitEmp
   */
  public QuitEmp findById(String empNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice QuitEmp QuitEmp of List
   */
  public Slice<QuitEmp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By QuitEmp
   * 
   * @param empNo key
   * @param titaVo Variable-Length Argument
   * @return QuitEmp QuitEmp
   */
  public QuitEmp holdById(String empNo, TitaVo... titaVo);

  /**
   * hold By QuitEmp
   * 
   * @param quitEmp key
   * @param titaVo Variable-Length Argument
   * @return QuitEmp QuitEmp
   */
  public QuitEmp holdById(QuitEmp quitEmp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param quitEmp Entity
   * @param titaVo Variable-Length Argument
   * @return QuitEmp Entity
   * @throws DBException exception
   */
  public QuitEmp insert(QuitEmp quitEmp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param quitEmp Entity
   * @param titaVo Variable-Length Argument
   * @return QuitEmp Entity
   * @throws DBException exception
   */
  public QuitEmp update(QuitEmp quitEmp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param quitEmp Entity
   * @param titaVo Variable-Length Argument
   * @return QuitEmp Entity
   * @throws DBException exception
   */
  public QuitEmp update2(QuitEmp quitEmp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param quitEmp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(QuitEmp quitEmp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param quitEmp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<QuitEmp> quitEmp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param quitEmp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<QuitEmp> quitEmp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param quitEmp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<QuitEmp> quitEmp, TitaVo... titaVo) throws DBException;

}
